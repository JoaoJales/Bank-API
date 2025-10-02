package bank.api.domain.user;

import bank.api.dto.user.DataPutPassword;
import bank.api.infra.security.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve retornar um User caso informações de alteração de senha estejam corretas")
    void alterPassword_case1() {
        User user = getDefaultUser();
        String senhaAntiga = "11111";
        String senhaNova = "22222";
        String cpfLogged = "123.456.789-00";

        when(securityService.getCpfUserLogged()).thenReturn(user.getCpf());
        when(userRepository.getUserByCpf(cpfLogged)).thenReturn(user);
        when(passwordEncoder.matches(senhaAntiga, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(senhaNova)).thenReturn("senhaNovaEncode");

        var dataPutPassword = new DataPutPassword(senhaAntiga, senhaNova);
        var result = userService.alterPassword(dataPutPassword);

        assertNotNull(result);
        assertEquals("senhaNovaEncode", result.getPassword());
        assertEquals(user.getCpf(), result.getCpf());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso a nova senha seja igual a senha antiga")
    void alterPassword_case2() {
        String senhaAntiga = "11111";
        String senhaNova = "11111";
        String cpfLogged = "123.456.789-00";

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        var dataPutPassword = new DataPutPassword(senhaAntiga, senhaNova);

        var thrown = assertThrows(IllegalArgumentException.class, () -> userService.alterPassword(dataPutPassword));

        assertEquals("A nova senha deve ser diferente da senha atual", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso a senha antiga não seja igual a senha atual")
    void alterPassword_case3() {
        User user = getDefaultUser();
        String senhaAntiga = "22222";
        String senhaNova = "33333";
        String cpfLogged = "123.456.789-00";

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(userRepository.getUserByCpf(cpfLogged)).thenReturn(user);
        when(passwordEncoder.matches(senhaAntiga, user.getPassword())).thenReturn(false);
        var dataPutPassword = new DataPutPassword(senhaAntiga, senhaNova);

        var thrown = assertThrows(ResponseStatusException.class, () -> userService.alterPassword(dataPutPassword));

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Senha atual incorreta", thrown.getReason());
    }

    private User getDefaultUser(){
        return new User(1L, "123.456.789-00", "11111");
    }
}