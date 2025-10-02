package bank.api.domain.transaction.validations.withdrawal;

import bank.api.dto.transactions.DataWithdrawal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidationValorMinimoWithdrawal implements ValidatorWithdrawalService {
    @Override
    public void validate(DataWithdrawal data) {
        if (data.value().compareTo(BigDecimal.valueOf(20)) < 0){
            throw new IllegalArgumentException("R$20,00 é o mínimo para realizar o saque!");
        }
    }
}
