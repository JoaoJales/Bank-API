package bank.api.domain.transaction;

import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.*;
import bank.api.domain.transaction.validations.deposit.ValidatorDepositService;
import bank.api.domain.transaction.validations.payment.ValidatorPaymentService;
import bank.api.domain.transaction.validations.pix.ValidatorPixService;
import bank.api.domain.transaction.validations.transfer.ValidatorTransferService;
import bank.api.domain.transaction.validations.withdrawal.ValidatorWithdrawalService;
import bank.api.infra.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SecurityService securityService;


    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Injeta uma lista vazia na lista de validadores para evitar NullPointerException
        transactionService.setValidatorsTransfer(Collections.emptyList());
        transactionService.setValidatorsDeposit(Collections.emptyList());
        transactionService.setValidatorsPayment(Collections.emptyList());
        transactionService.setValidatorsPix(Collections.emptyList());
        transactionService.setValidatorsWithdrawal(Collections.emptyList());
    }

    @Test
    @DisplayName("Deveria retornar uma transferência se informações estiverem corretas")
    void transfer_case1() {
        String cpfLogged = "123.456.789-00";
        String originAccountNumber = "00001-1";
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Transferência teste";

        var validador = mock(ValidatorTransferService.class);
        transactionService.setValidatorsTransfer(List.of(validador));

        var customer = mock(Customer.class);
        when(customer.getCpf()).thenReturn(cpfLogged);

        var originAccount = mock(Account.class);
        when(originAccount.getCustomer()).thenReturn(customer);

        var destinyAccount = mock(Account.class);

        var dataTransfer = new DataTransfer(originAccountNumber, destinyAccountNumber, value, description);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(true);
        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(true);
        when(accountRepository.getReferenceByNumero(originAccountNumber)).thenReturn(originAccount);
        when(accountRepository.getReferenceByNumero(destinyAccountNumber)).thenReturn(destinyAccount);


        Transaction result = transactionService.transfer(dataTransfer);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.TRANSFERENCIA, result.getTypeTransaction());
        assertEquals(originAccount, result.getOriginAccount());
        assertEquals(destinyAccount, result.getDestinyAccount());

        verify(validador, times(1)).validate(dataTransfer);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(originAccount);
        verify(accountRepository, times(1)).save(destinyAccount);
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso conta de origem ou conta de destino não existe")
    void transfer_case2(){
        String cpfLogged = "123.456.789-00";
        String originAccountNumber = "00001-1";
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Transferência teste";

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(false);
        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(false);

        assertAll(
                () -> {
                    var dataTransfer = new DataTransfer(originAccountNumber, null, value, description);
                    var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(dataTransfer));
                    assertEquals("Conta (" + originAccountNumber + ") não existe!", thrown.getMessage());
                },
                () -> {
                    var dataTransfer = new DataTransfer(null, destinyAccountNumber, value, description);
                    var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(dataTransfer));
                    assertEquals("Conta (" + destinyAccountNumber + ") não existe!", thrown.getMessage());
                }
        );

    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o logado não seja responsavel da conta de origem")
    void transfer_case3() {
        String cpfLogged = "123.456.789-00";
        String outroCpf = "111.111.111-11";

        String originAccountNumber = "00001-1";
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Transferência teste";

        var customerDonoDaConta = mock(Customer.class);
        when(customerDonoDaConta.getCpf()).thenReturn(outroCpf);

        var originAccount = mock(Account.class);
        when(originAccount.getCustomer()).thenReturn(customerDonoDaConta);
        when(originAccount.getNumero()).thenReturn(originAccountNumber);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(true);
        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(true);

        when(accountRepository.getReferenceByNumero(originAccountNumber)).thenReturn(originAccount);


        var dataTransfer = new DataTransfer(originAccountNumber, destinyAccountNumber, value, description);

        var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(dataTransfer));

        assertEquals("Você não tem permissão para movimentar a conta (" + originAccountNumber + ")", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve utilizar a conta corrente do cliente caso a conta de origem seja nula")
    void transfer_case4() {
        String cpfLogged = "123.456.789-00";
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Transferência teste";

        var customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);
        var contaCorrente = mock(Account.class);
        var contaDestino = mock(Account.class);



        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(true);
        when(customerRepository.existsByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(true);
        when(accountRepository.getReferenceByNumero(destinyAccountNumber)).thenReturn(contaDestino);
        when(customerRepository.findByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerIdAndTipo(1L, TypeAccount.CORRENTE)).thenReturn(Optional.of(contaCorrente));

        var dataTransfer = new DataTransfer(null, destinyAccountNumber, value, description);
        Transaction result = transactionService.transfer(dataTransfer);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.TRANSFERENCIA, result.getTypeTransaction());
        assertEquals(contaCorrente, result.getOriginAccount());
        assertEquals(contaDestino, result.getDestinyAccount());



        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(contaCorrente);
        verify(accountRepository).save(contaDestino);
    }

    @Test
    @DisplayName("Deveria retornar um depósito se informações estiverem corretas")
    void deposit_case1() {
        String cpf = "123.456.789-00";
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("200.00");
        String description = "Deposito";

        var validador = mock(ValidatorDepositService.class);
        transactionService.setValidatorsDeposit(List.of(validador));

        var destinyAccount = mock(Account.class);
        var dataDeposit = new DataDeposit(destinyAccountNumber, value, description);

        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(true);
        when(accountRepository.getReferenceByNumero(destinyAccountNumber)).thenReturn(destinyAccount);

        Transaction result = transactionService.deposit(dataDeposit);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.DEPOSITO, result.getTypeTransaction());
        assertEquals(destinyAccount, result.getDestinyAccount());

        verify(validador, times(1)).validate(dataDeposit);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(destinyAccount);
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso conta de destino não exista")
    void deposit_case2() {
        String destinyAccountNumber = "00002-2";
        BigDecimal value = new BigDecimal("100.00");
        String description = "depósito teste";

        when(accountRepository.existsByNumero(destinyAccountNumber)).thenReturn(false);

        var dataDeposit = new DataDeposit(destinyAccountNumber, value, description);
        var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.deposit(dataDeposit));

        assertEquals("Conta (" + destinyAccountNumber + ") não existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Deveria retornar um saque (withdrawal) se informações estiverem corretas")
    void withdrawal_case1(){
        String cpf = "123.456.789-00";
        String originAccountNumber = "00001-1";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Saque teste";

        var validador = mock(ValidatorWithdrawalService.class);
        transactionService.setValidatorsWithdrawal(List.of(validador));

        var customer = mock(Customer.class);
        when(customer.getCpf()).thenReturn(cpf);
        var originAccount = mock(Account.class);
        when(originAccount.getCustomer()).thenReturn(customer);

        var dataWithdrawal = new DataWithdrawal(originAccountNumber, value, description);

        when(securityService.getCpfUserLogged()).thenReturn(cpf);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(true);
        when(accountRepository.getReferenceByNumero(originAccountNumber)).thenReturn(originAccount);

        Transaction result = transactionService.withdrawal(dataWithdrawal);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.SAQUE, result.getTypeTransaction());
        assertEquals(originAccount, result.getOriginAccount());


        verify(validador, times(1)).validate(dataWithdrawal);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(originAccount);
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso conta de origem não exista")
    void withdrawal_case2(){
        String cpf = "123.456.789-00";
        String originAccountNumber = "00001-1";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Saque teste";

        when(securityService.getCpfUserLogged()).thenReturn(cpf);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(false);

        var dataWithdrawal = new DataWithdrawal(originAccountNumber, value, description);

        var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.withdrawal(dataWithdrawal));

        assertEquals("Conta (" + originAccountNumber + ") não existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso o logado não seja responsavel da conta de origem")
    void withdrawal_case3(){
        String cpfLogged = "123.456.789-00";
        String outroCpf = "111.111.111-11";

        String originAccountNumber = "00001-1";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Saque teste";

        var customerDonoDaConta = mock(Customer.class);
        when(customerDonoDaConta.getCpf()).thenReturn(outroCpf);

        var originAccount = mock(Account.class);
        when(originAccount.getCustomer()).thenReturn(customerDonoDaConta);
        when(originAccount.getNumero()).thenReturn(originAccountNumber);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(originAccountNumber)).thenReturn(true);
        when(accountRepository.getReferenceByNumero(originAccountNumber)).thenReturn(originAccount);

        var dataWithdrawal = new DataWithdrawal(originAccountNumber, value, description);
        var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.withdrawal(dataWithdrawal));

        assertEquals("Você não tem permissão para movimentar a conta (" + originAccountNumber + ")", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve utilizar a conta corrente do cliente caso a conta de origem seja nula")
    void withdrawal_case4(){
        String cpfLogged = "123.456.789-00";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Saque teste";

        var validador = mock(ValidatorWithdrawalService.class);
        transactionService.setValidatorsWithdrawal(List.of(validador));

        var customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);
        var contaCorrente = mock(Account.class);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.existsByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(true);
        when(customerRepository.findByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerIdAndTipo(1L, TypeAccount.CORRENTE)).thenReturn(Optional.of(contaCorrente));

        var dataWithdrawal = new DataWithdrawal(null, value, description);

        var result = transactionService.withdrawal(dataWithdrawal);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.SAQUE, result.getTypeTransaction());
        assertEquals(contaCorrente, result.getOriginAccount());

        verify(validador, times(1)).validate(dataWithdrawal);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(contaCorrente);
    }

    @Test
    @DisplayName("Deveria retornar um pagamento (Payment) se informações estiverem corretas")
    void payment_case1(){
        String cpfLogged = "123.456.789-00";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Pagamento teste";

        var validador = mock(ValidatorPaymentService.class);
        transactionService.setValidatorsPayment(List.of(validador));

        var customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);

        var contaCorrente = mock(Account.class);

        var dataPayment = new DataPayment(value, description);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.existsByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(true);
        when(customerRepository.findByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerIdAndTipo(1L, TypeAccount.CORRENTE)).thenReturn(Optional.of(contaCorrente));

        Transaction result = transactionService.payment(dataPayment);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.PAGAMENTO, result.getTypeTransaction());
        assertEquals(contaCorrente, result.getOriginAccount());


        verify(validador, times(1)).validate(dataPayment);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(contaCorrente);
    }

    @Test
    @DisplayName("Deveria retornar um pix se informações estiverem corretas")
    void pix_case1(){
        String cpfLogged = "123.456.789-00";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Pix teste";
        String keyDestiny = "111.111.111-11";

        var validador = mock(ValidatorPixService.class);
        transactionService.setValidatorsPix(List.of(validador));

        var customerOrigin = mock(Customer.class);
        when(customerOrigin.getId()).thenReturn(1L);

        var customerDestiny = mock(Customer.class);
        when(customerDestiny.getId()).thenReturn(2L);

        var contaCorrenteOrigin = mock(Account.class);
        var contaCorrenteDestiny = mock(Account.class);

        var dataPix = new DataPix(keyDestiny, value, description);

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.existsByCpfOrEmail(keyDestiny, keyDestiny)).thenReturn(true);
        when(customerRepository.existsByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(true);

        when(customerRepository.findByCpfOrEmail(keyDestiny, keyDestiny)).thenReturn(Optional.of(customerDestiny));
        when(customerRepository.findByCpfOrEmail(cpfLogged, cpfLogged)).thenReturn(Optional.of(customerOrigin));

        when(accountRepository.findByCustomerIdAndTipo(1L, TypeAccount.CORRENTE)).thenReturn(Optional.of(contaCorrenteOrigin));
        when(accountRepository.findByCustomerIdAndTipo(2L, TypeAccount.CORRENTE)).thenReturn(Optional.of(contaCorrenteDestiny));

        Transaction result = transactionService.pix(dataPix);

        assertNotNull(result);
        assertEquals(value, result.getValue());
        assertEquals(description, result.getDescription());
        assertEquals(TypeTransaction.PIX, result.getTypeTransaction());
        assertEquals(contaCorrenteOrigin, result.getOriginAccount());
        assertEquals(contaCorrenteDestiny, result.getDestinyAccount());

        verify(validador, times(1)).validate(dataPix, cpfLogged);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(contaCorrenteOrigin);
        verify(accountRepository, times(1)).save(contaCorrenteDestiny);
    }

    @Test
    @DisplayName("Deve lançar uma exeption caso key de destino não seja valida")
    void pix_case2(){
        String cpfLogged = "123.456.789-00";
        BigDecimal value = new BigDecimal("100.00");
        String description = "Pix teste";
        String keyDestiny = "111.111.111-11";

        var validador = mock(ValidatorPixService.class);
        transactionService.setValidatorsPix(List.of(validador));

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(customerRepository.existsByCpfOrEmail(keyDestiny, keyDestiny)).thenReturn(false);

        var dataPix = new DataPix(keyDestiny, value, description);
        var thrown = assertThrows(IllegalArgumentException.class, () -> transactionService.pix(dataPix));

        assertEquals("Key ("+ keyDestiny + ") inválida!", thrown.getMessage());

    }

    @Test
    @DisplayName("Deve retornar extrato da conta com sucesso")
    void getStatement_case1() {
        String numeroConta = "00001-1";
        String cpfLogged = "123.456.789-00";
        BigDecimal value = new BigDecimal("100.00");

        Pageable pageable = PageRequest.of(0, 10);
        var customer = mock(Customer.class);
        when(customer.getCpf()).thenReturn(cpfLogged);

        var account = mock(Account.class);
        when(account.getCustomer()).thenReturn(customer);

        var transaction = mock(Transaction.class);
        when(transaction.getValue()).thenReturn(value);

        var pageTransactions = new PageImpl<>(List.of(transaction));

        when(securityService.getCpfUserLogged()).thenReturn(cpfLogged);
        when(accountRepository.existsByNumero(numeroConta)).thenReturn(true);

        when(accountRepository.getReferenceByNumero(numeroConta)).thenReturn(account);
        when(transactionRepository.findAllByAccount(account, pageable)).thenReturn(pageTransactions);

        var result = transactionService.getStatement(numeroConta, pageable);

        assertEquals(1, result.getTotalElements());
        assertInstanceOf(DataStatement.class, result.getContent().getFirst());
    }


}