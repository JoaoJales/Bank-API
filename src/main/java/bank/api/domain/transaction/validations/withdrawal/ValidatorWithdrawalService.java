package bank.api.domain.transaction.validations.withdrawal;


import bank.api.dto.transactions.DataWithdrawal;

public interface ValidatorWithdrawalService {
    void validate(DataWithdrawal data);
}
