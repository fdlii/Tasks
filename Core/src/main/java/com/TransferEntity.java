package com;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_account_id")
    AccountEntity withdrawalAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_account_id")
    AccountEntity transferAccount;

    double summ;

    @Enumerated(EnumType.ORDINAL)
    TransferStatus status;

    public TransferEntity(AccountEntity withdrawalAccount, AccountEntity transferAccount, double summ) {
        this.withdrawalAccount = withdrawalAccount;
        this.transferAccount = transferAccount;
        this.summ = summ;
    }
}
