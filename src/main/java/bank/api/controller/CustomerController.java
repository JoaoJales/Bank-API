package bank.api.controller;

import bank.api.domain.customer.*;
import bank.api.dto.customer.DataDetailingCustomer;
import bank.api.dto.customer.DataGetCustomers;
import bank.api.dto.customer.DataPutCustomer;
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
    public ResponseEntity<DataDetailingCustomer> putCustomers(@RequestBody @Valid DataPutCustomer data){
        var customer  = customerService.putCustomers(data);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }


    @Operation(summary = "Busca todos os clientes")
    @GetMapping
    public ResponseEntity<Page<DataGetCustomers>> getCustomers(@PageableDefault(size = 10)Pageable pageable){
        var page = customerService.getCustomers(pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Busca detalhes do cliente logado")
    @GetMapping("/me")
    public ResponseEntity<DataDetailingCustomer> getCustomerLogged(){
        Customer customer = customerService.getCustomerLogged();

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

    @Operation(summary = "Busca detalhes de um cliente pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<DataDetailingCustomer> getCustomer(@PathVariable Long id){
        Customer customer = customerService.getCustomer(id);

        return ResponseEntity.ok().body(new DataDetailingCustomer(customer));
    }

}
