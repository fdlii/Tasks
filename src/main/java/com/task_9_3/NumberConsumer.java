package com.task_9_3;

public class NumberConsumer implements Runnable {
    MessageBuffer messageBuffer;
    public NumberConsumer(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Извлеченное число из буфера: " + messageBuffer.consume());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
