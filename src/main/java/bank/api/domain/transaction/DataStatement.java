package bank.api.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataStatement(String value, LocalDateTime date, TypeTransaction typeTransaction, String description) {
    public DataStatement(Transaction transaction){
        this("- " transaction.getValue().toString() ,
                transaction.getDate(), transaction.getTypeTransaction(),
                transaction.getDescription() != null ? transaction.getDescription() : null);
    }
}
