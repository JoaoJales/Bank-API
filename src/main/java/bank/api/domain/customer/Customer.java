package bank.api.domain.customer;

import bank.api.domain.account.Account;
import bank.api.domain.address.Address;
import bank.api.dto.customer.DataPutCustomer;
import bank.api.dto.customer.DataRegisterCustomer;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dt_nascimento;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public Customer(DataRegisterCustomer data){
        this.nome = data.nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.cpf = data.cpf();
        this.dt_nascimento = data.dt_nascimento();
        this.address = new Address(data.address());
    }


    public void updateInfo(@Valid DataPutCustomer data) {
        if (data.nome() != null) this.nome = data.nome();
        if (data.email() != null) this.email = data.email();
        if (data.telefone() != null) this.telefone = data.telefone();
        if (data.endereco() != null){
            this.address.updateInfoAddress(data.endereco());
        }
    }

    public void addAccount(Account account){
        accounts.add(account);
        account.setCustomer(this);
    }
}
