package bank.api.domain.address;

import jakarta.validation.constraints.NotBlank;

public record DataAddress(
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        String cep,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,

        String complemento,

        String numero
) {
}
