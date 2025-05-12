package bank.api.domain.transaction.validations.deposit;

import bank.api.domain.transaction.dtosTransactions.DataDeposit;
import bank.api.domain.transaction.dtosTransactions.DataTransfer;
import bank.api.domain.transaction.validations.transfer.ValidatorTransferService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidationHorarioFuncionamentoDeposit implements ValidatorDepositService {
    @Override
    public void validate(DataDeposit data) {
        var dateNow = LocalDateTime.now();
        var dateStart = dateNow.getHour() < 7;
        var dateFinal = dateNow.getHour() > 17;
        var sunday = dateNow.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var saturday = dateNow.getDayOfWeek().equals(DayOfWeek.SATURDAY);

        if (dateStart || dateFinal || sunday || saturday){
            throw new IllegalArgumentException("Não é possível realizar uma transferência fora do hórario útil");
        }
    }
}
