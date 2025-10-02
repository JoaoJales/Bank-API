package bank.api.controller;

import bank.api.domain.transaction.DataDetailingTransaction;
import bank.api.domain.transaction.DataStatement;
import bank.api.domain.transaction.TransactionService;
import bank.api.domain.transaction.dtosTransactions.*;
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
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "5 - Transações", description = "Endpoints de transações")
@RestController
@RequestMapping("/transactions")
@SecurityRequirement(name = "bearer-key")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Realiza um depósito")
    @PostMapping("/deposit")
    @Transactional
    public ResponseEntity<DataDetailingTransaction> deposit(@RequestBody @Valid DataDeposit data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.deposit(data);

        var uri = uriBuilder.path("/transactions/deposit/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @Operation(summary = "Realiza uma transferência")
    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<DataDetailingTransaction> transfer(@RequestBody @Valid DataTransfer data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.transfer(data);

        var uri = uriBuilder.path("/transactions/transfer/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @Operation(summary = "Realiza um saque")
    @PostMapping("/withdrawal")
    @Transactional
    public ResponseEntity<DataDetailingTransaction> withdrawal(@RequestBody @Valid DataWithdrawal data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.withdrawal(data);

        var uri = uriBuilder.path("/transactions/withdrawal/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @Operation(summary = "Realiza um pagamento")
    @PostMapping("/payment")
    @Transactional
    public ResponseEntity<DataDetailingTransaction> payment(@RequestBody @Valid DataPayment data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.payment(data);

        var uri = uriBuilder.path("/transactions/payment/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @Operation(summary = "Realiza um Pix")
    @PostMapping("/pix")
    @Transactional
    public ResponseEntity<DataDetailingTransaction> pix(@RequestBody @Valid DataPix data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.pix(data);

        var uri = uriBuilder.path("/transactions/pix/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @Operation(summary = "Busca extrato da conta")
    @GetMapping("statement/{numeroConta}")
    public ResponseEntity<Page<DataStatement>> getStatement(@PageableDefault(size = 10)Pageable pageable, @PathVariable String numeroConta){
        var page = transactionService.getStatement(numeroConta, pageable);

        return ResponseEntity.ok(page);
    }
}
