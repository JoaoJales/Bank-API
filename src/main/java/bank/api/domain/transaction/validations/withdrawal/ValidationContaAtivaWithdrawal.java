package bank.api.domain.transaction.validations.withdrawal;

import bank.api.domain.account.AccountRepository;
import bank.api.dto.transactions.DataWithdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaWithdrawal implements ValidatorWithdrawalService {
    @Autowired
    private AccountRepository repository;

    @Override
    public void validate(DataWithdrawal data) {
        if (data.originAccount() != null) {
            if (!repository.existsByNumeroAndAtivoTrue(data.originAccount())) {
                throw new IllegalArgumentException("Conta de origem inativa!");
            }
        }
    }
}
