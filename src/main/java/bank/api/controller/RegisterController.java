package bank.api.controller;


import bank.api.domain.register.DataRegister;
import bank.api.domain.register.RegisterService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid DataRegister data, UriComponentsBuilder uriBuilder){
        var dataDetailingCustomer = registerService.register(data);

        var uri = uriBuilder.path("customer/{id}").buildAndExpand(dataDetailingCustomer.id()).toUri();


        return ResponseEntity.created(uri).body(dataDetailingCustomer);
    }
}
