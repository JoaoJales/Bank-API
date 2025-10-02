package bank.api.domain.transaction.validations.deposit;

import bank.api.dto.transactions.DataDeposit;

public interface ValidatorDepositService {
    void validate(DataDeposit data);
}
