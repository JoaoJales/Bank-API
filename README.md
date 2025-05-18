## 🏦 Bank Api!

**Bank API** é uma API RESTful desenvolvida com **Java + Spring Boot**, que simula operações bancárias básicas, como:

- Cadastro de clientes e contas
- Depósitos, saques, pagamentos e transferências
- Consulta de extrato e histórico de transações
- Autenticação de usuários via cpf e senha

--- 
## 📦 Endpoints principais

| Método | Endpoint                   | Descrição                                       |
|--------|----------------------------|-------------------------------------------------|
| POST   | `/register`                | Cadastra um novo cliente com conta              |
| POST   | `/login`                   | Realiza login e retorna token (JWT)             |
| PUT    | `/customers`               | Atualiza dados cadastrais do cliente            |
| PUT    | `/user/password`           | Atualiza senha                                  |
| POST   | `/accounts`                | Cria uma nova conta bancaria                    |
| POST   | `/transactions/deposit`    | Realizar um depósito                            |
| POST   | `/transactions/transfer`   | Realizar uma transferência                      |
| POST   | `/transaction/pix`         | Realiza um PIX via key (CPF/Email)              |
| POST   | `/transactions/withdrawal` | Realizar um saque                               |
| GET    | `/statement/{numeroConta}` | **Consulta o extrato da conta**                 |
| GET    | `/customers`               | Busca uma lista de todos os clientes cadastrados |
| GET    | `/customers/{id}`          | Busca detalhes de um cliente                    |
---
## 📘 Regras de Negócio

👉 Veja todas as regras de funcionamento da aplicação no arquivo [`RULES.md`](./RULES.md)

--- 

## 📄 Exemplos de JSON
### 🔄 Exemplo de Transferência
```json
{
  "originAccount":"1234567-8",
  "destinyAccount":"9284567-4",
  "value":"100.00",
  "description":"transferencia bancaria"
}


```

### 🧑 Exemplo de Cliente
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

### Linguagem & Framework
- **[Java 21](https://www.oracle.com/java)**
- **[Spring Boot](https://spring.io/projects/spring-boot)**

### Persistência
- **[Spring Data JPA](https://spring.io/projects/spring-boot)**
- **[Hibernate](https://hibernate.org)**
- **[Flyway](https://flywaydb.org)**
- **[MySQL](https://www.mysql.com)**
### Segurança
- **[Spring Security](https://spring.io/projects/spring-boot)**
- **JWT - Auth0**
### Outros
- **[Maven](https://maven.apache.org)**
- **[Lombok](https://projectlombok.org)**
- **[Postman]()**
<!-- - **[Swagger (documentação)]()** -->


---
## ▶️ Como rodar o projeto localmente

### 1. Pré-requisitos

- **Java 21+**
- **Maven 3.8+**
- **MySQL**
- (Opcional) **IntelliJ IDEA** ou **VSCode**

### 2. Configuração do banco de dados

Configure o banco de dados no arquivo `src/main/resources/application.properties` com suas credenciais locais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank
spring.datasource.username=root
spring.datasource.password=1234
```

> 💡 Certifique-se de que o banco `bank` já existe antes de iniciar a aplicação.

### 3. Rodando via terminal (Maven)

```bash
# Baixar dependências e compilar o projeto
mvn clean install

# Rodar a aplicação
mvn spring-boot:run
```

### 4. Rodando via IntelliJ

1. Abra o projeto no IntelliJ
2. Aguarde o carregamento do Maven
3. Navegue até a classe `BankApplication.java`
4. Clique com o botão direito e selecione **Run 'BankApplication'**



--- 

## 📄 Documentação

Após rodar a aplicação:

Acesse http://localhost:8080/swagger-ui.html para explorar a documentação interativa da API com Swagger.

--- 

