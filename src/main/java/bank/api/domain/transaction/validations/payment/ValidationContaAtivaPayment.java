package bank.api.domain.transaction.validations.payment;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.transaction.dtosTransactions.DataPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaPayment implements ValidatorPaymentService {
    @Autowired
    private AccountRepository repository;

    @Override
    public void validate(DataPayment data) {
        if (!repository.existsByNumeroAndAtivoTrue(data.originAccount())){
            throw new IllegalArgumentException("Conta de origem inativa!");
        }
    }
}
