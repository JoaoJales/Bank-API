package bank.api.domain.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DataAddress(
        @Schema(example = "Rua das palmeiras")
        @NotBlank
        String logradouro,

        @Schema(example = "Copacabana")
        @NotBlank
        String bairro,

        @Schema(example = "22011034")
        @NotBlank
        String cep,

        @Schema(example = "Rio de Janeiro")
        @NotBlank
        String cidade,

        @Schema(example = "RJ")
        @NotBlank
        String uf,

        @Schema(example = "Ap 504")
        String complemento,

        @Schema(example = "50")
        String numero
) {
}
