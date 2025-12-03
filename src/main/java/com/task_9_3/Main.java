package com.task_9_3;

public class Main {
    public static void main(String[] args) {
        MessageBuffer messageBuffer = new MessageBuffer(10);
        Thread producer = new Thread(new NumberProducer(messageBuffer));
        Thread consumer = new Thread(new NumberConsumer(messageBuffer));

        producer.start();
        consumer.start();
    }
}
