# 📄 Regras de Negócio - Bank API 🏦

> ⚠️ Importante: algumas regras **foram temporariamente relaxadas/desativadas** para facilitar os testes e simulações.

## 🔐 Autenticação
- Endpoints de cadastro e login **não exigem autenticação**.
- Todos os demais endpoints exigem autenticação com **token JWT**.
- O usuário realiza login via CPF e senha e recebe um Token JWT.
- O CPF extraído do token JWT é utilizado para validar permissões nas transações.
- O usuário autenticado só pode movimentar **suas próprias contas**.


---
## 🧑‍💼 Cliente

- Todas as informações de cadastro são obrigatórias, exceto o número e o complemento do endereço.
- O cliente deve ser **maior de 18 anos**.
- Informações pessoais podem ser alteradas, exceto **CPF** e **data de nascimento**.
- Ao se cadastrar, o cliente recebe automaticamente uma **conta corrente**.

> ℹ️ Endpoints públicos para facilitar o uso  (Informações de clientes):
> - `GET /customers`
> - `GET /customers/{id}`
--- 

## 📌 Conta Bancária
- Um cliente pode ter **apenas uma conta de cada tipo** (corrente, poupança, salário).
- Saldo mínimo: **R$0,00** (sem saldo negativo).
- O número da conta deve ser **único**.
- A conta pode estar **ativa** ou **inativa**.

#### ✅ Conta Corrente
- Pode realizar **todos** os tipos de transações.

#### 🟡 Conta Pounpança
- ❌ Não realiza **PIX**
- ❌ Não realiza **pagamentos**

#### 🔒 Conta Salário
- ❌ Só movimenta dinheiro entre **contas do mesmo cliente**
- ❌ Não realiza **PIX**
- ❌ Não realiza **pagamentos**

--- 
## Transações

- Todos os campos são obrigatórios, **exceto a descrição**.
- Caso a conta de origem não seja informada, será usada automaticamente a **conta corrente** do cliente logado.
- O saldo é verificado antes de qualquer transação.

### 💰 Depósitos
- Valor mínimo: **R$1,00**
- Limite diário por conta: **R$50.000,00**
- Permitidos apenas entre **07h e 17h** em dias úteis *(temporariamente desativado)*

### 💸 Saques
- Valor mínimo: **R$20,00**
- Valor deve ser **múltiplo de R$10**
- Máximo por saque: **R$2.000,00**
- Limite diário: **R$5.000,00**
- Permitidos apenas entre **06h e 22h** em dias úteis *(temporariamente desativado)*

### 🔄 Transferências
- Só é permitido transferir de uma conta do **próprio cliente**
- ❌ Não é permitido transferir para a **própria conta**
- Valor mínimo: **R$1,00**
- São permitidos apenas entre **07h e 17h** em dias úteis. _(Temporariamente desativado)_

### ❖ PIX
- Permitido **apenas para contas correntes**
- Requer uma chave válida (CPF ou e-mail) do destinatário
- ❌ Não é permitido realizar PIX para a **própria conta**

### 💳 Pagamentos
- Permitido **apenas para contas correntes**
- Permitidos apenas entre **07h e 20h** em dias úteis *(temporariamente desativado)*


--- 