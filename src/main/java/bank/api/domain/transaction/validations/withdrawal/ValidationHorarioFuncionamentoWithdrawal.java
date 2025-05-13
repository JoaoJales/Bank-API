package bank.api.domain.transaction.validations.withdrawal;

import bank.api.domain.transaction.dtosTransactions.DataWithdrawal;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidationHorarioFuncionamentoWithdrawal implements ValidatorWithdrawalService {
    @Override
    public void validate(DataWithdrawal data) {
        var dateNow = LocalDateTime.now();
        var dateStart = dateNow.getHour() < 6;
        var dateFinal = dateNow.getHour() > 22;

        if (dateStart || dateFinal){
            throw new IllegalArgumentException("Saque fora do hárario de funcionamento!");
        }
    }
}
