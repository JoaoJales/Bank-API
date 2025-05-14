package bank.api.domain.transaction.validations.transfer;


import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.DataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationMesmaContaTransfer implements ValidatorTransferService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void validate(DataTransfer data) {
        Account contaOrigem;
        var contaDestino = accountRepository.getReferenceByNumero(data.destinyAccount());
        var customer = customerRepository.getReferenceById(contaDestino.getCustomer().getId());

        if (data.originAccount() == null){
            contaOrigem = accountRepository.findByCustomerIdAndTipo(customer.getId(), TypeAccount.CORRENTE).get();
        }else {
            contaOrigem = accountRepository.getReferenceByNumero(data.originAccount());
        }

        if (contaOrigem.getNumero().equalsIgnoreCase(contaDestino.getNumero())){
            throw new IllegalArgumentException("Não é possível realizar uma transferência para a mesma conta.");
        }
    }
}
