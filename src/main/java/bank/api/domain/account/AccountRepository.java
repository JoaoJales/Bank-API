package bank.api.domain.account;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNumero(String numero);

    Account getReferenceByNumero(String numero);
}
