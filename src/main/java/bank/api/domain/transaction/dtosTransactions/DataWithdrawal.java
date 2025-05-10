package bank.api.domain.transaction.dtosTransactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataWithdrawal(
        @NotBlank
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        String originAccount,

        @NotNull
        @Positive
        BigDecimal value,

        String description
) {
}
