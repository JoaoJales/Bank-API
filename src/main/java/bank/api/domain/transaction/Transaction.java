package bank.api.domain.transaction;

import bank.api.domain.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccount;

    @ManyToOne
    @JoinColumn(name = "destiny_account_id")
    private Account destinyAccount;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "tipo")
    private TypeTransaction typeTransaction;

    @Column(name = "valor")
    private BigDecimal value;

    @Column(name = "data")
    private LocalDateTime date;
    @Column(name = "descricao")
    private String description;

}
