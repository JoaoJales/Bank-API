package bank.api.domain.register;

import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.AccountService;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.customer.DataDetailingCustomer;
import bank.api.domain.user.User;
import bank.api.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegisterService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public DataDetailingCustomer register (DataRegister dataRegister){
        if (customerRepository.existsByCpfOrEmail(dataRegister.user().cpf(), dataRegister.customer().email())){
            throw new IllegalArgumentException("CPF ou Email ja cadastrado");
        }


        var customer = new Customer(dataRegister.customer(), dataRegister.user().cpf());
        var user = new User(dataRegister.user());
        customer = customerRepository.save(customer);
        var account = new Account(null, customer, null, null, dataRegister.account().numero(), BigDecimal.valueOf(0.0), TypeAccount.CORRENTE, true);
        accountRepository.save(account);

        customer.addAccount(account);


        userRepository.save(user);
        customerRepository.save(customer);

        return new DataDetailingCustomer(customer);
    }
}
