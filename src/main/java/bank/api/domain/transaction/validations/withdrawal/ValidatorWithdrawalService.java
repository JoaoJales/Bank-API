package bank.api.domain.transaction.validations.withdrawal;


import bank.api.domain.transaction.dtosTransactions.DataWithdrawal;

public interface ValidatorWithdrawalService {
    void validate(DataWithdrawal data);
}
