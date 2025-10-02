package bank.api.dto.customer;

import bank.api.domain.customer.Customer;
import bank.api.dto.account.DataGetAccount;
import bank.api.domain.address.Address;

import java.time.LocalDate;
import java.util.List;

public record DataDetailingCustomer(Long id, String nome, String cpf, String email, String telefone, LocalDate dt_nascimento, Address address, List<DataGetAccount> accounts) {
    public DataDetailingCustomer(Customer customer){
        this(customer.getId(), customer.getNome(), customer.getCpf(), customer.getEmail(), customer.getTelefone(), customer.getDt_nascimento(), customer.getAddress(),
                customer.getAccounts().stream().map(DataGetAccount::new).toList());
    }

}
