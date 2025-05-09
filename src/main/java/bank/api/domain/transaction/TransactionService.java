package bank.api.domain.transaction;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.transaction.dtosTransactions.DataDeposit;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Transaction deposit(DataDeposit data){
        if (!accountRepository.existsByNumero(data.destinyAccount())){
            throw new RuntimeException("Conta de destino não existe!");
        }

        var contaDestino = accountRepository.getReferenceByNumero(data.destinyAccount());
        var transaction = new Transaction(null, null, contaDestino, TypeTransaction.DEPOSITO, data.value(), LocalDateTime.now(), data.description());
        contaDestino.putAccountReceivedTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(contaDestino);

        return transaction;
    }
}
