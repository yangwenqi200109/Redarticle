package com.ape.kafkademo.kfk.controller;

import com.alibaba.fastjson.JSON;
import com.ape.kafkademo.kfk.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello(){
//        kafkaTemplate.send("test-topic","HelloWorld");
        User user = new User();
        user.setUsername("李耀东");
        user.setAge(18);
        kafkaTemplate.send("test-topic", JSON.toJSONString(user));
        return "ok";
    }
}
