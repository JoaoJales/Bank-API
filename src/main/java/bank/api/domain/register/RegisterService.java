package bank.api.domain.register;

import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.customer.DataDetailingCustomer;
import bank.api.domain.user.User;
import bank.api.domain.user.UserRepository;
import bank.api.dto.register.DataRegister;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class RegisterService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public DataDetailingCustomer register (DataRegister dataRegister){
        if (customerRepository.existsByCpfOrEmail(dataRegister.customer().cpf(), dataRegister.customer().email())){
            throw new IllegalArgumentException("CPF ou Email ja cadastrado");
        }

        if (accountRepository.existsByNumeroAndAtivoTrue(dataRegister.account().numero())){
            throw new IllegalArgumentException("Número da conta já existe!");
        }

        verifymajority(dataRegister.customer().dt_nascimento());

        var customer = new Customer(dataRegister.customer());
        var user = new User(null ,dataRegister.customer().cpf(), passwordEncoder.encode(dataRegister.user().senha()));

        customer = customerRepository.save(customer);

        var account = new Account(null, customer, null, null, dataRegister.account().numero(), BigDecimal.valueOf(0.0), TypeAccount.CORRENTE, true);
        accountRepository.save(account);

        customer.addAccount(account);


        userRepository.save(user);
        customerRepository.save(customer);

        return new DataDetailingCustomer(customer);
    }

    private void verifymajority(LocalDate dt_nascimento) {
        if(Period.between(dt_nascimento, LocalDate.now()).getYears() < 18){
            throw new IllegalArgumentException("Proibido para menores de 18 anos");
        }
    }
}
