package bank.api.domain.transaction.validations.transfer;

import bank.api.domain.transaction.dtosTransactions.DataTransfer;

public interface ValidatorTransferService {
    void validate(DataTransfer data);
}
