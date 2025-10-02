package bank.api.domain.transaction.validations.transfer;

import bank.api.dto.transactions.DataTransfer;

public interface ValidatorTransferService {
    void validate(DataTransfer data);
}
