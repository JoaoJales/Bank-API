package bank.api.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record DataRegisterUser(
        @NotBlank
        String senha
) {
}
