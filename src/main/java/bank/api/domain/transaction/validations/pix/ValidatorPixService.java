package bank.api.domain.transaction.validations.pix;

import bank.api.dto.transactions.DataPix;

public interface ValidatorPixService {
    void validate(DataPix data, String keyOrigin);
}
