package bank.api.domain.transaction;

import bank.api.domain.account.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
    SELECT t FROM Transaction t
    WHERE t.originAccount = :account OR t.destinyAccount = :account
    ORDER BY t.date DESC
""")
    Page<Transaction> findAllByAccount(@Param("account") Account account, Pageable pageable);

    List<Transaction> findAllByDestinyAccountAndTypeTransactionAndDateBetween(Account account, TypeTransaction typeTransaction, LocalDateTime dateStart, LocalDateTime dateFinal);

    List<Transaction> findAllByOriginAccountAndTypeTransactionAndDateBetween(Account account, TypeTransaction typeTransaction, LocalDateTime dateStart, LocalDateTime dateFinal);
}
