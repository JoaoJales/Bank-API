package bank.api.domain.account;

import bank.api.domain.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Account createNewAccount(DataCreateAccount data, Long id){
        if (data.tipo() == null){
            throw new IllegalArgumentException("É necessário passar um tipo de conta!");
        }

        if (accountRepository.existsByCustomerIdAndTipo(id, data.tipo())){
            throw new IllegalArgumentException("O Cliente já possui este tipo de conta");
        }

        if (accountRepository.existsByNumero(data.numero())){
            throw new IllegalArgumentException("Número da conta já existe!");
        }


        var customer = customerRepository.getReferenceById(id);
        var account = new Account(customer, data);
        customer.addAccount(account);

        accountRepository.save(account);

        return account;

    }


    @Transactional
    public void cancelAccount(Long id, String numeroConta) {
        if (!accountRepository.existsByNumeroAndCustomerId(numeroConta, id)){
            throw new IllegalArgumentException("Conta não pertence ao usuário indicado!");
        }

        var account = accountRepository.getReferenceByNumero(numeroConta);
        account.cancelAccount();

        accountRepository.save(account);
    }
}
