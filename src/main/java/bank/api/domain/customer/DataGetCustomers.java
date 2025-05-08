package bank.api.domain.customer;

import bank.api.domain.account.DataGetAccount;
import bank.api.domain.address.Address;

import java.time.LocalDate;
import java.util.List;

public record DataGetCustomers(Long id, String nome, String cpf, String email, String telefone, LocalDate dt_nascimento, Address endereco, List<DataGetAccount> accounts) {
    public DataGetCustomers(Customer customer){
        this(customer.getId(), customer.getNome(), customer.getCpf(), customer.getEmail(), customer.getTelefone(), customer.getDt_nascimento(), customer.getAddress(),
                customer.getAccounts().stream().map(DataGetAccount::new).toList());
    }
}
