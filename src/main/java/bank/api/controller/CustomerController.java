package bank.api.controller;

import bank.api.domain.account.AccountService;
import bank.api.domain.account.DataCreateAccount;
import bank.api.domain.account.DataGetAccount;
import bank.api.domain.customer.*;
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
    private CustomerService customerService;

    @PutMapping
    @Transactional
    public ResponseEntity putCustomers(@RequestBody @Valid DataPutCustomer data){
       var customer  = customerService.putCustomers(data);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }


    @GetMapping
    public ResponseEntity<Page<DataGetCustomers>> getCustomers(@PageableDefault(size = 10)Pageable pageable){
        var page = customerService.getCustomers(pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable Long id){
        Customer customer = customerService.getCustomer(id);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

}
