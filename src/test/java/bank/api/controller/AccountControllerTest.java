package bank.api.controller;

import bank.api.domain.account.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataCreateAccount> dataCreateAccountJson;

    @Autowired
    private JacksonTester<DataGetAccount> dataGetAccountJson;

    @MockitoBean
    private AccountService accountService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de nova conta estiverem invalidas")
    @WithMockUser
    void postAccounts_case1() throws Exception {
        var response = mvc.perform(post("/accounts")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações de nova conta estiverem corretas")
    @WithMockUser
    void postAccounts_case2() throws Exception {
        var dataCreateAccount  = getDataCreateAccount();
        var dataGetAccount = new DataGetAccount(null, null, null, null);
        when(accountService.createNewAccount(dataCreateAccount)).thenReturn(new Account());


        var response = mvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dataCreateAccountJson.write(dataCreateAccount).getJson()))
                        .andReturn().getResponse();

        var jsonEsperado = dataGetAccountJson.write(dataGetAccount).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DataCreateAccount getDataCreateAccount(){
        return new DataCreateAccount("1234567-8", TypeAccount.POUPANCA);
    }
}