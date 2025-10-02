package bank.api.domain.transaction.validations.deposit;

import bank.api.dto.transactions.DataDeposit;
import org.springframework.stereotype.Component;

@Component
public class ValidationHorarioFuncionamentoDeposit implements ValidatorDepositService {
    @Override
    public void validate(DataDeposit data) {
        //TODO: Desativado temporariamente para facilitar os testes fora do horário bancário.
//        var dateNow = LocalDateTime.now();
//        var dateStart = dateNow.getHour() < 7;
//        var dateFinal = dateNow.getHour() > 17;
//        var sunday = dateNow.getDayOfWeek().equals(DayOfWeek.SUNDAY);
//        var saturday = dateNow.getDayOfWeek().equals(DayOfWeek.SATURDAY);
//
//        if (dateStart || dateFinal || sunday || saturday){
//            throw new IllegalArgumentException("Não é possível realizar um depósito fora do hórario útil");
//        }
    }
}
