package bank.api.domain.transaction.validations.transfer;


import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.transactions.DataTransfer;
import bank.api.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationMesmaContaTransfer implements ValidatorTransferService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    public void validate(DataTransfer data) {
        Account contaOrigem;
        var contaDestino = accountRepository.getReferenceByNumero(data.destinyAccount());
        var customerDestino = customerRepository.getReferenceById(contaDestino.getCustomer().getId());

        if (data.originAccount() == null){
            var cpf = securityService.getCpfUserLogged();
            var customerOrigem = customerRepository.findByCpf(cpf).get();
            contaOrigem = accountRepository.findByCustomerIdAndTipo(customerOrigem.getId(), TypeAccount.CORRENTE).get();
        }else {
            contaOrigem = accountRepository.getReferenceByNumero(data.originAccount());
        }

        if (contaOrigem.getNumero().equalsIgnoreCase(contaDestino.getNumero())){
            throw new IllegalArgumentException("Não é possível realizar uma transferência para a mesma conta.");
        }
    }
}
