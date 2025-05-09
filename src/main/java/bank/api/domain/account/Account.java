package bank.api.domain.account;

import bank.api.domain.customer.Customer;
import bank.api.domain.transaction.Transaction;
import bank.api.domain.transaction.TypeTransaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "originAccount", cascade = CascadeType.ALL)
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinyAccount", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions = new ArrayList<>();

    private String numero;
    private BigDecimal saldo;
    private boolean ativo;

    public Account(Customer customer, String numero) {
        this.ativo = true;
        this.customer = customer;
        this.numero = numero;
        this.saldo = BigDecimal.valueOf(0.00);
    }

    public void putAccountReceivedTransaction(Transaction transaction){
        this.saldo = this.saldo.add(transaction.getValue());
        receivedTransactions.add(transaction);
    }

    public void putAccountsentTransaction(Transaction transaction){
        if (this.saldo.subtract(transaction.getValue()).compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.sentTransactions.add(transaction);
    }
}
