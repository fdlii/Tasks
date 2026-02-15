package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferHandler {
    @Autowired
    private TransferService transferService;
    Logger logger = LoggerFactory.getLogger(TransferHandler.class);

    @KafkaListener(
            topics = "transfer-created-events-topic",
            groupId = "transfer-created-events",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handle(TransferEntity entity) {
        transferService.saveTransfer(entity);
    }
}
