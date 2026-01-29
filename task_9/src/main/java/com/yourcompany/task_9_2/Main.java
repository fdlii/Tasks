package com.yourcompany.task_9_2;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        HelloThread thread1 = new HelloThread("Поток 1");
        HelloThread thread2 = new HelloThread("Поток 2");
        thread1.start();
        Thread.sleep(250);
        thread2.start();
    }

    public static synchronized void printName(HelloThread thread) {
        System.out.println("Имя потока: " + thread.name);
    }

    public static class HelloThread extends Thread {
        public String name;

        public HelloThread(String name) {
            super();
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    printName(this);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
