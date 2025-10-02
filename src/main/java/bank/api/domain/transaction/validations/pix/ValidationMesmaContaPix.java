package bank.api.domain.transaction.validations.pix;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.transactions.DataPix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationMesmaContaPix implements ValidatorPixService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void validate(DataPix data, String keyOrigin) {
        var customerDestino = customerRepository.findByCpfOrEmail(data.key(), data.key()).get();
        var customerOrigem = customerRepository.findByCpfOrEmail(keyOrigin, keyOrigin).get();

        var contaDestino = accountRepository.findByCustomerIdAndTipo(customerDestino.getId(), TypeAccount.CORRENTE).get();
        var contaOrigem = accountRepository.findByCustomerIdAndTipo(customerOrigem.getId(), TypeAccount.CORRENTE).get();


        if (contaOrigem.getNumero().equalsIgnoreCase(contaDestino.getNumero())){
            throw new IllegalArgumentException("Não é possível realizar um pix para a mesma conta.");
        }
    }
}
