package bank.api.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCpfOrEmail(String cpf, String email);

    Optional<Customer> findByCpfOrEmail(String cpf, String email);

    Optional<Customer> findByCpf(String cpfLogged);
}
