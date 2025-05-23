package bank.api.controller;

import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.account.TypeAccount;
import bank.api.domain.address.Address;
import bank.api.domain.address.DataAddress;
import bank.api.domain.customer.Customer;
import bank.api.domain.customer.DataDetailingCustomer;
import bank.api.domain.customer.DataRegisterCustomer;
import bank.api.domain.register.DataRegister;
import bank.api.domain.register.RegisterService;
import bank.api.domain.user.DataRegisterUser;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class RegisterControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataRegister> dataRegisterJson;

    @Autowired
    private JacksonTester<DataDetailingCustomer> dataDetailingCustomerJson;

    @MockitoBean
    private RegisterService registerService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de registro estiverem invalidas")
    @WithMockUser
    void register_case1() throws Exception {
        var response = mvc.perform(post("/register")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de registro estiverem corretas")
    @WithMockUser
    void register_case2() throws Exception {
        var dataRegisterUser = getDataRegisterUser();
        var dataRegisterCustomer = getDataRegisterCustomer();
        var dataCreateAccount = getDataCreateAccount();
        var dataRegister = new DataRegister(dataRegisterUser, dataRegisterCustomer, dataCreateAccount);

        var dataDetailingCustomer = getDataDetailingCustomer();
        when(registerService.register(dataRegister)).thenReturn(dataDetailingCustomer);

        var response = mvc
                .perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dataRegisterJson.write(dataRegister).getJson()))
                        .andReturn().getResponse();

        var jsonEsperado = dataDetailingCustomerJson.write(dataDetailingCustomer).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DataRegisterUser getDataRegisterUser(){
        return new DataRegisterUser("12345678");
    }

    private DataRegisterCustomer getDataRegisterCustomer(){
        return new DataRegisterCustomer("nome", "email@gmail.com", "000.000.000-00", "6199999-9999", LocalDate.of(2000, 12, 12), dadosEndereco());
    }

    private DataCreateAccount getDataCreateAccount(){
        return new DataCreateAccount("1234567-8", TypeAccount.CORRENTE);
    }

    private DataDetailingCustomer getDataDetailingCustomer(){
        var customer = new Customer(1L, "nome", "email@gmail.com", "(00)11111-1111", "000.000.000-00", LocalDate.of(2000, 12, 12), new Address(dadosEndereco()), new ArrayList<>());

        return new DataDetailingCustomer(customer);
    }

    private DataAddress dadosEndereco(){
        return new DataAddress(
                "rua xpto",
                "Asa Sul",
                "43123980",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}