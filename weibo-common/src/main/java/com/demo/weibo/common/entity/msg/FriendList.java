package com.demo.weibo.common.entity.msg;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 前端获取好友列表信息
 */
@Data
@Accessors(chain = true)
public class FriendList {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

    private String nickname;

    private String avatar;

}
