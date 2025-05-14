package bank.api.domain.transaction.dtosTransactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataPix(
        @NotBlank
        String key,
        @NotNull
        @Positive
        BigDecimal value,
        String description
) {
}
