package com.demo.weibo.file.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.file.service.FileService;
import com.demo.weibo.file.util.ConstantPropertiesUtils;
import com.demo.weibo.file.util.ConstantVodUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {


    //全局变量
    private InputStream inputStream;

    @Override
    public List<String> uploadImageFile(MultipartFile[] files) {
        //局部静态常量
        String endpoint = ConstantPropertiesUtils.END_POINT;

        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;

        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;

        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        List<String> fileList = new ArrayList<>();
        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            for (int i = 0; i < files.length; i++) {
                //获取上传文件输入流
                inputStream = files[i].getInputStream();
                //获取文件名称
                String fileName = null;
                fileName = files[i].getOriginalFilename();

                //文件名加入唯一uuid
                String uuid = IdGenerator.simpleUUID();
                fileName = uuid + fileName;

                //把文件按照日期进行分类
                //获取当前日期
                String datePath = DateUtil.dateTime(new Date());
                //拼接
                //  如20201/11/12/ewtqr313401.jpg
                fileName = datePath + "/" + fileName;

                //设置图片为预览模式
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType("image/jpg");

                //调用oss方法实现上传
                ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);
                //将文件路径放到list数组里
                fileList.add("https://" + bucketName + "." + endpoint + "/" + fileName);
            }

            //文件全部上传完成,关闭OSSClient。
            ossClient.shutdown();
            //返回文件路径数组
            System.out.println(fileList);
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String uploadOneFile(MultipartFile file) {
        //局部静态常量
        String endpoint = ConstantPropertiesUtils.END_POINT;

        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;

        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;

        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        try {

            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //文件名加入唯一uuid
            String uuid = IdGenerator.simpleUUID();
            fileName = uuid + fileName;

            //把文件按照日期进行分类
            //获取当前日期
            String datePath = DateUtil.dateTime(new Date());
            //拼接
            //  如20201/11/12/ewtqr313401.jpg
            fileName = datePath + "/" + fileName;

            //设置图片为预览模式
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);


            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //如  https://yk1001.oss-cn-beijing.aliyuncs.com/01.jpg

            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        try {

            //fileName：上传文件原始名称
            // 01.03.09.mp4
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else {
                //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空
                videoId = response.getVideoId();
            }
            //调用工具类方法通过 videoId 获取视频播放链接并返回
            return ConstantVodUtils.getPlayUrl(videoId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
