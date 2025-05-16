package bank.api.domain.customer;

import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.address.DataAddress;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DataRegisterCustomer(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao padrão 000.000.000-00")
        String cpf,
        @NotBlank
        String telefone,
        @Past
        LocalDate dt_nascimento,
        @NotNull
        @Valid
        @JsonAlias("endereco")
        DataAddress address
) {
}
