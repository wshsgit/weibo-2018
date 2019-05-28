package com.atguigu;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * Created by wss on 2019/4/16
 */
public class Client {

    private PulsarClient client;

    public Client() throws PulsarClientException {
        client = PulsarClient.builder()
                .serviceUrl("pulsar://hadoop101:6650")
                .build();
    }
    public void Close() throws PulsarClientException {
        client.close();
    }
    public PulsarClient getPulsarClient(){
        return client;
    }
}
