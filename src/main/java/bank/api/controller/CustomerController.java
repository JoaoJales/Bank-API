package bank.api.controller;

import bank.api.domain.customer.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3 - Cliente", description = "Endpoints de Cliente")
@RestController
@RequestMapping("/customers")
@SecurityRequirement(name = "bearer-key")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Atualiza dados do cliente")
    @PutMapping
    @Transactional
    public ResponseEntity putCustomers(@RequestBody @Valid DataPutCustomer data){
        var customer  = customerService.putCustomers(data);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }


    @Operation(summary = "Busca clientes")
    @GetMapping
    public ResponseEntity<Page<DataGetCustomers>> getCustomers(@PageableDefault(size = 10)Pageable pageable){
        var page = customerService.getCustomers(pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Busca um cliente")
    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable Long id){
        Customer customer = customerService.getCustomer(id);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

}
