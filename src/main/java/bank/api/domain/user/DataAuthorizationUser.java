package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataAuthorizationUser(
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao padrão 000.000.000-00")
        String cpf,
        @NotBlank
        String password
) {
}
