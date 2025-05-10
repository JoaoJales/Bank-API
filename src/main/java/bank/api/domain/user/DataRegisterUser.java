package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record DataRegisterUser(
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao padrão 000.000.000-00")
        String cpf,
        @NotBlank
        String senha
) {
}
