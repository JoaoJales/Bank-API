package bank.api.domain.customer;

import bank.api.domain.address.DataAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DataPutCustomer(
        @NotNull
        Long id,
        String nome,
        String email,
        String telefone,
        @Valid
        DataAddress endereco
) {
}
