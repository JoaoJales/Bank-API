package bank.api.controller;


import bank.api.domain.register.DataRegister;
import bank.api.domain.register.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "1 - Cadastro", description = "Endpoint de registro")
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @Operation(summary = "Realiza cadastro")
    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid DataRegister data, UriComponentsBuilder uriBuilder){
        var dataDetailingCustomer = registerService.register(data);

        var uri = uriBuilder.path("customer/{id}").buildAndExpand(dataDetailingCustomer.id()).toUri();


        return ResponseEntity.created(uri).body(dataDetailingCustomer);
    }
}
