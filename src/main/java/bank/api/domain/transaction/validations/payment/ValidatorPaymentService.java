package bank.api.domain.transaction.validations.payment;

import bank.api.domain.transaction.dtosTransactions.DataPayment;

public interface ValidatorPaymentService {
    void validate(DataPayment data);
}
