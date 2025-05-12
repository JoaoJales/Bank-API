package bank.api.domain.transaction.validations.deposit;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.transaction.dtosTransactions.DataDeposit;
import bank.api.domain.transaction.dtosTransactions.DataTransfer;
import bank.api.domain.transaction.validations.transfer.ValidatorTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaDeposit implements ValidatorDepositService {
    @Autowired
    private AccountRepository repository;

    @Override
    public void validate(DataDeposit data) {
        if (!repository.existsByNumeroAndAtivoTrue(data.destinyAccount())){
            throw new IllegalArgumentException("Conta de destino inativa!");
        }
    }
}
