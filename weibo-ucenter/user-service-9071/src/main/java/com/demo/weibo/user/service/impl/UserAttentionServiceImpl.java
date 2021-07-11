package com.demo.weibo.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.UserAttention;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.UserAttentionMongo;
import com.demo.weibo.common.entity.msg.FriendList;
import com.demo.weibo.common.entity.msg.U2;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.mapper.UserDetailMapper;
import com.demo.weibo.user.service.UserAttentionService;
import com.demo.weibo.user.util.AttentionMongoComponent;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserAttentionServiceImpl implements UserAttentionService {

    @Autowired
    private AttentionMongoComponent attentionMongoComponent;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserDetailMapper userDetailMapper;


    /**
     * 添加关注
     *                / userAttention => 当前用户的实体    list => 该对象里的关注对象数组
     *              /    userAttentionMongo => 该对象的list对象数组的泛型对象
     *            /-----------------------------------------------------------
     *          /  attention => 当前用户在mongodb中的集合实体（也就是从mongodb中查出来的）
     * UserAttention
     *          \  userAttentionU2 => 被关注用户的实体     fansList => 该对象里的粉丝对象数组
     *           \  userFansMongo => 该对象的list对象数组的泛型对象
     *           ---------------------------------------------------------------------
     *             \selectUserAttentionU2 => 被关注用户在mongodb中的集合实体（也就是从mongodb中查出来的）
     */
    @Override
    public R addUserAttention(UserAttention userAttention) {

        //查询当前用户的集合
        UserAttention attention = attentionMongoComponent.selectUserAttention(userAttention);

        //获取UserAttention对象中的数组对象
        List<UserAttentionMongo> list = userAttention.getAttentionList();
        UserAttentionMongo userAttentionMongo = list.get(0);

        //查询当前用户是否关注了另一个用户  selectList(当前用户，用户2)
        JSONObject object = attentionMongoComponent.selectList(userAttention.getU1Id(), userAttentionMongo.getU2Id());
        if (object != null){
            return R.error("已关注该用户，请不要重复关注");
        }

        //查询被关注的用户是否关注了当前用户
        JSONObject jsonObject = attentionMongoComponent.selectList(userAttentionMongo.getU2Id(), userAttention.getU1Id());

        if (jsonObject == null){ // 被关注用户没有关注当前用户

            //设置关注状态为1，即单向关注
            userAttentionMongo.setCode("1");
            list.set(0, userAttentionMongo);

        }else {

            //设置关注状态为2，即互相关注
            userAttentionMongo.setCode("2");
            list.set(0, userAttentionMongo);

            //更新被关注的用户对当前用户的关注状态为2(互相关注)
            Query query = Query.query(Criteria.where("_id")
                    .is(userAttentionMongo.getU2Id())
            .and("attentionList._id").is(userAttention.getU1Id()));
            Update update = Update.update("attentionList.$.aCode", "2");
            mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);

        }

        //将数组对象存入UserAttention对象中
        userAttention.setAttentionList(new ArrayList<>());

        //从redis缓存获取当前用户的所有信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttention.getU1Id());

        //如果当前用户不存在缓存
        if (userDetail == null){

            //缓存没有，查数据库
            QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
            userDetail = userDetailMapper.selectOne(wrapper.eq("u_id", userAttention.getU1Id()));

            if (userDetail == null){
                //查询数据库中也没有
                return R.error("出错了，请进行反馈");
            }

        }

        //将当前用户的关注消息存到mongodb，以便于推送给被关注用户
        boolean res = attentionMongoComponent.addAttentionMessage(userAttention.getU1Id(), userAttentionMongo.getU2Id());

        //获取当前用户关注的人数并更新
        int a = userDetail.getAttention();
        userDetail.setAttention(a+1);

        //获取被关注用户的信息并更新粉丝数量
        UserDetail userDetail_2 = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttentionMongo.getU2Id());
        assert userDetail_2 != null;
        int b = userDetail_2.getFans();
        userDetail_2.setFans(b+1);

        //查询并判断被关注的用户是否创建了集合
        ////新建被关注用户的对象
        UserAttention userAttentionU2 = new UserAttention();
        userAttentionU2.setU1Id(userAttentionMongo.getU2Id());

        ////使用工具类获取是否创建了集合
        UserAttention selectUserAttentionU2 = attentionMongoComponent.selectUserAttention(userAttentionU2);

        ////设置粉丝对象数组
        UserAttentionMongo userFansMongo = new UserAttentionMongo();
        userFansMongo.setU2Id(userAttention.getU1Id()).setCode("1");
        List<UserAttentionMongo> fansList = new ArrayList<>();


        ////判断是否为空
        if (selectUserAttentionU2 == null){

            selectUserAttentionU2 = new UserAttention();

            ////添加到userAttention对象
            selectUserAttentionU2.setFansList(fansList).setAttentionList(new ArrayList<>()).setU1Id(userAttentionU2.getU1Id());

            ////在mongodb创建被关注的用户的集合
            mongoTemplate.save(selectUserAttentionU2);

            //插入到内嵌对象数组里
            Query query = Query.query(Criteria.where("_id").is(selectUserAttentionU2.getU1Id()));
            Update update = new Update();
            update.addToSet("fansList", userFansMongo);
            mongoTemplate.upsert(query, update, UserAttentionMongo.class);

        }else{

            ////不为空，也就是已经创建了被关注用户的集合,
            ////_id为被关注用户id，添加当前用户到被关注用户集合的fanslist对象数组中
            Query query = Query.query(Criteria.where("_id").is(selectUserAttentionU2.getU1Id()));
            Update update = new Update();
            update.addToSet("fansList", userFansMongo);
            UpdateResult r = mongoTemplate.upsert(query, update, UserAttentionMongo.class);

            //判断是否成功写入
            if (!r.wasAcknowledged()){
                log.info("出现错误，写入mongodb失败");
            }

        }

        //判断当前用户的集合是否已经创建
        if (attention == null) {

            //新建当前用户id为主键的集合
            mongoTemplate.save(userAttention);

            //重新存进缓存
            redisTemplate.opsForValue().set("UserDetail:" + userDetail_2.getUId(), userDetail_2);
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);



            //添加被关注用户到当前用户集合的attentionlist对象数组中
            Query query = Query.query(Criteria.where("_id").is(userAttention.getU1Id()));
            Update update = new Update();
            update.addToSet("attentionList", userAttentionMongo);
            mongoTemplate.upsert(query, update, UserAttentionMongo.class);
            return R.ok("关注成功");

        }

        //_id为当前用户id，添加被关注用户到当前用户集合的attentionlist对象数组中
        Query query = Query.query(Criteria.where("_id").is(userAttention.getU1Id()));
        Update update = new Update();
        update.addToSet("attentionList", userAttentionMongo);
        UpdateResult result = mongoTemplate.upsert(query, update, UserAttentionMongo.class);

        //重新将两个用户信息更新到缓存
        redisTemplate.opsForValue().set("UserDetail:" + userDetail_2.getUId(), userDetail_2);
        redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);

        UpdateWrapper<UserDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("u_id", userDetail.getUId());
        userDetailMapper.update(userDetail, updateWrapper);

        updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("u_id", userDetail_2.getUId());
        userDetailMapper.update(userDetail_2, updateWrapper);

        return R.ok("关注成功");
    }

    /**
     * 取消关注
     */
    @Override
    public R cancelUserAttention(UserAttention userAttention) {
        //查询该用户的集合是否已经创建
        UserAttention attention = attentionMongoComponent.selectUserAttention(userAttention);
        if (attention == null){  // 没有创建
            return R.error("你没有关注该用户");
        }

        //获取UserAttention对象中的数组对象
        List<UserAttentionMongo> list = userAttention.getAttentionList();
        UserAttentionMongo userAttentionMongo = list.get(0);

        //查询并判断当前用户是否关注了另一个用户
        JSONObject object = attentionMongoComponent.selectList(userAttention.getU1Id(), userAttentionMongo.getU2Id());
        if (object != null){ //已关注另一个用户

            //删除对另一个用户的关注信息
            Query query = Query.query(Criteria.where("_id").is(userAttention.getU1Id()));
            Update update = new Update();
            Document document=new Document("_id", userAttentionMongo.getU2Id());
            //使用pull()删除document中条件的内嵌数组的一个集合
            update.pull("attentionList", document);
            UpdateResult result = mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);

            //从redis缓存获取当前用户的所有信息
            UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttention.getU1Id());
            //获取当前用户关注的人数并更新
            assert userDetail != null;
            int a = userDetail.getAttention();
            userDetail.setAttention(a-1);

            //从redis获取被取消关注用户的所有信息
            UserDetail userDetail_2 = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttentionMongo.getU2Id());
            //获取被取消关注用户粉丝人数并更新
            assert userDetail_2 != null;
            int b = userDetail_2.getFans();
            userDetail_2.setFans(b-1);

            //重新存进缓存
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
            redisTemplate.opsForValue().set("UserDetail:" + userDetail_2.getUId(), userDetail_2);

            UpdateWrapper<UserDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("u_d", userDetail.getUId());
            userDetailMapper.update(userDetail, updateWrapper);

            updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("u_d", userDetail_2.getUId());
            userDetailMapper.update(userDetail_2, updateWrapper);

            //删除另一个用户的粉丝列表里的当前用户
            query = Query.query(Criteria.where("_id").is(userAttentionMongo.getU2Id()));
            update = new Update();
            document=new Document("_id", userAttention.getU1Id());
            //使用pull()删除document中条件的内嵌数组的一个集合
            update.pull("fansList", document);
            result = mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);

            //判断是否删除成功
            if (!result.wasAcknowledged()){
                log.info("出错了，没能删除另一个用户的粉丝列表中的当前用户");
            }

            //查询被关注的用户是否关注了当前用户
            JSONObject jsonObject = attentionMongoComponent.selectList(userAttentionMongo.getU2Id(), userAttention.getU1Id());

            if (jsonObject != null){ // 被关注用户关注了当前用户

                //设置被关注用户对当前用户的关注状态为1，即单向关注
                update = new Update();
                update.set("attentionList.$.aCode", "1");
                query = new Query(Criteria.where("_id").is(userAttentionMongo.getU2Id())
                        .and("attentionList._id").is(userAttention.getU1Id()));
                mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);

                return R.ok("取消关注成功");
            }

        }

        return R.error("你没有关注该用户，无法取消关注");
    }

    @Override
    public R findAllUserAttention(Long uId) {
        //获取当前用户关注数组
        List<UserAttentionMongo> attentionList = attentionMongoComponent.selectAttentionList(uId);

        //存放关注用户的信息
        List<UserDetail> userDetailList = new ArrayList<>();

        //判断是否有数据
        if (attentionList != null && attentionList.size() > 0){

            //遍历
            for (UserAttentionMongo object : attentionList) {

                //从redis获取用户信息
                UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + object.getU2Id());
                if (userDetail == null){

                    //缓存没有，查数据库
                    Long userId = object.getU2Id();
                    userDetailMapper.selectById(userId);

                }

                //存到数组
                userDetailList.add(userDetail);

            }

            return R.ok("查询关注列表成功").addData("userDetailList", userDetailList);
        }

        return R.error("当前用户没有任何关注");
    }

    @Override
    public List<UserAttentionMongo> findAllMyAttention(Long uId) {

        //获取当前用户关注数组
        List<UserAttentionMongo> attentionList = attentionMongoComponent.selectAttentionList(uId);
        if (attentionList != null && attentionList.size() > 0){

            return attentionList;
        }
        return null;
    }

    @Override
    public R findAllUserFriend(Long uId) {
        List<UserAttentionMongo> friendList = attentionMongoComponent.selectFriendList(uId);

        UserDetail userDetailMe = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);

        //判断是否为空
        if (friendList.isEmpty()){
            return R.error("您目前没有好友").addData("myself", userDetailMe);
        }
        //新建对象数组存放好友数据
        List<FriendList> mapList = new ArrayList<>();

        //遍历好友列表，从缓存获取每个好友的昵称,然后以FriendList对象形式存到list数组里
        for (UserAttentionMongo jsonObject : friendList) {

            UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + jsonObject.getU2Id());
            assert userDetail != null;
            FriendList uFriendList = new FriendList();
            uFriendList.setUId(userDetail.getUId());
            uFriendList.setNickname(userDetail.getNickname());
            uFriendList.setAvatar(userDetail.getAvatar());
            mapList.add(uFriendList);

        }

        return R.ok("获取好友列表成功").addData("friendList", mapList).addData("myself", userDetailMe);
    }

    @Override
    public R findAllUserFans(Long uId) {

        //获取用户粉丝列表
        List<UserAttentionMongo> fansList = attentionMongoComponent.selectFansList(uId);

        //存放用户信息
        List<UserDetail> userDetailList = null;

        //判断是否有数据
        if (fansList != null && fansList.size() > 0){

            userDetailList = new ArrayList<>();

            //遍历
            for (UserAttentionMongo object : fansList) {

                //从redis获取用户信息
                UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + object.getU2Id());
                if (userDetail == null){

                    //缓存没有，查数据库
                    Long userId = object.getU2Id();
                    userDetailMapper.selectById(userId);

                }

                //存到数组
                userDetailList.add(userDetail);

            }

            return R.ok("查询粉丝列表成功").addData("userDetailList", userDetailList);
        }

        return R.error("当前用户没有任何粉丝");
    }
}
