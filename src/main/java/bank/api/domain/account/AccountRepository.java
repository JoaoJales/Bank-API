package bank.api.domain.account;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNumero(String numero);

    Account getReferenceByNumero(String numero);

    boolean existsByNumeroAndCustomerId(String numeroConta, Long id);

    boolean existsByNumeroAndAtivoTrue(String numero);

    Optional<Account> findByCustomerIdAndTipo(Long id, TypeAccount tipo);

    boolean existsByCustomerIdAndTipo(Long id, TypeAccount tipo);
}
