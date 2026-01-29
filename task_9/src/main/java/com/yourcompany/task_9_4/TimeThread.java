package com.yourcompany.task_9_4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeThread extends Thread {
    private int intervalSeconds;

    public TimeThread(int intervalSeconds) {
        super();
        this.intervalSeconds = intervalSeconds;
    }

    @Override
    public void run() {
        LocalDateTime time;
        while (true) {
            time = LocalDateTime.now();
            System.out.println("Текущее системное время: " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            try {
                Thread.sleep(1000L * intervalSeconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
