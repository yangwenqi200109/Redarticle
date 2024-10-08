package com.ape.kafkademo.kfk.startTest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.kafka链接配置信息
        Properties prop = new Properties();
        //kafka链接地址
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //key和value的序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //2.创建kafka生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(prop);
        //3.发送消息
        /**
         * 第一个参数 ：topic
         * 第二个参数：消息的key
         * 第三个参数：消息的value
         */
        ProducerRecord<String, String> kvPRoducerRecord = new ProducerRecord<>("topic-first", "key-01", "你好，kafka");
        //同步发送消息
        RecordMetadata recordMetadata = producer.send(kvPRoducerRecord).get();
        System.out.println(recordMetadata.offset());
        producer.close();
    }
}
