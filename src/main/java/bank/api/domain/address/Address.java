package bank.api.domain.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String logradouro;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;
    private String numero;
    private String complemento;

    public Address(DataAddress data){
        this.logradouro = data.logradouro();
        this.bairro = data.bairro();
        this.cep = data.cep();
        this.cidade = data.cidade();
        this.uf = data.uf();
        this.numero = data.numero();
        this.complemento = data.complemento();
    }

    public void updateInfoAddress(DataAddress data){
        if (data.logradouro() != null) this.logradouro = data.logradouro();
        if (data.bairro() != null) this.bairro = data.bairro();
        if (data.cep() != null) this.cep = data.cep();
        if (data.uf() != null) this.uf = data.uf();
        if (data.numero() != null) this.numero = data.numero();
        if (data.complemento() != null) this.complemento = data.complemento();

    }
}
