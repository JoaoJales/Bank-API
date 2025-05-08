package bank.api.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCpfOrEmail(String cpf, String email);
}
