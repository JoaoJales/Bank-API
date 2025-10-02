package bank.api.domain.transaction.validations.withdrawal;

import bank.api.dto.transactions.DataWithdrawal;
import org.springframework.stereotype.Component;

@Component
public class ValidationHorarioFuncionamentoWithdrawal implements ValidatorWithdrawalService {
    @Override
    public void validate(DataWithdrawal data) {
        //TODO: Desativado temporariamente para facilitar os testes fora do horário bancário.
//        var dateNow = LocalDateTime.now();
//        var dateStart = dateNow.getHour() < 6;
//        var dateFinal = dateNow.getHour() > 22;
//
//        if (dateStart || dateFinal){
//            throw new IllegalArgumentException("Saque fora do hárario de funcionamento!");
//        }
    }
}
