package bank.api.domain.transaction;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.DataDeposit;
import bank.api.domain.transaction.dtosTransactions.DataPayment;
import bank.api.domain.transaction.dtosTransactions.DataTransfer;
import bank.api.domain.transaction.dtosTransactions.DataWithdrawal;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //REALIZAR VALIDAÇÕES NOS METODOS

    @Transactional
    public Transaction deposit(DataDeposit data){
        verificaConta(data.destinyAccount(), null);

        var contaDestino = accountRepository.getReferenceByNumero(data.destinyAccount());
        var transaction = new Transaction(null, null, contaDestino, TypeTransaction.DEPOSITO, data.value(), LocalDateTime.now(), data.description());
        contaDestino.addReceivedTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(contaDestino);

        return transaction;
    }

    @Transactional
    public Transaction transfer(DataTransfer data){
        verificaConta(data.destinyAccount(), data.originAccount());

        var contaDestino = accountRepository.getReferenceByNumero(data.destinyAccount());
        var contaOrigem = accountRepository.getReferenceByNumero(data.originAccount());
        var transaction = new Transaction(null, contaOrigem, contaDestino, TypeTransaction.TRANSFERENCIA, data.value(), LocalDateTime.now(), data.description());
        contaDestino.addReceivedTransaction(transaction);
        contaOrigem.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(contaDestino);
        accountRepository.save(contaOrigem);

        return transaction;
    }

    @Transactional
    public Transaction withdrawal(@Valid DataWithdrawal data) {
        verificaConta(null, data.originAccount());

        var contaOrigem = accountRepository.getReferenceByNumero(data.originAccount());
        var transaction = new Transaction(null, contaOrigem, null, TypeTransaction.SAQUE, data.value(), LocalDateTime.now(), data.description());
        contaOrigem.addsentTransaction(transaction);


        transactionRepository.save(transaction);
        accountRepository.save(contaOrigem);

        return transaction;
    }

    @Transactional
    public Transaction payment(DataPayment data){
        verificaConta(null, data.originAccount());

        var contaOrigem = accountRepository.getReferenceByNumero(data.originAccount());
        var transaction = new Transaction(null, contaOrigem, null, TypeTransaction.PAGAMENTO, data.value(), LocalDateTime.now(), data.description());
        contaOrigem.addsentTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(contaOrigem);

        return transaction;
    }

    public Page<DataStatement> getStatement(String numeroConta, Pageable pageable) {
        verificaConta(null, numeroConta);
        var account = accountRepository.getReferenceByNumero(numeroConta);

        return transactionRepository.findAllByAccount(account, pageable).map(t -> new DataStatement(t, account));
    }

    private void verificaConta(String contaDestino, String contaOrigem){
        if (contaDestino != null){
            if (!accountRepository.existsByNumero(contaDestino)){
                throw new RuntimeException("Conta de destino não existe!");
            }
        }

        if (contaOrigem != null){
            if (!accountRepository.existsByNumero(contaOrigem)){
                throw new RuntimeException("Conta de origem não existe!");
            }
        }
    }

}
