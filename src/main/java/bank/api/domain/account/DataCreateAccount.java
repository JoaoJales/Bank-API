package bank.api.domain.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataCreateAccount(
        @NotBlank
        @Pattern(regexp = "\\d{7}\\-\\d{1}", message = "O número da conta deve corresponder ao padrão 0000000-0")
        String numero,
        TypeAccount tipo
) {
}
