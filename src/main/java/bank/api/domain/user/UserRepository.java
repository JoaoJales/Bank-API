package bank.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.cpf ILIKE :cpf")
    User getUserByCpf(String cpf);
}
