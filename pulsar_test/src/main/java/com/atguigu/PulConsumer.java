package com.atguigu;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wss on 2019/4/16
 */
public class PulConsumer {

    private Client client;
    private Consumer consumer;

    public PulConsumer(String topic, String subscription) throws PulsarClientException {
        client = new Client();
        consumer = createConsumer(topic, subscription);
    }

    private Consumer createConsumer(String topic, String subscription) throws PulsarClientException {

        return client.getPulsarClient().newConsumer().topic(topic).subscriptionName(subscription)
                .ackTimeout(10, TimeUnit.SECONDS).subscriptionType(SubscriptionType.Exclusive).subscribe();
    }


    public String getMessage() throws ExecutionException, InterruptedException, PulsarClientException {
        /***
         * 获取一次，就关闭会话
         */
        // Wait for a message
        System.out.printf("Start pulsar");
        CompletableFuture<Message> msg = consumer.receiveAsync();

        // System.out.printf("Message received: %s", new String(msg.get().getData()));
        String result = "topic is: " + msg.get().getTopicName() + ",data is: " + new String(msg.get().getData());

        // Acknowledge the message so that it can be deleted by the message broker
        consumer.acknowledge(msg.get());
        consumer.close();
        client.Close();
        return result;
    }

    public static void main(String[] args) throws PulsarClientException, ExecutionException, InterruptedException {
        PulConsumer consumer = new PulConsumer("topic1", "my-sub");
        String reString = consumer.getMessage();
        System.out.println(reString);

    }
}
