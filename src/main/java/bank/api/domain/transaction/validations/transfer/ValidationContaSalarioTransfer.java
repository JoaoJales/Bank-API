package bank.api.domain.transaction.validations.transfer;

import bank.api.domain.account.AccountRepository;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.transaction.dtosTransactions.DataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationContaSalarioTransfer implements ValidatorTransferService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void validate(DataTransfer data) {
        if (data.originAccount() != null){
            var originAccount = accountRepository.getReferenceByNumero(data.originAccount());
            var destinyAccount = accountRepository.getReferenceByNumero(data.destinyAccount());
            var contaSalario = originAccount.getTipo().equals(TypeAccount.SALARIO);
            var contaDestinoNaoPertenceAoCliente = !destinyAccount.getCustomer().getId().equals(originAccount.getCustomer().getId());

            if (contaSalario && contaDestinoNaoPertenceAoCliente){
                throw new IllegalArgumentException("Contas do tipo Salário só podem realizar transferências para contas vinculadas ao mesmo cliente");
            }
        }
    }
}
