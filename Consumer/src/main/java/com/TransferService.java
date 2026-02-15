package com;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransferService {
    Logger logger = LoggerFactory.getLogger(TransferService.class);
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    AccountRepository accountRepository;

    public void saveTransfer(TransferEntity entity) {
        Optional<AccountEntity> withdrawalAccount = accountRepository.findById(entity.getWithdrawalAccount().getId());
        Optional<AccountEntity> transferAccount = accountRepository.findById(entity.getTransferAccount().getId());
        try {
            if (withdrawalAccount.isEmpty() || transferAccount.isEmpty()) {
                throw new EntityNotFoundException("Счёт не найден.");
            }
            double remains = withdrawalAccount.get().getMoney() - entity.getSumm();
            if (remains < 0) {
                throw new ArithmeticException("Средств на счёте недостаточно.");
            }

            transferAccount.get().setMoney(withdrawalAccount.get().getMoney() + entity.getSumm());
            withdrawalAccount.get().setMoney(remains);
            entity.setStatus(TransferStatus.COMPLETED);

            accountRepository.save(withdrawalAccount.get());
            accountRepository.save(transferAccount.get());
            transferRepository.save(entity);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            entity.setStatus(TransferStatus.ERROR);
            transferRepository.save(entity);
        }
    }
}
