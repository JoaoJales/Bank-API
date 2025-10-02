package bank.api.domain.transaction;

import bank.api.domain.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record DataStatement(
        @Schema(example = "+R$ 100.00", description = "valor")
        String value,

        @Schema(example = "2025-07-02T14:44:26", description = "Data")
        LocalDateTime date,

        @Schema(example = "DEPOSITO", description = "tipo de transação")
        TypeTransaction typeTransaction,

        @Schema(example = "deposito em dinheiro", description = "descrição")
        String description) {
    public DataStatement(Transaction transaction, Account account){
        this((account.equals(transaction.getDestinyAccount()) ? "+R$ " : "-R$ ") + transaction.getValue().toString(),
                transaction.getDate(),
                transaction.getTypeTransaction(),
                transaction.getDescription() != null ? transaction.getDescription() : null
                );


    }
}
