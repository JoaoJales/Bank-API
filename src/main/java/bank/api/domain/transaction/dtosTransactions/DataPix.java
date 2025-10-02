package bank.api.domain.transaction.dtosTransactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataPix(
        @Schema(example = "033.934.654-32")
        @NotBlank
        String key,

        @Schema(example = "100.00")
        @NotNull
        @Positive
        BigDecimal value,

        @Schema(example = "Pix de pagamento da conta do restaurante")
        String description
) {
}
