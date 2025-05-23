package bank.api.controller;

import bank.api.domain.customer.Customer;
import bank.api.domain.customer.CustomerService;
import bank.api.domain.customer.DataDetailingCustomer;
import bank.api.domain.customer.DataPutCustomer;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataPutCustomer> dataPutCustomerJson;

    @Autowired
    private JacksonTester<DataDetailingCustomer> dataDetailingCustomerJson;

    @MockitoBean
    private CustomerService customerService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de atualização de dados do cliente estão invalidas")
    @WithMockUser
    void putCustomers_case1() throws Exception {
        var response = mvc.perform(put("/customers")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando informações de atualização de dados do cliente estiverem corretas")
    @WithMockUser
    void putCustomers_case2() throws Exception {
        var dataPutCustomer = new DataPutCustomer(1L, "Fulano", "fulano@gmail.com", "(00)00000-0000", null);

        var customer = new Customer(1L, "nome", "email@gmail.com", "(00)11111-1111", "000.000.000-00", null, null, new ArrayList<>());

        when(customerService.putCustomers(dataPutCustomer)).thenReturn(customer);

        var dataDetailingCustomer = new DataDetailingCustomer(customer);
        var response = mvc.perform(put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dataPutCustomerJson.write(dataPutCustomer).getJson()))
                .andReturn().getResponse();

        var jsonEsperado = dataDetailingCustomerJson.write(dataDetailingCustomer).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }




}