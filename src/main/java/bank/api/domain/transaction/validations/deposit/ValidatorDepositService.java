package bank.api.domain.transaction.validations.deposit;

import bank.api.domain.transaction.dtosTransactions.DataDeposit;

public interface ValidatorDepositService {
    void validate(DataDeposit data);
}
