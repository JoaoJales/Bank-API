package bank.api.domain.user;

import bank.api.dto.user.DataPutPassword;
import bank.api.infra.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User alterPassword(@Valid DataPutPassword data) {
        var cpfLogged = securityService.getCpfUserLogged();

        if (data.oldPassword().equals(data.newPassword())){
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
        }

        var user = userRepository.getUserByCpf(cpfLogged);
        var isOldPasswordCorrect = passwordEncoder.matches(data.oldPassword(), user.getPassword());

        if (!isOldPasswordCorrect){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }

        user.updatePassword(passwordEncoder.encode(data.newPassword()));
        userRepository.save(user);

        return user;
    }
}
