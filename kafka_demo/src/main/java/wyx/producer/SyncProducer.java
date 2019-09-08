package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import utils.PropertyUtils;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SyncProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties prop = PropertyUtils.getProducerProperties();
        Producer<String, String> producer = new KafkaProducer<String, String>(prop);
//        for (int i = 0; i < 10; i++) {
        int i = 0;
        while (i++ >= 0){
            Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>("first", ("sync" + i)));
            RecordMetadata recordMetadata = future.get();
            System.out.println(recordMetadata.toString() + " value: " + i);
            Thread.sleep(100);
        }
        System.out.println("Send Finish");

    }
}
