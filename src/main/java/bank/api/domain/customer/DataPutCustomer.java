package bank.api.domain.customer;

import bank.api.domain.address.DataAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DataPutCustomer(
        @Schema(example = "1")
        @NotNull
        Long id,

        @Schema(example = "Customer da Silva")
        String nome,

        @Schema(example = "customer@email.com")
        String email,

        @Schema(example = "(61)99977-4806")
        String telefone,

        @Valid
        DataAddress endereco
) {
}
