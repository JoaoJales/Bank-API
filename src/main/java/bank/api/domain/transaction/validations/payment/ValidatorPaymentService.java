package bank.api.domain.transaction.validations.payment;

import bank.api.dto.transactions.DataPayment;

public interface ValidatorPaymentService {
    void validate(DataPayment data);
}
