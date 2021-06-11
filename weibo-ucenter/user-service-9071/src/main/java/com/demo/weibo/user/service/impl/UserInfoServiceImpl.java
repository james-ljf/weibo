package com.demo.weibo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.mapper.UserDetailMapper;
import com.demo.weibo.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Autowired
//    private FileClient fileClient;

    @Override
    public Integer addUserInfo(UserDetail userDetail) {
        return userDetailMapper.insert(userDetail);
    }

    @Override
    public UserDetail selectUserEmail(String email) {
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        return userDetailMapper.selectOne(queryWrapper.eq("email", email));
    }

    @Override
    public R selectUserAll(Long uId) {
        //先查缓存有没有
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            return R.ok("获取用户信息成功").addData("userDetail", userDetail);
        }
        //缓存没有，查数据库
        QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
        userDetail = userDetailMapper.selectOne(wrapper.eq("u_id", uId));
        if (userDetail == null){
            return R.error("获取信息失败");
        }
        redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);
        //stringRedisTemplate.opsForValue().set("userEmail:" + uId, userDetail.getEmail());
        //将email中间部分用*代替
        return R.ok("获取用户信息成功").addData("userDetail", userDetail);
    }

    @Override
    public R updateUserInfo(UserDetail userDetail) {
        UpdateWrapper<UserDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("u_id", userDetail.getUId());
        //根据用户id更改用户信息
        int result = userDetailMapper.update(userDetail, updateWrapper);
        //取出更新的消息并更新到缓存
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", userDetail.getUId());
        userDetail = userDetailMapper.selectOne(queryWrapper);
        redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
        return result > 0 ? R.ok("更新成功").addData("userDetail", userDetail) : R.error("更新失败，请稍后再试");
    }

    @Override
    public R uploadUserAvatar(Long uId, MultipartFile file) {
//        //将头像图片上传至fastDFS服务器
//        String avatarUrl = fileClient.uploadOneFile(file);
//        if (avatarUrl == null){
//            return R.error("上传失败");
//        }
//        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
//        if (userDetail == null){
//            //缓存没有，查数据库
//            QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
//            userDetail = userDetailMapper.selectOne(wrapper.eq("u_id", uId));
//            if (userDetail == null){
//                return R.error("出错了，上传失败");
//            }
//        }
//        userDetail.setAvatar(avatarUrl);
//        //存入缓存
//        redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
//        return R.ok("上传成功").addData("userDetail", userDetail);
        return null;
    }

}
