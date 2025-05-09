package bank.api.domain.transaction.dtosTransactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataDeposit(
        @NotBlank
        @Pattern(regexp = "\\d{7}\\-?\\d{1}")
        String destinyAccount,

        @NotNull
        @Positive
        BigDecimal value,

        String description

        ) {
}
