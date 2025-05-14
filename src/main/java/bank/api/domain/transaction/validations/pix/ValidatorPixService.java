package bank.api.domain.transaction.validations.pix;

import bank.api.domain.transaction.dtosTransactions.DataPix;

public interface ValidatorPixService {
    void validate(DataPix data, String keyOrigin);
}
