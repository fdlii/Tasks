package com.yourcompany.task_9_3;

import java.util.ArrayDeque;
import java.util.Queue;

public final class MessageBuffer {
    private Queue<Integer> numbersToConsume;
    private int queueSize;

    public MessageBuffer(int queueSize) {
        this.queueSize = queueSize;
        numbersToConsume = new ArrayDeque<>(queueSize);
    }

    public synchronized void produce(int number) {
        try {
            while(numbersToConsume.size() == queueSize) {
                super.wait();
            }
        }
        catch (InterruptedException ex) {
            throw new RuntimeException();
        }
        numbersToConsume.add(number);
        super.notify();
    }

    public synchronized int consume() {
        try {
            while(numbersToConsume.isEmpty()) {
                super.wait();
            }
        }
        catch (InterruptedException ex) {
            throw new RuntimeException();
        }
        int result =  numbersToConsume.poll();
        super.notify();
        return result;
    }
}
