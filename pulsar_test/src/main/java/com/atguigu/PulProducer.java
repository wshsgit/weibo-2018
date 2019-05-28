package com.atguigu;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

import java.util.concurrent.TimeUnit;

/**
 * Created by wss on 2019/4/16
 */
public class PulProducer {


    private Client client;
    private Producer<byte[]> producer;

    public PulProducer(String topic) throws PulsarClientException {
        client = new Client();
        producer = createProducer(topic);
    }

    private Producer<byte[]> createProducer(String topic) throws PulsarClientException {
        return client.getPulsarClient().newProducer()
                .topic(topic)
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
                .sendTimeout(10, TimeUnit.SECONDS)
                .blockIfQueueFull(true)
                .create();
    }

    public void sendMessage(String message) {
        producer.sendAsync(message.getBytes()).thenAccept(msgId -> {
            System.out.printf("Message with ID %s successfully sent", msgId);
        });

    }
    public void sendOnce(String message) {
        /**
         * 发送一次就关闭
         */
        try {
            producer.send(message.getBytes());
            System.out.printf("Message with content %s successfully sent", message);
            producer.close();
            client.Close();
        } catch (PulsarClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // todo add exceptionally().
    public void close(Producer<byte[]> producer){
        producer.closeAsync()
                .thenRun(() -> System.out.println("Producer closed"));
    }

    public static void main(String[] args) throws PulsarClientException {
        PulProducer producer = new PulProducer("topic2");
//        producer.sendMessage("Hello World ,lalla");
        producer.sendOnce("Hello World ,atguigu1");

    }

}
