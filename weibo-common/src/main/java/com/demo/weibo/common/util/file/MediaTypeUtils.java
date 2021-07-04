package com.demo.weibo.common.util.file;

import cn.hutool.core.io.FileTypeUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 媒体类型工具类
 * 
 * @author ruoyi
 */
public class MediaTypeUtils
{
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = { "bmp", "gif", "jpg", "jpeg", "png" };

    public static final String[] FLASH_EXTENSION = { "swf", "flv" };

    public static final String[] MEDIA_EXTENSION = { "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb" };

    public static final String[] VIDEO_EXTENSION = { "mp4", "avi", "rmvb" };

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf" };

    /**
     * 判断是否是图片
     * @param file
     * @return 0 上传类型不全是图片   1 上传的全是图片
     */
    public static String getImage(List<MultipartFile> file){
        boolean img = false;
        for (MultipartFile multipartFile : file) {
            //获取上传文件原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件类型
            String fileType = FileTypeUtil.getType(originalFilename);
            //判断是否是图片类型
            for (String s : IMAGE_EXTENSION) {
                //判断当前是不是图片类型，是的话img+1
                if (fileType.equals(s)) {
                    img = true;
                    break;
                }
            }
            for (String r : VIDEO_EXTENSION) {
                //判断是不是有视频类型，有的话video+1
                if (fileType.equals(r)){
                    img = false;
                    return "0";
                }
            }
        }
        if (img = false){
            return "0";
        }
       return "1";
    }


    public static String getVideo(String type){
        for (String s : VIDEO_EXTENSION) {
            if (type.equals(s)){
                return s;
            }
        }
        return "";
    }

    public static String getExtension(String prefix)
    {
        switch (prefix)
        {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
