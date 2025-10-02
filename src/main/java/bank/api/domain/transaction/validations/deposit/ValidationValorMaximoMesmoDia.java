package bank.api.domain.transaction.validations.deposit;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.transaction.Transaction;
import bank.api.domain.transaction.TransactionRepository;
import bank.api.domain.transaction.TypeTransaction;
import bank.api.dto.transactions.DataDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidationValorMaximoMesmoDia implements ValidatorDepositService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void validate(DataDeposit data) {
        var dateNow = LocalDateTime.now();
        var dateStart = dateNow.withHour(7);
        var dateFinal = dateNow.withHour(17);
        var account = accountRepository.getReferenceByNumero(data.destinyAccount()) ;
        var limit = new BigDecimal(50000);

        List<Transaction> transactions = transactionRepository.findAllByDestinyAccountAndTypeTransactionAndDateBetween(account, TypeTransaction.DEPOSITO, dateStart, dateFinal);
        var total = transactions.stream()
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var somaFinal = total.add(data.value());

        if (somaFinal.compareTo(limit) > 0){
            throw new IllegalArgumentException("Limite diário de R$50.000 em depósitos ultrapassado.");
        }
    }
}
