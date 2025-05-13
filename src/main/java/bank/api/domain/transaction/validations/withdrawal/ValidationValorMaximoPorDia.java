package bank.api.domain.transaction.validations.withdrawal;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.transaction.Transaction;
import bank.api.domain.transaction.TransactionRepository;
import bank.api.domain.transaction.TypeTransaction;
import bank.api.domain.transaction.dtosTransactions.DataWithdrawal;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidationValorMaximoPorDia implements ValidatorWithdrawalService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void validate(DataWithdrawal data) {
        var dateNow = LocalDateTime.now();
        var dateStart = dateNow.withHour(6);
        var dateFinal = dateNow.withHour(22);
        var limit = new BigDecimal(5000);
        var account = accountRepository.getReferenceByNumero(data.originAccount());

        List<Transaction> transactions = transactionRepository.findAllByOriginAccountAndTypeTransactionAndDateBetween(account, TypeTransaction.SAQUE, dateStart, dateFinal);

        var valor = transactions.stream()
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var soma = valor.add(data.value());

        if (soma.compareTo(limit) > 0){
            throw new IllegalArgumentException("Limite diário de R$5.000,00 atingido!");
        }

    }
}
