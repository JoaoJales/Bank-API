package bank.api.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DataRegisterUser(
        @Schema(example = "senha1234")
        @NotBlank
        String senha
) {
}
