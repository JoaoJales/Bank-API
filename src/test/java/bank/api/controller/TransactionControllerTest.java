package bank.api.controller;

import bank.api.domain.transaction.DataDetailingTransaction;
import bank.api.domain.transaction.Transaction;
import bank.api.domain.transaction.TransactionService;
import bank.api.domain.transaction.TypeTransaction;
import bank.api.domain.transaction.dtosTransactions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TransactionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataDeposit> dataDepositJson;

    @Autowired
    private JacksonTester<DataTransfer> dataTransferJson;

    @Autowired
    private JacksonTester<DataWithdrawal> dataWithdrawalJson;

    @Autowired
    private JacksonTester<DataPayment> dataPaymentJson;

    @Autowired
    private JacksonTester<DataPix> dataPixJson;


    @Autowired
    private JacksonTester<DataDetailingTransaction> dataDetailingTransactionJson;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de depósito estão invalidas")
    @WithMockUser
    void deposit_case1() throws Exception {
        var response = mvc.perform(post("/transactions/deposit")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de depósito estiverem corretas")
    @WithMockUser
    void deposit_case2() throws Exception {
        var dataDeposit = getDefaultDataDeposit();
        var transaction = new Transaction(null, null, null, TypeTransaction.DEPOSITO, dataDeposit.value(), LocalDateTime.now(), dataDeposit.description());
        var dataDetailingTransaction = getDataDetailingTransaction(transaction);

        when(transactionService.deposit(dataDeposit)).thenReturn(transaction);

        var response = mvc.perform(post("/transactions/deposit").contentType(MediaType.APPLICATION_JSON)
                .content(dataDepositJson.write(dataDeposit).getJson())).andReturn().getResponse();

        var jsonEsperado = dataDetailingTransactionJson.write(dataDetailingTransaction).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de transferência estão invalidas")
    @WithMockUser
    void transfer_case1() throws Exception {
        var response = mvc.perform(post("/transactions/transfer")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de transferência estiverem corretas")
    @WithMockUser
    void transfer_case2() throws Exception {
        var dataTransfer = getDefaultDataTransfer();
        var transaction = new Transaction(null, null, null, TypeTransaction.TRANSFERENCIA, dataTransfer.value(), LocalDateTime.now(), dataTransfer.description());
        var dataDetailingTransaction = getDataDetailingTransaction(transaction);

        when(transactionService.transfer(dataTransfer)).thenReturn(transaction);

        var response = mvc.perform(post("/transactions/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(dataTransferJson.write(dataTransfer).getJson())).andReturn().getResponse();

        var jsonEsperado = dataDetailingTransactionJson.write(dataDetailingTransaction).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de saque estão invalidas")
    @WithMockUser
    void withdrawal_case1() throws Exception {
        var response = mvc.perform(post("/transactions/withdrawal")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de saque estiverem corretas")
    @WithMockUser
    void withdrawal_case2() throws Exception {
        var dataWithdrawal = getDefaultDataWithdrawal();
        var transaction = new Transaction(null, null, null, TypeTransaction.SAQUE, dataWithdrawal.value(), LocalDateTime.now(), dataWithdrawal.description());
        var dataDetailingTransaction = getDataDetailingTransaction(transaction);

        when(transactionService.withdrawal(dataWithdrawal)).thenReturn(transaction);

        var response = mvc.perform(post("/transactions/withdrawal").contentType(MediaType.APPLICATION_JSON)
                .content(dataWithdrawalJson.write(dataWithdrawal).getJson())).andReturn().getResponse();

        var jsonEsperado = dataDetailingTransactionJson.write(dataDetailingTransaction).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de pagamento estão invalidas")
    @WithMockUser
    void payment_case1() throws Exception {
        var response = mvc.perform(post("/transactions/payment")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de pagamento estiverem corretas")
    @WithMockUser
    void payment_case2() throws Exception {
        var dataPayment = getDefaultDataPayment();
        var transaction = new Transaction(null, null, null, TypeTransaction.TRANSFERENCIA, dataPayment.value(), LocalDateTime.now(), dataPayment.description());
        var dataDetailingTransaction = getDataDetailingTransaction(transaction);

        when(transactionService.payment(dataPayment)).thenReturn(transaction);

        var response = mvc.perform(post("/transactions/payment").contentType(MediaType.APPLICATION_JSON)
                .content(dataPaymentJson.write(dataPayment).getJson())).andReturn().getResponse();

        var jsonEsperado = dataDetailingTransactionJson.write(dataDetailingTransaction).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de Pix estão invalidas")
    @WithMockUser
    void pix_case1() throws Exception {
        var response = mvc.perform(post("/transactions/pix")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de Pix estiverem corretas")
    @WithMockUser
    void pix_case2() throws Exception {
        var dataPix = getDefaultDataPix();
        var transaction = new Transaction(null, null, null, TypeTransaction.TRANSFERENCIA, dataPix.value(), LocalDateTime.now(), dataPix.description());
        var dataDetailingTransaction = getDataDetailingTransaction(transaction);

        when(transactionService.pix(dataPix)).thenReturn(transaction);

        var response = mvc.perform(post("/transactions/pix").contentType(MediaType.APPLICATION_JSON)
                .content(dataPixJson.write(dataPix).getJson())).andReturn().getResponse();

        var jsonEsperado = dataDetailingTransactionJson.write(dataDetailingTransaction).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DataDeposit getDefaultDataDeposit(){
        return new DataDeposit("1111111-1", BigDecimal.valueOf(10.00), "Descrição");
    }

    private DataTransfer getDefaultDataTransfer(){
        return new DataTransfer("0000000-0","1111111-1", BigDecimal.valueOf(10.00), "Descrição");
    }

    private DataWithdrawal getDefaultDataWithdrawal(){
        return new DataWithdrawal("1111111-1", BigDecimal.valueOf(10.00), "Descrição");
    }
    private DataPayment getDefaultDataPayment(){
        return new DataPayment(BigDecimal.valueOf(10.00), "Descrição");
    }

    private DataPix getDefaultDataPix(){
        return new DataPix("000.000.000-00",BigDecimal.valueOf(10.00), "Descrição");
    }

    private DataDetailingTransaction getDataDetailingTransaction(Transaction transaction){
        return new DataDetailingTransaction(transaction.getId(),
                transaction.getOriginAccount() != null ? transaction.getOriginAccount().getNumero() : null,
                transaction.getDestinyAccount() != null ? transaction.getDestinyAccount().getNumero() : null,
                transaction.getValue() , transaction.getDate(), transaction.getTypeTransaction(),
                transaction.getDescription() != null ? transaction.getDescription() : null);
    }
}