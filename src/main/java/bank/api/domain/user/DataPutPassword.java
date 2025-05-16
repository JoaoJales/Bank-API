package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record DataPutPassword(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword
) {
}
