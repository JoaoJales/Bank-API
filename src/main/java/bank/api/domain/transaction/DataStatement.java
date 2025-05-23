package bank.api.domain.transaction;

import bank.api.domain.account.Account;

import java.time.LocalDateTime;

public record DataStatement(String value, LocalDateTime date, TypeTransaction typeTransaction, String description) {
    public DataStatement(Transaction transaction, Account account){
        this((account.equals(transaction.getDestinyAccount()) ? "+R$ " : "-R$ ") + transaction.getValue().toString(),
                transaction.getDate(),
                transaction.getTypeTransaction(),
                transaction.getDescription() != null ? transaction.getDescription() : null
                );


    }
}
