package com.pandurang.notifications;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NotificationServiceApplication::main)
                .with(ContainerConfig.class)
                .run(args);
    }
}
