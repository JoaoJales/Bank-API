package bank.api.domain.transaction.validations.withdrawal;

import bank.api.domain.transaction.dtosTransactions.DataWithdrawal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidationValorMaximoWithdrawal implements ValidatorWithdrawalService{
    @Override
    public void validate(DataWithdrawal data) {
        if (data.value().compareTo(BigDecimal.valueOf(2000)) > 0){
            throw new IllegalArgumentException("O valor máximo permitido por saque é de R$2.000,00 ");
        }
    }
}
