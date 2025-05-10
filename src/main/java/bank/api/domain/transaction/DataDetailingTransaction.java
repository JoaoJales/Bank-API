package bank.api.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataDetailingTransaction(Long id, String originAccount, String destinyAccount, BigDecimal value, LocalDateTime date, TypeTransaction typeTransaction, String description) {
    public DataDetailingTransaction(Transaction transaction){
        this(transaction.getId(),
                transaction.getOriginAccount() != null ? transaction.getOriginAccount().getNumero() : null,
                transaction.getDestinyAccount() != null ? transaction.getDestinyAccount().getNumero() : null,
                transaction.getValue() , transaction.getDate(), transaction.getTypeTransaction(),
                transaction.getDescription() != null ? transaction.getDescription() : null);
    }
}
