package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataRegisterUser(
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}")
        String cpf,
        @NotBlank
        String senha
) {
}
