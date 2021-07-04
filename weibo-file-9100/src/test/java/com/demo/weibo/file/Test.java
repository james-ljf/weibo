package com.demo.weibo.file;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;


public class Test {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域  字符串中的值不可以修改
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
    //1 根据视频iD获取视频播放地址
    public static void getPlayUrl() throws Exception{
        //创建初始化对象 第一个参数是OSS中申请的ID，第二个参数是密钥
        DefaultAcsClient client = Test.initVodClient("LTAI5tHgqA6CBFvWCHTNxePq", "ulGp3ZwQ1iIw9W8UZtSINITadSltwN");

        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId("37b78722a7be4bcb90cf871714674789");

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        System.out.print("PlayInfo.PlayURL = " + playInfoList.get(0).getPlayURL() + "\n");
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
    // 上传测试
    public static void testUpload(){
        String accessKeyId = "LTAI5tHgqA6CBFvWCHTNxePq";
        String accessKeySecret = "ulGp3ZwQ1iIw9W8UZtSINITadSltwN";

        String title = "test.mp4";   //上传之后文件名称
        String fileName = "D:\\MP4\\test.mp4";  //本地文件路径和名称
        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    public static void main(String[] args) {
        try {
            getPlayUrl();
//            testUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
