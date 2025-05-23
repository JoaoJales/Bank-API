package bank.api.domain.register;

import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.address.DataAddress;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.customer.DataRegisterCustomer;
import bank.api.domain.user.DataRegisterUser;
import bank.api.domain.user.User;
import bank.api.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService registerService;

    @Test
    @DisplayName("Deve registrar e retornar um dto com dados do cliente caso informarções estiverem corretas")
    void register_case1() {
        var dataRegisterCustomer = getDefaultDataRegisterCustomer();
        var dataRegisterUser = new DataRegisterUser("12345678");
        var dataCreateAccount = new DataCreateAccount("1234567-8", TypeAccount.CORRENTE);


        var dataRegister = new DataRegister(dataRegisterUser, dataRegisterCustomer, dataCreateAccount);
        var customer = new Customer(dataRegisterCustomer);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.existsByCpfOrEmail(customer.getCpf(), customer.getEmail())).thenReturn(false);
        when(accountRepository.existsByNumeroAndAtivoTrue(dataCreateAccount.numero())).thenReturn(false);


        var result = registerService.register(dataRegister);

        assertNotNull(result);
        assertEquals(customer.getNome(), result.nome());
        assertEquals(customer.getCpf(), result.cpf());
        assertEquals(customer.getEmail(), result.email());
        assertEquals(customer.getDt_nascimento(), result.dt_nascimento());
        assertEquals(customer.getTelefone(), result.telefone());

        assertEquals(customer.getAddress().getCep(), result.address().getCep());
        assertEquals(customer.getAddress().getCidade(), result.address().getCidade());
        assertEquals(customer.getAddress().getUf(), result.address().getUf());
        assertEquals(customer.getAddress().getNumero(), result.address().getNumero());


        assertEquals(dataCreateAccount.numero(), result.accounts().getFirst().numero());
        assertEquals(dataCreateAccount.tipo(), result.accounts().getFirst().tipo());


        verify(accountRepository, times(1)).save(any(Account.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(customerRepository, times(2)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o cpf ou email já tenha sido cadastrado")
    void register_case2() {
        var dataRegisterCustomer = getDefaultDataRegisterCustomer();
        var dataRegisterUser = new DataRegisterUser("12345678");
        var dataCreateAccount = new DataCreateAccount("1234567-8", TypeAccount.CORRENTE);


        var dataRegister = new DataRegister(dataRegisterUser, dataRegisterCustomer, dataCreateAccount);
        var customer = new Customer(dataRegisterCustomer);

        when(customerRepository.existsByCpfOrEmail(customer.getCpf(), customer.getEmail())).thenReturn(true);

        var thrown = assertThrows(IllegalArgumentException.class, () -> registerService.register(dataRegister));

        assertEquals("CPF ou Email ja cadastrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o número da conta já exista")
    void register_case3() {
        var dataRegisterCustomer = getDefaultDataRegisterCustomer();
        var dataRegisterUser = new DataRegisterUser("12345678");
        var dataCreateAccount = new DataCreateAccount("1234567-8", TypeAccount.CORRENTE);


        var dataRegister = new DataRegister(dataRegisterUser, dataRegisterCustomer, dataCreateAccount);
        var customer = new Customer(dataRegisterCustomer);

        when(customerRepository.existsByCpfOrEmail(customer.getCpf(), customer.getEmail())).thenReturn(false);
        when(accountRepository.existsByNumeroAndAtivoTrue(dataCreateAccount.numero())).thenReturn(true);

        var thrown = assertThrows(IllegalArgumentException.class, () -> registerService.register(dataRegister));

        assertEquals("Número da conta já existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o cliente seja menor de idade")
    void register_case4() {
        var dataRegisterCustomer = new DataRegisterCustomer("fulano", "fff@gmail.com", "000.000.000-00", "(00)00000-0000", LocalDate.now(), getDefaultDataAddress());
        var dataRegisterUser = new DataRegisterUser("12345678");
        var dataCreateAccount = new DataCreateAccount("1234567-8", TypeAccount.CORRENTE);


        var dataRegister = new DataRegister(dataRegisterUser, dataRegisterCustomer, dataCreateAccount);
        var customer = new Customer(dataRegisterCustomer);

        when(customerRepository.existsByCpfOrEmail(customer.getCpf(), customer.getEmail())).thenReturn(false);
        when(accountRepository.existsByNumeroAndAtivoTrue(dataCreateAccount.numero())).thenReturn(false);

        var thrown = assertThrows(IllegalArgumentException.class, () -> registerService.register(dataRegister));

        assertEquals("Proibido para menores de 18 anos", thrown.getMessage());
    }

    private DataAddress getDefaultDataAddress() {
        return new DataAddress("rua xpto", "Asa Sul", "43123980", "Brasilia", "DF", null, null);
    }

    private DataRegisterCustomer getDefaultDataRegisterCustomer() {
        return new DataRegisterCustomer("fulano", "fff@gmail.com", "000.000.000-00", "(00)00000-0000", LocalDate.of(2005, 9, 24), getDefaultDataAddress());
    }
}