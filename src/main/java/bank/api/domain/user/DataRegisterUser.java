package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record DataRegisterUser(
        @NotBlank
        String senha
) {
}
