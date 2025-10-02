package bank.api.domain.transaction.validations.deposit;

import bank.api.dto.transactions.DataDeposit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidationValorMinimoDeposit implements ValidatorDepositService {
    @Override
    public void validate(DataDeposit data) {
        if (data.value().compareTo(BigDecimal.ONE) < 0){
            throw new IllegalArgumentException("R$1,00 é o mínimo para depósito!");
        }
    }
}
