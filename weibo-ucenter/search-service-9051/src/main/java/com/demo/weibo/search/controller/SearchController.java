package com.demo.weibo.search.controller;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.ObjectUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/by-keyword")
    public Object searchByKeyword(HttpServletRequest request, @RequestParam("keyword") String keyword){
        User user = (User) request.getAttribute("weiboUser");
        R r = searchService.searchByKeyword(user.getId(), keyword, 0);

        if (r.getCode() == 1 && !"".equals(keyword)){
            //搜索内容非空才能高亮
            //高亮处理
            List<Weibo> weiboList = (List<Weibo>) r.getData().get("weiboList");
            for (Weibo weibo : weiboList) {
                ObjectUtil.highLightReplace(weibo, keyword,"microblog.cContent", "keywords");
            }
        }
        return r;
    }

}
