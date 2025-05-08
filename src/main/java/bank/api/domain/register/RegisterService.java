package bank.api.domain.register;

import bank.api.domain.account.AccountService;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.customer.DataDetailingCustomer;
import bank.api.domain.user.User;
import bank.api.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public DataDetailingCustomer register (DataRegister dataRegister){
        if (customerRepository.existsByCpfOrEmail(dataRegister.user().cpf(), dataRegister.customer().email())){
            throw new IllegalArgumentException("CPF ou Email ja cadastrado");
        }

//        if (dataRegister.account().idCustomer() != null){
//            throw new IllegalArgumentException("Não é necessário indicar o id do cliente");
//        }

        var customer = new Customer(dataRegister.customer(), dataRegister.user().cpf());
        var user = new User(dataRegister.user());
        customer = customerRepository.save(customer);

        accountService.createAccount(dataRegister.account(), customer.getId());


        userRepository.save(user);
        customerRepository.save(customer);

        return new DataDetailingCustomer(customer);
    }
}
