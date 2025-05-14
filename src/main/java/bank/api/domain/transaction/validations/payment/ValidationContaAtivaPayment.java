package bank.api.domain.transaction.validations.payment;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.DataPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaPayment implements ValidatorPaymentService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void validate(DataPayment data, String cpf) {
        var customer = customerRepository.findByCpfOrEmail(cpf, cpf).get();
        var originAccount = accountRepository.findByCustomerIdAndTipo(customer.getId(), TypeAccount.CORRENTE).get();

        if (!accountRepository.existsByNumeroAndAtivoTrue(originAccount.getNumero())){
            throw new IllegalArgumentException("Conta de origem inativa!");
        }
    }
}
