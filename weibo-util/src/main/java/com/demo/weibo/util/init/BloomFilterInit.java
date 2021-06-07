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
        List<Long> list = signClient.selectAllId();
        for (Long aLong : list) {
            RedissonBloomFilter.add(aLong.toString());
        }
        System.out.println("布隆过滤器正在初始化");
    }
}
