package bank.api.domain.transaction.validations.withdrawal;

import bank.api.dto.transactions.DataWithdrawal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidationValorMultiplo10 implements ValidatorWithdrawalService{
    @Override
    public void validate(DataWithdrawal data) {
        var naoMultiplo = data.value()
                .remainder(BigDecimal.TEN)
                .compareTo(BigDecimal.ZERO) != 0;

        if (naoMultiplo){
            throw new IllegalArgumentException("Só é possível realizar saques com valores múltiplos de 10!");
        }
    }
}
