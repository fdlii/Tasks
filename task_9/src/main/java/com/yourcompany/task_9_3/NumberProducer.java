package com.yourcompany.task_9_3;

import java.util.Random;

public class NumberProducer implements Runnable {
    Random random = new Random();
    MessageBuffer messageBuffer;

    public NumberProducer(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    @Override
    public void run() {
        int nextNum;
        while (true) {
            nextNum = random.nextInt(10);
            messageBuffer.produce(nextNum);
            System.out.printf("Число %d поступило в буфер.\n", nextNum);
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
