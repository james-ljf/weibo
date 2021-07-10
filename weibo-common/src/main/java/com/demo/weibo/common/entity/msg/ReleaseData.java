package com.demo.weibo.common.entity.msg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ReleaseData {

    private String content;

    private List<String> image;

    private String video;

}
