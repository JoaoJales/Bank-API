package bank.api.controller;

import bank.api.domain.account.AccountService;
import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.account.DataGetAccount;
import bank.api.domain.customer.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "4 - Contas", description = "Endpoints de contas")
@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "bearer-key")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerRepository customerRepository;

    @Operation(summary = "Cria uma nova conta")
    @PostMapping
    @Transactional
    public ResponseEntity postAccounts(@RequestBody @Valid DataCreateAccount data, UriComponentsBuilder uriBuilder){
        var account = accountService.createNewAccount(data);

        var uri = uriBuilder.path("accounts/{accountId}").buildAndExpand(account.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataGetAccount(account));
    }

    @Operation(summary = "Desativa uma conta")
    @DeleteMapping("/{numeroConta}")
    @Transactional
    public ResponseEntity cancelAccounts(@PathVariable String numeroConta){
        accountService.cancelAccount(numeroConta);

        return ResponseEntity.noContent().build();
    }
}

