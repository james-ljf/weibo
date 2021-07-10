package com.demo.weibo.microblog.consumer;



import com.demo.weibo.common.entity.mq.MicroblogMQ;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.binding.MicroblogBinding;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


import java.util.concurrent.TimeUnit;

/**
 * 点赞功能消费者
 */
@Component
@Slf4j
public class MicroblogConsumer {

    private static Integer NUM = 0;

    @Autowired
    private MicroblogOperationService microblogOperationService;

    @Autowired
    private MicroblogBinding microblogBinding;

    @StreamListener(MicroblogBinding.INPUT)
    public void addOrCancel(MicroblogMQ message) throws InterruptedException {

        try {
            //根据前端传来的strategy值判断是添加关注还是取消关注
            if (message.getStrategy().equals(1)){
                //添加点赞
                R r = microblogOperationService.addLikeWeibo(message.getUId(), message.getCId());
                log.info(r.getMessage());

            }else if (message.getStrategy().equals(2)){
                //取消点赞
                R r = microblogOperationService.cancelLikeWeibo(message.getUId(), message.getCId());
                log.info(r.getMessage());
            }
        } catch (Exception e) {

            e.printStackTrace();

            if (NUM == 10){
                //重试达到十次则通知开发者
                log.info("尝试消费十次未能成功消费，请解决。");
                NUM = 0;
            }
            NUM++;

            //重新丢回消息队列进行消费
            microblogBinding.setLike().send(MessageBuilder.withPayload(message).build());
        }

        TimeUnit.MILLISECONDS.sleep(20); //一秒钟消费50个消息

    }


}
