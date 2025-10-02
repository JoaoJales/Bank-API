package bank.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DataPutPassword(
        @Schema(example = "antiga123")
        @NotBlank
        String oldPassword,

        @Schema(example = "nova1234")
        @NotBlank
        String newPassword
) {
}
