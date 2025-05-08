package bank.api.domain.account;

import bank.api.domain.customer.CustomerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Account createAccount(DataCreateAccount data, Long id){
        if (accountRepository.existsByNumero(data.numero())){
            throw new IllegalArgumentException("Número da conta já existe!");
        }

//        var idCustumer = getIdCustomer(data, id);

        var customer = customerRepository.getReferenceById(id);
        var account = new Account(null, customer, data.numero(), 0.00, true);
        customer.addAccount(account);

        accountRepository.save(account);

        return account;

    }

//    private Long getIdCustomer(DataCreateAccount data, Long id){
//
//        if (id == null && data.idCustomer() == null){
//            throw new IllegalArgumentException("É necessário informar o do Cliente para criar uma nova conta");
//        }
//
//        if (id != null){
//            return id;
//        }
//        return data.idCustomer();
//    }

}
