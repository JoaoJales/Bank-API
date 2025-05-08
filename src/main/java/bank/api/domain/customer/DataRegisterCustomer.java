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
        String telefone,
        @Past
        LocalDate dt_nascimento,
        @NotNull
        @Valid
        @JsonAlias("endereco")
        DataAddress address
) {
}
