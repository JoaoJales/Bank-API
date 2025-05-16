package bank.api.controller;

import bank.api.domain.account.AccountService;
import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.account.DataGetAccount;
import bank.api.domain.customer.CustomerRepository;
import bank.api.domain.customer.DataDetailingCustomer;
import bank.api.domain.customer.DataGetCustomers;
import bank.api.domain.customer.DataPutCustomer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private AccountService accountService;

    @PutMapping
    @Transactional
    public ResponseEntity putCustomers(@RequestBody @Valid DataPutCustomer data){
        var customer = repository.findById(data.id()).get();
        customer.updateInfo(data);


        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

    @PostMapping("/accounts")
    @Transactional
    public ResponseEntity postAccounts(@RequestBody @Valid DataCreateAccount data, UriComponentsBuilder uriBuilder){
        var account = accountService.createNewAccount(data);
        var customer = repository.getReferenceById(account.getCustomer().getId());

        var uri = uriBuilder.path("/customers/{customerId}/accounts/{accountId}").buildAndExpand(customer.getId(), account.getId()).toUri();


        return ResponseEntity.created(uri).body(new DataGetAccount(account));
    }

    @GetMapping
    public ResponseEntity<Page<DataGetCustomers>> getCustomers(@PageableDefault(size = 10)Pageable pageable){
        var page = repository.findAll(pageable)
                .map(DataGetCustomers::new);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable Long id){
        var customer = repository.getReferenceById(id);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

    @DeleteMapping("/accounts/{numeroConta}")
    @Transactional
    public ResponseEntity cancelAccounts(@PathVariable String numeroConta){
        accountService.cancelAccount(numeroConta);

        return ResponseEntity.noContent().build();
    }
}
