package bank.api.domain.transaction.dtosTransactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataPayment(
        @Schema(example = "100.00")
        @NotNull
        @Positive
        BigDecimal value,

        @Schema(example = "pagamento conta de luz")
        String description
) {
}
