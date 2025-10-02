package bank.api.domain.transaction.validations.pix;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.transactions.DataPix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaPix implements ValidatorPixService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void validate(DataPix data, String keyOrigin) {
        var customerDestiny = customerRepository.findByCpfOrEmail(data.key(), data.key()).get();
        var destinyAccount = accountRepository.findByCustomerIdAndTipo(customerDestiny.getId(), TypeAccount.CORRENTE).get();

        if (!accountRepository.existsByNumeroAndAtivoTrue(destinyAccount.getNumero())){
            throw new IllegalArgumentException("Conta de destino inativa!");
        }
    }
}
