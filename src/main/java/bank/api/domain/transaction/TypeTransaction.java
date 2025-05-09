package bank.api.domain.transaction;

public enum TypeTransaction {
    TRANSFERENCIA("Transferência"),
    DEPOSITO("Deposito"),
    SAQUE("Saque"),
    PAGAMENTO("Pagamento");

    private String type;

    TypeTransaction(String type){
        this.type = type;
    }

    public static TypeTransaction fromString(String text){
        for (TypeTransaction typeTransaction : TypeTransaction.values()){
            if (typeTransaction.type.equalsIgnoreCase(text)){
                return typeTransaction;
            }
        }

        throw new IllegalArgumentException("Tipo de transação inválida");
    }
}
