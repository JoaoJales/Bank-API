package bank.api.domain.transaction.validations.payment;

import bank.api.domain.transaction.dtosTransactions.DataPayment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidationHorarioFuncionamentoPayment implements ValidatorPaymentService {
    @Override
    public void validate(DataPayment data) {
        var dateNow = LocalDateTime.now();
        var dateStart = dateNow.getHour() > 20;
        var dateFinal = dateNow.getHour() < 7;

        if (dateStart || dateFinal){
            throw new IllegalArgumentException("Pagamento fora do hárario de funcionamento!");
        }
    }
}
