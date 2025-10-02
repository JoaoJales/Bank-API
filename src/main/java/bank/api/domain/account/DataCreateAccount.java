package bank.api.domain.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataCreateAccount(
        @NotBlank
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        @Schema(example = "1234567-8")
        String numero,

        @Schema(example = "CORRENTE")
        TypeAccount tipo
) {
}
