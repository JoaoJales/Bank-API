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
        validateAccounts(data.destinyAccount(), null);

        validatorsDeposit.forEach(d -> d.validate(data));

        var destinyAccount = accountRepository.getReferenceByNumero(data.destinyAccount());
        var transaction = new Transaction(null, null, destinyAccount, TypeTransaction.DEPOSITO, data.value(), LocalDateTime.now(), data.description());
        destinyAccount.addReceivedTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(destinyAccount);

        return transaction;
    }

    @Transactional
    public Transaction transfer(DataTransfer data, String cpf){
        Account originAccount;
        if (data.originAccount() == null){
            validateAccounts(data.destinyAccount(), null);
            originAccount = findCurrentAccount(cpf);
        }else {
            validateAccounts(data.destinyAccount(), data.originAccount());
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
    public Transaction withdrawal(@Valid DataWithdrawal data, String cpf) {

        Account originAccount;
        if (data.originAccount() == null){
            originAccount = findCurrentAccount(cpf);
        }else {
            validateAccounts(null, data.originAccount());
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
    public Transaction payment(DataPayment data, String cpf){

        validatorsPayment.forEach(p -> p.validate(data, cpf));

        var originAccount = findCurrentAccount(cpf);
        var transaction = new Transaction(null, originAccount, null, TypeTransaction.PAGAMENTO, data.value(), LocalDateTime.now(), data.description());
        originAccount.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(originAccount);

        return transaction;
    }

    @Transactional
    public Transaction pix(DataPix data, String keyOrigin){

        validatorsPix.forEach(p -> p.validate(data, keyOrigin));

        var destinyAccount = findCurrentAccount(data.key());
        var originAccount = findCurrentAccount(keyOrigin);
        var transaction = new Transaction(null, originAccount, destinyAccount, TypeTransaction.PIX, data.value(), LocalDateTime.now(), data.description());
        originAccount.addsentTransaction(transaction);
        destinyAccount.addReceivedTransaction(transaction);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    public Page<DataStatement> getStatement(String numeroConta, Pageable pageable) {
        validateAccounts(null, numeroConta);
        var account = accountRepository.getReferenceByNumero(numeroConta);

        return transactionRepository.findAllByAccount(account, pageable).map(t -> new DataStatement(t, account));
    }

    private Account findCurrentAccount(String key){
        if (!customerRepository.existsByCpfOrEmail(key, key)){
            throw new IllegalArgumentException("Key inválida!");
        }
        var customer = customerRepository.findByCpfOrEmail(key, key).get();
        var account = accountRepository.findByCustomerIdAndTipo(customer.getId(), TypeAccount.CORRENTE).get();

        return account;
    }

    private void validateAccounts(String destinyAccount, String originAccount){
        if (destinyAccount != null){
            if (!accountRepository.existsByNumero(destinyAccount)){
                throw new IllegalArgumentException("Conta de destino não existe!");
            }
        }

        if (originAccount != null){
            if (!accountRepository.existsByNumero(originAccount)){
                throw new IllegalArgumentException("Conta de origem não existe!");
            }
        }
    }

}
