package com.atguigu.kafka_consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class CustomerConsumer {

    public static void main(String[] args) {

        // ================ 配置信息 ==============
        Properties props = new Properties();
        // 定义kakfa 服务的地址，不需要将所有broker指定上
        props.put("bootstrap.servers", "hadoop102:9092");
        // 指定消费者组id
        props.put("group.id", "test");
        // 设置是否自动提交offset
        props.put("enable.auto.commit", "true");

        // 自动提交offset的时间间隔，指的是获取到数据，过1s再提交offset
        // 消费数据，实际开发中不是简单地消费数据打印在控制台，而是需要对获取到的数据做一些业务处理
        // 获取数据 -> 对数据做业务处理 -> 提交offset，
        // 假如消费者在提交之前挂了（还没提交），但是业务已经处理完了，再启动消费者会出现重复消费数据的问题
        // 或者已经提交，但是业务还没处理完，这时候挂掉了，就会丢失数据
        // 后面我们可以使用低级api来控制，数据处理和提交保持一致
        props.put("auto.commit.interval.ms", "1000");
        // 指定KV的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // ================ 创建消费者 ==============
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // subscribe：订阅，消费什么topic 形参要求是Collection集合，表示一个消费者可以消费多个topic
        // 如果订阅的topic不存在，会抛一个你订阅的topic不存在的异常，自己处理掉了，不会影响其他topic的处理
        consumer.subscribe(Arrays.asList("first", "second", "third"));
        while (true) {
            // poll：拉取，timeout表示拉取超时时间
            // 返回ConsumerRecords，有s表示一次拉取多条数据
            ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.topic() + "-" + consumerRecord.partition() +
                        "-" + consumerRecord.offset() +
                        ":" + consumerRecord.value());
            }
        }
    }
}