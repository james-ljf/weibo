package com.demo.weibo.util.init;

import com.demo.weibo.api.client.SignClient;
import com.demo.weibo.common.filter.RedissonBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BloomFilterInit implements CommandLineRunner {

    @Autowired
    private SignClient signClient;

    @Override
    public void run(String... args) throws Exception {
        List<Long> uIdList = signClient.selectAllId();
        for (Long aLong : uIdList) {
            RedissonBloomFilter.add(aLong.toString());
        }
        System.out.println("布隆过滤器初始化用户id成功");
        List<String> accountList = signClient.selectAllAccount();
        for (String s : accountList) {
            RedissonBloomFilter.add(s);
        }
        System.out.println("布隆过滤器初始化用户账号成功");
    }
}
