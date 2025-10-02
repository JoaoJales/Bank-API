package bank.api.dto.transactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataWithdrawal(
        @Schema(example = "7941698-2")
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        String originAccount,

        @Schema(example = "100.00")
        @NotNull
        @Positive
        BigDecimal value,

        @Schema(example = "Saque em dinheiro")
        String description
) {
}
