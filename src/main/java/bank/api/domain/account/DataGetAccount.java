package bank.api.domain.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record DataGetAccount(
        @Schema(example = "1")
        Long id,
        @Schema(example = "1234567-8")
        String numero,
        @Schema(example = "100.00")
        BigDecimal saldo,
        @Schema(example = "CORRENTE")
        TypeAccount tipo) {
    public DataGetAccount(Account account){
        this(account.getId(), account.getNumero(), account.getSaldo(), account.getTipo());
    }
}
