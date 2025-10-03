## 🏦 Bank Api!

Bank API é um sistema RESTful de simulação de operações bancárias desenvolvido com **Java + Spring Boot**.
O projeto foi estruturado para simular as principais operações de um banco real, como depósitos, saques, pagamentos, transferências e consulta de extrato.

--- 

### 📚 Sumário

1. [Principais Funcionalidades](#-principais-funcionalidades)
2. [Regras de Negócio](#-regras-de-negócio)
3. [Autenticação & Autorização](#-autenticação--autorização)
4. [Endpoints](#-endpoints-visão-geral)
    - [Usuários](#usuarios)
    - [Contas Bancárias](#contas-bancarias)
    - [Transações](#transaçoes)
5. [Exemplos de JSON](#-exemplos-de-json)
    - [Cadastro de usuário](#cadastro-de-usuario)
    - [Criação de nova Conta](#)
    - [Transferência](#transferencia)
    - [Saque](#saque)
    - [PIX](#pix)
    - [Extrato](#extrato)
6. [Tecnologias utilizadas](#-tecnologias-utilizadas)
7. [Como rodar o projeto localmente](#️-como-rodar-o-projeto-localmente)
8. [Documentação](#-documentação)

--- 
## 📌 Principais Funcionalidades

- **Usuários**
    - Cadastro, autenticação e gestão de conta (dados pessoais, senha).
    - Consultas de extratos de contas
- **Contas**
    - Criação de Contas (Corrente, Poupança, Salário).
- **Transações**
    - Depósitos
    - Saques
    - Transferências
    - Pagamentos
    - PIX
> Obs: Cada transação e cada tipo de Conta bancária possui regras de negócio diferentes e bem definidas

- Fluxo Básico
  - Cliente se registra e recebe uma conta corrente
  - Cliente realiza um depósito na sua conta
  - Cliente pode realizar diferentes tipos de transações (ex: receber uma transferência / realizar um pix / sacar dinheiro)
  - Cliente pode consultar o extrato de uma conta


<details>
<summary style="font-size: 1.5em;"><strong>⚙️ Utilize esses usuários para Testes</strong></summary>

##### Customer 1 (Cliente 1):
```json
{
  "cpf":"123.456.789-10",
  "password":"customer1"
}
```

##### Customer 2 (Cliente 2):
```json
{
  "cpf":"123.456.789-11",
  "password":"customer2"
}
```

</details>

--- 

## 📘 Regras de Negócio

👉 Veja todas as regras de funcionamento da aplicação no arquivo [`RULES.md`](./RULES.md)

--- 
## 🔐 Autenticação & Autorização
- Autenticação baseada em **JWT (JSON Web Token)**.

--- 

## 📦 Endpoints (visão geral)

> POST -> `/populate` : Popular Banco de Dados (Opcional)

<details>
<summary style="font-size: 1.5em;"><strong>👤 Usuários</strong></summary>

| Método | Endpoint                  | Descrição                               |
|--------|---------------------------|-----------------------------------------|
| POST   | `/register`               | Cadastrar um novo cliente com conta     |
| POST   | `/login`                  | Realizar login (retorna token (JWT))    |
| PUT    | `/customers`              | Atualizar dados cadastrais do cliente   |
| PUT    | `/user/password`          | Atualizar senha                         |
| GET    | `/customers`              | Consultar todos os clientes             |
| GET    | `/customers/{customerId}` | Consultar detalhes de todos os Clientes |

</details>


<details>
<summary style="font-size: 1.5em;"><strong>📌 Contas Bancárias</strong></summary>

| Método | Endpoint                 | Descrição             |
|--------|--------------------------|-----------------------|
| POST   | `/accounts`              | Cria uma nova conta   |
| DELETE | `/accounts/{numeroCota}` | Desativa uma conta    |

</details>

<details>
<summary style="font-size: 1.5em;"><strong>🔄 Transações</strong></summary>

| Método | Endpoint                   | Descrição                               |
|--------|----------------------------|-----------------------------------------|
| POST   | `/transactions/deposit`    | Realizar um Depósito                    |
| POST   | `/transactions/transfer`   | Realizar uma Transferência              |
| POST   | `/transaction/pix`         | Realizar um PIX via key (CPF/Email)     |
| POST   | `/transactions/withdrawal` | Realizar um Saque                       |
| POST   | `/transactions/payment`    | Realizar um Pagamento                   |
| GET    | `/statement/{numeroConta}` | **Consulta o extrato da conta**         |

</details>


---

--- 

## 📄 Exemplos de JSON

<details>
<summary style="font-size: 1.5em;"><strong>🧑 Cadastro de Usuário</strong></summary>

```json
{
  "user":{
    "senha":"12345678"
  },
"customer": {
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
  }
},
  
  "conta": {
      "numero": "1234567-8"
  }
}

```

</details>

<details>
<summary style="font-size: 1.5em;"><strong>📌 Cadastro de Nova Conta</strong></summary>

##### request:
```json
{
  "numero":"1534147-9",
  "tipo":"POUPANCA"
}
```
##### response:
```json
{
  "id": 3,
  "numero": "1534147-9",
  "saldo": 100,
  "tipo": "POUPANCA"
}
```
</details>

<details>
<summary style="font-size: 1.5em;"><strong>🔄 Transferência</strong></summary>

##### request:
```json
{
  "originAccount":"1234567-8",
  "destinyAccount":"9284567-4",
  "value":100.00,
  "description":"transferencia bancaria"
}
```
##### response:
```json
{
  "id": 7,
  "originAccount": "1234567-9",
  "destinyAccount": "1234567-8",
  "value": 50,
  "date": "2025-07-02T014:50:36.6861109",
  "typeTransaction": "TRANSFERENCIA",
  "description": "transferencia bancaria"
}
```

</details>

<details>
<summary style="font-size: 1.5em;"><strong>💸 Saque</strong></summary> 

##### request:
```json
{
  "originAccount": "1234567-9",
  "value": 100,
  "description": "Saque em dinheiro"
}
```
##### response:
```json
{
  "id": 8,
  "originAccount": "1234567-9",
  "destinyAccount": null,
  "value": 20,
  "date": "2025-08-16T07:59:50.0332117",
  "typeTransaction": "SAQUE",
  "description": "Saque em dinheiro"
}
```
</details>

<details>
<summary style="font-size: 1.5em;"><strong>❖ PIX</strong></summary> 

##### request:
```json
{
  "key": "123.456.789-10",
  "value": 100,
  "description": "Pix de pagamento da conta do restaurante"
}
```
##### response:
```json
{
  "id": 11,
  "originAccount": "1234567-9",
  "destinyAccount": "1234567-3",
  "value": 100,
  "date": "2025-07-10T05:12:16.2540227",
  "typeTransaction": "PIX",
  "description": "Pix de pagamento da conta do restaurante"
}
```
</details>

<details>
<summary style="font-size: 1.5em;"><strong>Extrato</strong></summary> 

##### response:
```json
{
  "content": [
    {
      "value": "-R$ 100.00",
      "date": "2025-09-02T09:04:16",
      "typeTransaction": "PIX",
      "description": "Pix de pagamento da conta do restaurante"
    },
    {
      "value": "+R$ 100.00",
      "date": "2025-09-01T11:50:30",
      "typeTransaction": "TRANSFERENCIA",
      "description": "transferencia bancaria"
    },
    {
      "value": "+R$ 300.00",
      "date": "2025-08-30T016:49:28",
      "typeTransaction": "DEPOSITO",
      "description": "deposito em dinheiro"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "...":"..."
}}
```
</details>

---
## 🚀 Tecnologias utilizadas

### Linguagem & Framework
- **Java 21**
- **Spring Boot**

### Persistência
- **Spring Data JPA**
- **Hibernate**
- **Flyway**
- **MySQL**
### Segurança
- **Spring Security**
- **JWT - Auth0**
### Outros
- **Maven**
- **Lombok**
- **Postman**
- **Spring Doc**
- **Testes com JUnit + Mockito**
- **Swagger/OpenAPI (documentação)**


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

