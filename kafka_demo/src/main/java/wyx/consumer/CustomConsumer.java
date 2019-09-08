package consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import utils.PropertyUtils;

import java.io.*;
import java.util.*;

public class CustomConsumer {
    private static Map<TopicPartition, Long> offset = new HashMap<TopicPartition, Long>();


    public static void main(String[] args) {
        Properties prop = PropertyUtils.getConsumerProperties();
        prop.put("enable.auto.commit", "false");//关闭自动提交offset
        final Consumer<String, String> consumer = new KafkaConsumer<String, String>(prop);
        consumer.subscribe(Collections.singletonList("first"), new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("Before Rebalance=================================");
                String lock = (System.currentTimeMillis() + "").intern();
//                synchronized (lock){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    commitOffset(offset);
//                }

            }
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

                System.out.println("After Rebalance=================================");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getOffset(offset);
                for (TopicPartition partition : partitions) {
                    Long newOffset = offset.get(partition);
                    if (newOffset == null) {
                        newOffset = 0L;
                    }
//                    offset.put(partition, newOffset);
                    consumer.seek(partition, newOffset);
                }
            }
        });

//        for (TopicPartition topicPartition : offset.keySet()) {
//            consumer.seek(topicPartition, offset.get(topicPartition));
//        }

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            String lock = (System.currentTimeMillis() + "").intern();
//            synchronized (lock){
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.topic() + "-" +  record.partition() + "value: " + record.value());
                    offset.put(new TopicPartition(record.topic(), record.partition()), record.offset());
                }
                commitOffset(offset);
//            }
        }

    }


    private static void getOffset(Map<TopicPartition, Long> offSetMap){
        ObjectInputStream ois = null;
        try{
            ois = new ObjectInputStream(new FileInputStream("E:/_test_data/kafka_offset"));

            offset = (Map<TopicPartition, Long>) ois.readObject();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void commitOffset(Map<TopicPartition, Long> offSetMap){
//        if (offSetMap == null || offSetMap.size() == 0) {
//            return;
//        }
        ObjectOutputStream oos = null;
        try{
           oos = new ObjectOutputStream(new FileOutputStream("E:/_test_data/kafka_offset"));

           oos.writeObject(offSetMap);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
