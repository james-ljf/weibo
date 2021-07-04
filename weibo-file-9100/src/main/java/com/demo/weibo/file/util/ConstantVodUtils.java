package com.demo.weibo.file.util;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 阿里云视频点播配置
 */
@Component
public class ConstantVodUtils  implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    private String keyid;

    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;

    public static String ACCESS_KEY_SECRET;
    public static String ACCESS_KEY_ID;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyid;
        ACCESS_KEY_SECRET = keysecret;
    }

    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域  字符串中的值不可以修改
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    //根据视频iD获取视频播放地址
    public static String getPlayUrl(String videoId) throws Exception{
        //创建初始化对象 第一个参数是OSS中申请的ID，第二个参数是密钥
        DefaultAcsClient client = ConstantVodUtils.initVodClient("LTAI5tHgqA6CBFvWCHTNxePq", "ulGp3ZwQ1iIw9W8UZtSINITadSltwN");

        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId(videoId);

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        return playInfoList.get(0).getPlayURL();

    }
}
