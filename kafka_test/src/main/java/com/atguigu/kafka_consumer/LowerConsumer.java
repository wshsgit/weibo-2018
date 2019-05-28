package com.atguigu.kafka_consumer;


import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.cluster.BrokerEndPoint;

import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by wss on 2019/4/8
 */
public class LowerConsumer {

    public static void main(String[] args) {
        // 定义相关参数
        // kafka集群
        List<String> seedBrokers = Arrays.asList("hadoop101", "hadoop102", "hadoop103");
        // 端口号
        int port = 9092;
        // 主题
        String topic = "first";
        // 分区
        int partition = 0;
        // offset
        long offset = 0;
        LowerConsumer lowerConsumer = new LowerConsumer();
        lowerConsumer.getData(seedBrokers, port, topic, partition, offset);
    }
        // 找分区Leader
    private BrokerEndPoint findLeader(List<String> seedBrokers, int port, String topic, int partition) {
        // 遍历所有broker
        for (String seed : seedBrokers) {
            // javaapi，实际是scala api，特别底层的一个类
            // SimpleConsumer和KafkaConsumer区别：
            // SimpleConsumer连到某一台具体的节点
            // KafkaConsumer bootstrap-server，任何一台节点就行，能根据那台节点找到kafka集群，同样是做一个遍历
            // 真正获取数据、元数据都是通过SimpleConsumer，因为SimpleConsumer是实际跟某一个节点打交道
            // 创建获取partition Leader的消费者对象
            SimpleConsumer consumer = new SimpleConsumer(
                    seed, port, 1000, 1024 * 4, "lookup_leader");
            // 创建一个topic元数据信息请求
            // 注意：形参类型是List，表示可以一次请求多个topic的元数据信息
            TopicMetadataRequest topicMetadataRequest =
            new TopicMetadataRequest(Collections.singletonList(topic));
            // 获取topic元数据返回值
            TopicMetadataResponse topicMetadataResponse =
            consumer.send(topicMetadataRequest);
            // 获取多个topic元数据信息
            // 由于可以一次请求多个topic的元数据信息，所以返回的是topic元数据的List集合
            List<TopicMetadata> topicMetadatas = topicMetadataResponse.topicsMetadata();
            // 遍历每一个topic元数据信息
            for (TopicMetadata topicMetadata : topicMetadatas) {
                // 获取每个topic的所有partition的元数据信息
                List<PartitionMetadata> partitionsMetadata = topicMetadata.partitionsMetadata();
                // 遍历每一个partition元数据信息
                for (PartitionMetadata partitionMetadata : partitionsMetadata) {
                    // 找我们指定的分区0
                    if (partition == partitionMetadata.partitionId()) {
                        // 实现findNewLeader()，需要保存副本List，避免重新遍历所有的broker
                        // List<BrokerEndPoint> isr = partitionMetadata.isr();
                        // List<BrokerEndPoint> replicas = partitionMetadata.replicas();
                        return partitionMetadata.leader();
                    }
                }
            }
        }
        return null;
    }
        // 获取数据
    private void getData(List<String> seedBrokers, int port, String topic, int partition, long offset) {
        // 获取partition的Leader
        BrokerEndPoint leader = findLeader(seedBrokers, port, topic, partition);
        if (leader == null) return;
        String leaderHost = leader.host();
        // 获取数据的消费者对象
        SimpleConsumer consumer =
        new SimpleConsumer(leaderHost, port, 1000, 1024 * 4, "getData");
        while (true) {
            // 获取fetch请求
            // fetchSize：类型虽然是int，但是表示的是一次读取到的最多的字节数，并不是条数
            // 如果这个值设置过小，则至少保证一次读取一条记录，为了读取效率的提高，可以把这个值设置的大一些
            FetchRequest fetchRequest =
            new FetchRequestBuilder().addFetch(topic, partition, offset, 1024).build();
            // 注意可以一直addFectch()：
            // new FetchRequestBuilder().addFetch().addFetch().addFetch()....build()
            // 所以可以一次获取多个不同topic不同partition的fetch请求
            // 获取数据返回值
            FetchResponse fetchResponse = consumer.fetch(fetchRequest);
            // 解析数据返回值
            // 由于可以一次获取多个不同topic不同partition的fetch请求，所以需要指定topic指定partition
            ByteBufferMessageSet messageAndOffsets = fetchResponse.messageSet(topic, partition);
            // 一次可以拉取多条数据，所以可以遍历，有iterator就可以增强for循环遍历
            // messageAndOffsets.iterator()
            // 遍历一次拉取的多条数据
            for (MessageAndOffset messageAndOffset : messageAndOffsets) {
                // messageAndOffset这才是一条数据
                long offset1 = messageAndOffset.offset();
                // 由于kafka的数据有序列化，我们又没有像高级API提供一个反序列化类，
                // 所以我们需要自己实现反序列化的过程来获取数据
                ByteBuffer payload = messageAndOffset.message().payload();
                // 创建一个长度为payload.limit()的byte数组
                byte[] bytes = new byte[payload.limit()];
                // payload把数据放到空的byte数组中
                payload.get(bytes);
                // 打印数据
                System.out.println(offset1 + "-" + new String(bytes));
                offset = offset1;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (messageAndOffsets.iterator().hasNext()) {
                // 如果这次确实读到数据了，所以把offset的值++，下一次读的时候就从新的位置开始读了
                // 如果这次没有读到数据，表示数据已经读完了，所以就不需要再++了
                // 这个offset 只是针对当前分区的
                // 如果想下次启动的时候从上次位置开始读取，则应该把offset的值持久化，
                // 比如存到Zookeeper 或者 mysql 数据库中，然后下次在获取fetch请求之前获取offset
                offset++;
                System.out.println("拉取完一批数据，现在offset为：" + offset);
            }
        }
    }
}
