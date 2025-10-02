package bank.api.domain.account;

import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.account.DataCreateAccount;
import bank.api.infra.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SecurityService securityService;


    @Transactional
    public Account createNewAccount(DataCreateAccount data){
        var cpfLogged = securityService.getCpfUserLogged();
        var customer = customerRepository.findByCpf(cpfLogged).get();
        if (data.tipo() == null){
            throw new IllegalArgumentException("É necessário passar um tipo de conta!");
        }

        if (accountRepository.existsByCustomerIdAndTipo(customer.getId(), data.tipo())){
            throw new IllegalArgumentException(customer.getNome() + " (" + customer.getCpf() + ")" + " já possui uma conta do tipo " + data.tipo());
        }

        if (accountRepository.existsByNumero(data.numero())){
            throw new IllegalArgumentException("Conta ("+ data.numero() +")" + " já existe!");
        }

        var account = new Account(customer, data);
        customer.addAccount(account);

        accountRepository.save(account);

        return account;
    }


    @Transactional
    public void cancelAccount(String numeroConta) {
        var cpfLogged = securityService.getCpfUserLogged();
        var customer = customerRepository.findByCpf(cpfLogged).get();
        if (!accountRepository.existsByNumeroAndCustomerId(numeroConta, customer.getId())){
            throw new IllegalArgumentException("Conta não pertence ao usuário " + customer.getNome() + " (" + customer.getCpf() + ")");
        }

        var account = accountRepository.getReferenceByNumero(numeroConta);
        account.cancelAccount();

        accountRepository.save(account);
    }
}
