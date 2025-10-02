package bank.api.domain.transaction.validations.transfer;

import bank.api.domain.account.AccountRepository;
import bank.api.dto.transactions.DataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaAtivaTransfer implements ValidatorTransferService {
    @Autowired
    private AccountRepository repository;

    @Override
    public void validate(DataTransfer data) {
        if (data.originAccount() != null) {
            if (!repository.existsByNumeroAndAtivoTrue(data.originAccount())) {
                throw new IllegalArgumentException("Conta de origem inativa!");
            }
        }

        if (!repository.existsByNumeroAndAtivoTrue(data.destinyAccount())){
            throw new IllegalArgumentException("Conta de destino inativa!");
        }
    }
}
