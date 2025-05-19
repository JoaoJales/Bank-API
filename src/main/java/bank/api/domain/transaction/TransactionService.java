package bank.api.domain.transaction;

import bank.api.domain.account.Account;
import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.*;
import bank.api.domain.transaction.validations.deposit.ValidatorDepositService;
import bank.api.domain.transaction.validations.payment.ValidatorPaymentService;
import bank.api.domain.transaction.validations.pix.ValidatorPixService;
import bank.api.domain.transaction.validations.transfer.ValidatorTransferService;
import bank.api.domain.transaction.validations.withdrawal.ValidatorWithdrawalService;
import bank.api.infra.security.SecurityService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private List<ValidatorTransferService> validatorsTransfer;

    @Autowired
    private List<ValidatorDepositService> validatorsDeposit;

    @Autowired
    private List<ValidatorWithdrawalService> validatorsWithdrawal;

    @Autowired
    private List<ValidatorPaymentService> validatorsPayment;

    @Autowired
    private List<ValidatorPixService> validatorsPix;

    @Transactional
    public Transaction deposit(DataDeposit data){
        validateExistsAccount(data.destinyAccount());

        validatorsDeposit.forEach(d -> d.validate(data));

        var destinyAccount = accountRepository.getReferenceByNumero(data.destinyAccount());
        var transaction = new Transaction(null, null, destinyAccount, TypeTransaction.DEPOSITO, data.value(), LocalDateTime.now(), data.description());
        destinyAccount.addReceivedTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(destinyAccount);

        return transaction;
    }

    @Transactional
    public Transaction transfer(DataTransfer data){
        var cpfLogged = securityService.getCpfUserLogged();
        validateExistsAccount(data.destinyAccount());
        validateExistsAccount(data.originAccount());

        Account originAccount;
        if (data.originAccount() == null){
            originAccount = findCurrentAccount(cpfLogged);
        }else {
            validateOriginAccountAndCpfLogged(cpfLogged, data.originAccount());
            originAccount = accountRepository.getReferenceByNumero(data.originAccount());
        }


        validatorsTransfer.forEach(t -> t.validate(data));

        var destinyAccount = accountRepository.getReferenceByNumero(data.destinyAccount());
        var transaction = new Transaction(null, originAccount, destinyAccount, TypeTransaction.TRANSFERENCIA, data.value(), LocalDateTime.now(), data.description());
        destinyAccount.addReceivedTransaction(transaction);
        originAccount.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(destinyAccount);
        accountRepository.save(originAccount);

        return transaction;
    }

    @Transactional
    public Transaction withdrawal(@Valid DataWithdrawal data) {
        var cpfLogged = securityService.getCpfUserLogged();
        validateExistsAccount(data.originAccount());

        Account originAccount;
        if (data.originAccount() == null){
            originAccount = findCurrentAccount(cpfLogged);
        }else {
            validateOriginAccountAndCpfLogged(cpfLogged, data.originAccount());
            originAccount = accountRepository.getReferenceByNumero(data.originAccount());
        }

        validatorsWithdrawal.forEach(w -> w.validate(data));

        var transaction = new Transaction(null, originAccount, null, TypeTransaction.SAQUE, data.value(), LocalDateTime.now(), data.description());
        originAccount.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(originAccount);

        return transaction;
    }

    @Transactional
    public Transaction payment(DataPayment data){
        var cpfLogged = securityService.getCpfUserLogged();
        validatorsPayment.forEach(p -> p.validate(data));

        var originAccount = findCurrentAccount(cpfLogged);
        var transaction = new Transaction(null, originAccount, null, TypeTransaction.PAGAMENTO, data.value(), LocalDateTime.now(), data.description());
        originAccount.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(originAccount);

        return transaction;
    }

    @Transactional
    public Transaction pix(DataPix data){
        var cpfLogged = securityService.getCpfUserLogged();
        validatorsPix.forEach(p -> p.validate(data, cpfLogged));

        var destinyAccount = findCurrentAccount(data.key());
        var originAccount = findCurrentAccount(cpfLogged);
        var transaction = new Transaction(null, originAccount, destinyAccount, TypeTransaction.PIX, data.value(), LocalDateTime.now(), data.description());
        originAccount.addsentTransaction(transaction);
        destinyAccount.addReceivedTransaction(transaction);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    public Page<DataStatement> getStatement(String numeroConta, Pageable pageable) {
        var cpfLogged = securityService.getCpfUserLogged();
        validateExistsAccount(numeroConta);
        validateOriginAccountAndCpfLogged(cpfLogged, numeroConta);
        var account = accountRepository.getReferenceByNumero(numeroConta);

        return transactionRepository.findAllByAccount(account, pageable).map(t -> new DataStatement(t, account));
    }

    private Account findCurrentAccount(String key){
        if (!customerRepository.existsByCpfOrEmail(key, key)){
            throw new IllegalArgumentException("Key ("+ key + ") inválida!");
        }
        var customer = customerRepository.findByCpfOrEmail(key, key).get();
        var account = accountRepository.findByCustomerIdAndTipo(customer.getId(), TypeAccount.CORRENTE).get();

        return account;
    }

    private void validateExistsAccount(String account){
        if (account != null){
            if (!accountRepository.existsByNumero(account)){
                throw new IllegalArgumentException("Conta (" + account + ") não existe!");
            }
        }
    }

    private void validateOriginAccountAndCpfLogged(String cpfLogged, String originAccount){
        if (originAccount != null){
            var origin = accountRepository.getReferenceByNumero(originAccount);
            var customerLogged = customerRepository.findByCpf(cpfLogged).get();

            if (!origin.getCustomer().equals(customerLogged)){
                throw new IllegalArgumentException("Você não tem permissão para movimentar a conta (" + origin.getNumero() + ")");
            }
        }
    }


}
