package com;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class Producer {
    Logger logger = LoggerFactory.getLogger(Producer.class);
    private KafkaTemplate<String, TransferEntity> kafkaTemplate;
    private AccountRepository accountRepository;
    private Random random = new Random();
    private List<AccountEntity> entities;

    // Конструктор только для инъекции (без логики!)
    public Producer(AccountRepository accountRepository,
                    KafkaTemplate<String, TransferEntity> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        entities = accountRepository.findAll();

        if (entities.isEmpty()) {
            List<AccountEntity> newAccounts = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                double money = random.nextDouble() * 10000;
                newAccounts.add(new AccountEntity(money));
            }
            accountRepository.saveAll(newAccounts);
            entities = accountRepository.findAll();
        }
    }

    @Scheduled(fixedDelay = 2000)  // потом на 200
    public void generateTransfer() {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        AccountEntity withdrawalAccount = entities.get(random.nextInt(entities.size()));
        AccountEntity transferAccount = withdrawalAccount;
        while (withdrawalAccount == transferAccount) {
            transferAccount = entities.get(random.nextInt(entities.size()));
        }

        double sum = random.nextDouble() * 1000;
        TransferEntity transfer = new TransferEntity(withdrawalAccount, transferAccount, sum);

        String key = transfer.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);
        logger.info("Сообщение отправляется в брокер.");

        kafkaTemplate.executeInTransaction(ops -> {
            ops.send("transfer-created-events-topic", key, transfer);
            return null;
        });
    }
}
