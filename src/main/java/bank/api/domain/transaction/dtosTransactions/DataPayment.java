package bank.api.domain.transaction.dtosTransactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataPayment(
        @NotNull
        @Positive
        BigDecimal value,

        String description
) {
}
