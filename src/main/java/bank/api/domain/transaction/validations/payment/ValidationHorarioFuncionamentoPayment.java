package bank.api.domain.transaction.validations.payment;

import bank.api.dto.transactions.DataPayment;
import org.springframework.stereotype.Component;

@Component
public class ValidationHorarioFuncionamentoPayment implements ValidatorPaymentService {
    @Override
    public void validate(DataPayment data) {
        //TODO: Desativado temporariamente para facilitar os testes fora do horário bancário.
//        var dateNow = LocalDateTime.now();
//        var dateStart = dateNow.getHour() > 20;
//        var dateFinal = dateNow.getHour() < 7;
//
//        if (dateStart || dateFinal){
//            throw new IllegalArgumentException("Pagamento fora do hárario de funcionamento!");
//        }
    }
}
