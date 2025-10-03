INSERT INTO users (login, password) VALUES
('034.654.234-87','$2a$12$OoE4vB/jfBI0JsiCd.ctjezQXW2GmhK/3ysaiwnFomSHhV6pjHE1C'),
('129.134.998-17','$2a$12$OoE4vB/jfBI0JsiCd.ctjezQXW2GmhK/3ysaiwnFomSHhV6pjHE1C'),
('008.551.034-13','$2a$12$OoE4vB/jfBI0JsiCd.ctjezQXW2GmhK/3ysaiwnFomSHhV6pjHE1C'),
('014.754.884-80','$2a$12$OoE4vB/jfBI0JsiCd.ctjezQXW2GmhK/3ysaiwnFomSHhV6pjHE1C'),
('123.456.789-10','$2a$12$0UBegh3zA1FD9c4JZQDYWeIHkB2gnkYgYUuAX4.RBxxk.IL5.3cwG'),
('123.456.789-11','$2a$12$Gkly5sb.9AA/inok3K/aCOrOBIgKS4Uw4gR70VucoElAwo2L8V4/C');


INSERT INTO customers (nome, email, cpf, telefone, dt_nascimento, logradouro, bairro, cep, complemento, numero, uf, cidade) VALUES
('Maria Silva', 'maria.silva@gmail.com', '034.654.234-87', '61998765218', '1995-05-12', 'Avenida Paulista', 'Bela Vista', '01310100', 'apto 101', '150', 'SP', 'São Paulo'),
('Carlos Eduardo Santos', 'carlos.santos@yahoo.com', '129.134.998-17', '21994887621', '1990-02-16', 'Rua das Palmeiras', 'Copacabana', '22011000', 'casa', '50', 'RJ', 'Rio de Janeiro'),
('Ana Clara Oliveira', 'ana.clara@hotmail.com', '008.551.034-13', '61319888844', '2002-10-25', 'Rua de Ouro Preto', 'Santo Agostinho', '30170050', 'bloco A', '100', 'MG', 'Belo Horizonte'),
('Rafael Costa', 'rafael.costa@email.com', '014.754.884-80', '53859777711', '2001-04-29', 'Avenida Beira Mar', 'Meireles', '60165120', NULL, '2020', 'CE', 'Fortaleza'),
('Customer da Silva', 'customer1@gmail.com', '123.456.789-10', '55942216759', '2004-01-10' ,'Rua xpto', 'Bairro xpto', '35131766', 'ap 601', '11', 'DF', 'Brasilia'),
('Customer Pereira', 'customer2@gmail.com', '123.456.789-11', '55981242159', '2005-10-08' ,'Rua xpto', 'Bairro xpto', '35131742', 'casa', '12','DF', 'Brasilia');



INSERT INTO accounts (numero, saldo, customer_id, tipo) VALUES
('1234567-0', 1500.50, 1, 'CORRENTE'),
('1234567-1', 8000.00, 1, 'POUPANCA'),
('9876543-0', 350.25, 2, 'CORRENTE'),
('5555555-3', 1120.00, 3, 'CORRENTE'),
('5555555-0', 120.00, 3, 'SALARIO'),
('1112223-0', 50000.75, 4, 'CORRENTE'),
('7778889-1', 2300.00, 5, 'CORRENTE'),
('7778889-0', 0.00, 5, 'POUPANCA'),
('4443332-1', 789.99, 6, 'CORRENTE'),
('4443332-0', 789.99, 6, 'POUPANCA');


INSERT INTO transactions (origin_account_id, destiny_account_id, valor, data, tipo, descricao) VALUES
(NULL, 1, 500.00, NOW(), 'DEPOSITO', 'Pagamento de boleto de terceiros.'),
(NULL, 3, 100.00, NOW(), 'DEPOSITO', 'Dinheiro em envelope.'),
(NULL, 6, 2500.00, NOW(), 'DEPOSITO', 'Recebimento de empréstimo.'),
(NULL, 7, 75.50, NOW(), 'DEPOSITO', 'Troco de venda.'),
(NULL, 4, 150.00, NOW(), 'DEPOSITO', 'Mensalidade de serviço.'),
(NULL, 2, 1000.00, NOW(), 'DEPOSITO', 'Aplicação na poupança.'),
(NULL, 10, 50.00, NOW(), 'DEPOSITO', 'Pequeno ajuste de saldo.'),
(NULL, 5, 3000.00, NOW(), 'DEPOSITO', 'Salário - Adiantamento.'),


(1, NULL, 50.00, NOW(), 'SAQUE', 'Saque no caixa 24h.'),
(3, NULL, 150.00, NOW(), 'SAQUE', 'Retirada para compras.'),
(4, NULL, 20.00, NOW(), 'SAQUE', 'Pagar pedágio.'),
(7, NULL, 200.00, NOW(), 'SAQUE', 'Dinheiro para viagem.'),
(9, NULL, 80.00, NOW(), 'SAQUE', 'Saque para despesas rápidas.'),
(1, NULL, 100.00, NOW(), 'SAQUE', 'Saque de emergência.'),
(6, NULL, 5000.00, NOW(), 'SAQUE', 'Retirada para grande compra.'),

(1, 3, 200.00, NOW(), 'TRANSFERENCIA', 'Transferência de aluguel.'),
(3, 1, 50.00, NOW(), 'TRANSFERENCIA', 'Devolução de dinheiro.'),
(6, 7, 1000.00, NOW(), 'TRANSFERENCIA', 'Transferência entre sócios.'),
(7, 4, 350.00, NOW(), 'TRANSFERENCIA', 'Pagamento de serviço.'),
(4, 9, 800.00, NOW(), 'TRANSFERENCIA', 'Quitação de dívida.'),
(9, 6, 120.50, NOW(), 'TRANSFERENCIA', 'Pagamento de jantar.'),
(2, 8, 500.00, NOW(), 'TRANSFERENCIA', 'Ajuda de custo.'),
(1, 9, 450.00, NOW(), 'TRANSFERENCIA', 'Compra de produto online.'),
(3, 6, 75.00, NOW(), 'TRANSFERENCIA', 'Reembolso de despesa.'),
(5, 7, 100.00, NOW(), 'TRANSFERENCIA', 'Doação para projeto.'),

(6, 1, 10.00, NOW(), 'PIX', 'Pix para café.'),
(7, 3, 60.00, NOW(), 'PIX', 'Pix para lanche.'),
(1, 4, 15.75, NOW(), 'PIX', 'Pagamento de assinatura.'),
(9, 5, 250.00, NOW(), 'PIX', 'Pix urgente.'),
(4, 10, 1500.00, NOW(), 'PIX', 'Compra de eletrônico.');
