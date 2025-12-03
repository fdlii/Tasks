package com.task_9_1;

public class Main {
    static MyThread thread1 = new MyThread();
    static MyThread thread2 = new MyThread();
    static Thread thread3 = new Thread(() -> {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });
    static Thread thread4 = new Thread(() -> {
        int count = 0;
        for (int i = 0; i < 1000000000; i++){
            count++;
        }
    });
    public static void main(String[] args) throws InterruptedException {

        printState(thread1);

        thread2.start();
        Thread.sleep(10);
        thread1.start();
        printState(thread1);

        Thread.sleep(10);
        printState(thread1);

        Thread.sleep(2000);
        printState(thread1);

        thread3.start();

        Thread.sleep(3000);
        printState(thread1);

        Thread.sleep(2000);
        printState(thread1);
    }

    public static class MyThread extends Thread {

        @Override
        public void run() {
            try {
                syncMethod();
                thread3.join();
                thread4.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized void syncMethod() throws InterruptedException {
        Thread.sleep(2000);
    }

    public static void printState(MyThread thread){
        System.out.println("Состояние потока: " + thread.getState());
    }
}
