package bank.api.domain.transaction.dtosTransactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DataTransfer(
        @Schema(example = "1234678-8")
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        String originAccount,

        @Schema(example = "7944698-2")
        @NotBlank
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        String destinyAccount,

        @Schema(example = "100.00")
        @NotNull
        @Positive
        BigDecimal value,

        @Schema(example = "transferencia bancaria")
        String description
) {
}
