package bank.api.controller;

import bank.api.dto.user.DataPutPassword;
import bank.api.dto.user.DataRegisterUser;
import bank.api.domain.user.User;
import bank.api.domain.user.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataPutPassword> dataPutPasswordJson;

    @Autowired
    private JacksonTester<DataRegisterUser> dataRegisterUserJson;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações de alteração de senha estiverem invalidas")
    @WithMockUser
    void alterPassword_case1() throws Exception {
        var response = mvc.perform(put("/user/password")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando informações de alteração de senha estiverem corretas")
    @WithMockUser
    void alterPassword_case2() throws Exception {
        var dataPutPassword = new DataPutPassword("11111", "22222");
        var dataRegisterUser = new DataRegisterUser("22222");

        when(userService.alterPassword(dataPutPassword)).thenReturn(new User());

        var response = mvc.perform(put("/user/password").contentType(MediaType.APPLICATION_JSON)
                .content(dataPutPasswordJson.write(dataPutPassword).getJson())).andReturn().getResponse();

        var jsonEsperado = dataRegisterUserJson.write(dataRegisterUser).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}