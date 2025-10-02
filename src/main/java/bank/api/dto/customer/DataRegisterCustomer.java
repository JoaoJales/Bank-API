package bank.api.dto.customer;

import bank.api.dto.address.DataAddress;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DataRegisterCustomer(
        @Schema(example = "Customer da Silva")
        @NotBlank
        String nome,

        @Schema(example = "customer@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(example = "123.456.789-10")
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao padrão 000.000.000-00")
        String cpf,

        @Schema(example = "(61)99977-4806")
        @NotBlank
        String telefone,

        @Schema(example = "2005-12-17")
        @Past
        LocalDate dt_nascimento,

        @NotNull
        @Valid
        @JsonAlias("endereco")
        DataAddress address
) {
}
