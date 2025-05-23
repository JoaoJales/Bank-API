package bank.api.domain.customer;

import bank.api.infra.security.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Deveria retornar um customer caso as informações de atualização estejam corretas")
    void putCustomers_case1() {
        String cpfLogged = "123.456.789-10";
        Long id = 1L;
        String nome = "joao";
        String email = "joao@gmail.com";
        String telefone = "(61)99999-9999";

        DataPutCustomer dataPutCustomer = new DataPutCustomer(id, nome, email, telefone, null);

        var customer = new Customer(id, "nome antigo", "email.antigo@gmail.com", "(00)00000-0000", cpfLogged, null, null, null);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));

        Customer result = customerService.putCustomers(dataPutCustomer);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(nome, result.getNome());
        assertEquals(email, result.getEmail());
        assertEquals(telefone, result.getTelefone());
        assertEquals(cpfLogged, result.getCpf());
        assertSame(customer, result);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o cliente logado não tenha o mesmo id passado no dto")
    void putCustomers_case2() {
        String cpfLogged = "123.456.789-10";
        Long idLogged = 2L;
        Long outroId = 1L;

        DataPutCustomer dataPutCustomer = new DataPutCustomer(outroId, null, null, null, null);

        var customer = mock(Customer.class);
        when(customer.getId()).thenReturn(idLogged);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.findByCpf(cpfLogged)).thenReturn(Optional.of(customer));

        var thrown = assertThrows(IllegalArgumentException.class, () -> customerService.putCustomers(dataPutCustomer));

        assertEquals("Você não tem permissão de atualizar dados desse usuário", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve retornar clientes com sucesso")
    void getCustomers_case1() {
        Pageable pageable = PageRequest.of(0, 10);
        var customer = mock(Customer.class);
        var pageCustomers = new PageImpl<>(List.of(customer));

        when(customerRepository.findAll(pageable)).thenReturn(pageCustomers);
        var result = customerService.getCustomers(pageable);

        assertEquals(1, result.getTotalElements());
        assertInstanceOf(DataGetCustomers.class, result.getContent().getFirst());
    }

    @Test
    @DisplayName("Deve retonar um cliente com sucesso")
    void getCustomer() {
        Long id = 1L;

        var customer = mock(Customer.class);
        when(customerRepository.getReferenceById(1L)).thenReturn(customer);

        var result = customerService.getCustomer(id);
        assertSame(customer, result);
    }
}