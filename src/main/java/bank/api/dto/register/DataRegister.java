package bank.api.dto.register;

import bank.api.dto.account.DataCreateAccount;
import bank.api.dto.customer.DataRegisterCustomer;
import bank.api.dto.user.DataRegisterUser;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataRegister(
        @NotNull
        @Valid
        DataRegisterUser user,
        @NotNull
        @Valid
        DataRegisterCustomer customer,
        @NotNull
        @Valid
        @JsonAlias("conta")
        DataCreateAccount account
) {
}
