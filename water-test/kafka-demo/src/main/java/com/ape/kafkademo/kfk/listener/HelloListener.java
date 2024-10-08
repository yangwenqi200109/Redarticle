package com.ape.kafkademo.kfk.listener;


import com.alibaba.fastjson.JSON;
import com.ape.kafkademo.kfk.pojo.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloListener {
    @KafkaListener(topics = "test-topic")
    public void onMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            User user = JSON.parseObject(message, User.class);
            System.out.println(user);
        }
    }
}
