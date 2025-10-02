package bank.api.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataAuthorizationUser(
        @Schema(example = "123.456.789-10")
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao padrão 000.000.000-00")
        String cpf,

        @Schema(example = "senha1234")
        @NotBlank
        String password
) {
}
