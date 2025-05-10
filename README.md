## 🏦 Bank Api!

**Bank API** é uma API RESTful simples desenvolvida com **Java + Spring Boot**, que simula operações bancárias básicas, como:

- Cadastro de clientes e contas
- Depósitos, saques, pagamentos e transferências
- Consulta de extrato e histórico de transações
- Autenticação de usuários via cpf e senha

--- 
## 📦 Endpoints principais

| Método | Endpoint                                   | Descrição                                        |
|--------|--------------------------------------------|--------------------------------------------------|
| POST   | `/register`                                | Cadastra um novo cliente com conta               |
| POST   | `/login`                                   | Realiza login e retorna token (JWT)              |
| PUT    | `/customers`                               | Atualiza dados cadastrais do cliente             |
| POST   | `/customers/{id}/account`                  | Cria uma nova conta bancaria                     |
| POST   | `/transactions/deposit`                    | Realizar um depósito                             |
| POST   | `/transactions/transfer`                   | Realizar uma transferência                       |
| POST   | `/transactions/withdrawal`                 | Realizar um saque                                |
| GET    | `/statement/{numeroConta}`                 | **Consulta o extrato da conta**                  |
| GET    | `/customers`                               | Busca uma lista de todos os clientes cadastratos |
| GET    | `/customers/{id}`                          | Busca detalhes de um cliente                     | 
---

## 📄 Exemplos de JSON
### Transferência
```json
{
  "originAccount":"1234567-8",
  "destinyAccount":"9284567-4",
  "value":"100.00",
  "description":"transferencia bancaria"
}
```

### Depósito
```json
{
  "destinyAccount":"1234567-8",
  "value":"1000.00",
  "description":"deposito em dinheiro"
}
```

### Cliente
```json
{
  "id": 1,
  "nome": "Exemplo",
  "cpf": "000.000.000-00",
  "email": "exemplo@gmail.com",
  "telefone": "(00)00000-0000",
  "dt_nascimento": "0000-00-00",
  "address": {
    "logradouro": "rua",
    "bairro": "bairro",
    "cep": "00000000",
    "cidade": "Cidade",
    "uf": "UF",
    "numero": "4",
    "complemento": "complemento"
  },
  "accounts": [
    {
      "id": 1,
      "numero": "1234567-8",
      "saldo": 1000.00
    }
  ]
}
```
---
## 🚀 Tecnologias utilizadas

- **[Java 21](https://www.oracle.com/java)**
- **[Spring Boot](https://spring.io/projects/spring-boot)**
- **[Spring Data JPA](https://spring.io/projects/spring-boot)**
- **[Spring Security (login/autenticação)](https://spring.io/projects/spring-boot)**
- **[Maven](https://maven.apache.org)**
- **[MySQL](https://www.mysql.com)**
- **[Hibernate](https://hibernate.org)**
- **[Flyway](https://flywaydb.org)**
- **[Lombok](https://projectlombok.org)**
- **[Postman]()**
<!-- - **[Swagger (documentação)]()** -->

---
## 🔐 Segurança
- Endpoints exigem autenticação (cpf + senha).
- Usuário autenticado só pode movimentar sua própria conta.

---

## 📄 Documentação

Após rodar a aplicação:

Acesse http://localhost:8080/swagger-ui.html para explorar a documentação interativa da API com Swagger.

--- 

