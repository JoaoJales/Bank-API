package bank.api.domain.account;

import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.dto.account.DataCreateAccount;
import bank.api.infra.security.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("Deve retornar um conta nova se todas as informações estiverem validas")
    void createNewAccount_case1() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";
        TypeAccount typeAccount = TypeAccount.POUPANCA;

        var customer = new Customer(1L, "nome ", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerIdAndTipo(1L, typeAccount)).thenReturn(false);
        when(accountRepository.existsByNumero(accountNumber)).thenReturn(false);

        var dataCreateAccount = new DataCreateAccount(accountNumber, typeAccount);

        var result = accountService.createNewAccount(dataCreateAccount);

        assertNotNull(result);
        assertEquals(accountNumber, result.getNumero());
        assertEquals(customer, result.getCustomer());
        assertEquals(typeAccount, result.getTipo());
        assertEquals(BigDecimal.valueOf(0.0), result.getSaldo());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso não seja passado um tipo de conta")
    void createNewAccount_case2() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";


        var customer = new Customer(1L, "nome ", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));

        var dataCreateAccount = new DataCreateAccount(accountNumber, null);

        var thrown = assertThrows(IllegalArgumentException.class, ()-> accountService.createNewAccount(dataCreateAccount));

        assertEquals("É necessário passar um tipo de conta!", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o cliente já possua uma conta desse tipo")
    void createNewAccount_case3() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";
        TypeAccount typeAccount = TypeAccount.POUPANCA;

        var customer = new Customer(1L, "nome ", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerIdAndTipo(1L, typeAccount)).thenReturn(true);

        var dataCreateAccount = new DataCreateAccount(accountNumber, typeAccount);

        var thrown = assertThrows(IllegalArgumentException.class, ()-> accountService.createNewAccount(dataCreateAccount));

        assertEquals(customer.getNome() + " (" + customer.getCpf() + ")" + " já possui uma conta do tipo " + typeAccount, thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o cliente já possua uma conta com esse número")
    void createNewAccount_case4() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";
        TypeAccount typeAccount = TypeAccount.POUPANCA;

        var customer = new Customer(1L, "nome", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerIdAndTipo(1L, typeAccount)).thenReturn(false);
        when(accountRepository.existsByNumero(accountNumber)).thenReturn(true);

        var dataCreateAccount = new DataCreateAccount(accountNumber, typeAccount);

        var thrown = assertThrows(IllegalArgumentException.class, ()-> accountService.createNewAccount(dataCreateAccount));

        assertEquals("Conta ("+ accountNumber +")" + " já existe!", thrown.getMessage());
    }


    @Test
    @DisplayName("Deve cancelar uma conta com sucesso")
    void cancelAccount_case1() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";

        var customer = new Customer(1L, "nome", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());
        var account = mock(Account.class);
        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.getReferenceByNumero(accountNumber)).thenReturn(account);
        when(accountRepository.existsByNumeroAndCustomerId(accountNumber, 1L)).thenReturn(true);

        accountService.cancelAccount(accountNumber);

        verify(account, times(1)).cancelAccount();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se conta não pertencer ao cliente logado")
    void cancelAccount_case2() {
        String cpfLogged = "123.456.789-00";
        String accountNumber = "00001-1";

        var customer = new Customer(1L, "nome", "email@gmail.com", "(00)00000-0000", cpfLogged, null, null, new ArrayList<>());

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByNumeroAndCustomerId(accountNumber, 1L)).thenReturn(false);


        var thrown = assertThrows(IllegalArgumentException.class, () -> accountService.cancelAccount(accountNumber));

        assertEquals("Conta não pertence ao usuário " + customer.getNome() + " (" + customer.getCpf() + ")", thrown.getMessage());
    }
}