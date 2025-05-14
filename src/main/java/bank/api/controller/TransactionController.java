package bank.api.controller;

import bank.api.domain.transaction.DataDetailingTransaction;
import bank.api.domain.transaction.DataStatement;
import bank.api.domain.transaction.TransactionService;
import bank.api.domain.transaction.dtosTransactions.*;
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
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    @Transactional
    public ResponseEntity deposit(@RequestBody @Valid DataDeposit data, UriComponentsBuilder uriBuilder){
        var transaction = transactionService.deposit(data);

        var uri = uriBuilder.path("/transactions/deposit/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @PostMapping("/transfer/{cpf}")
    @Transactional
    public ResponseEntity transfer(@RequestBody @Valid DataTransfer data, UriComponentsBuilder uriBuilder, @PathVariable String cpf){
        var transaction = transactionService.transfer(data, cpf);

        var uri = uriBuilder.path("/transactions/transfer/{cpf}/{id}").buildAndExpand(cpf ,transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @PostMapping("/withdrawal/{cpf}")
    @Transactional
    public ResponseEntity withdrawal(@RequestBody @Valid DataWithdrawal data, UriComponentsBuilder uriBuilder, @PathVariable String cpf){
        var transaction = transactionService.withdrawal(data, cpf);

        var uri = uriBuilder.path("/transactions/withdrawal/{cpf}/{id}").buildAndExpand(cpf, transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @PostMapping("/payment/{cpf}")
    @Transactional
    public ResponseEntity withdrawal(@RequestBody @Valid DataPayment data, UriComponentsBuilder uriBuilder, @PathVariable String cpf){
        var transaction = transactionService.payment(data, cpf);

        var uri = uriBuilder.path("/transactions/payment/{cpf}/{id}").buildAndExpand(cpf, transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @PostMapping("/pix/{cpfOrigin}")
    @Transactional
    public ResponseEntity pix(@RequestBody @Valid DataPix data, UriComponentsBuilder uriBuilder, @PathVariable String cpfOrigin){
        var transaction = transactionService.pix(data,cpfOrigin);

        var uri = uriBuilder.path("/transactions/pix/{cpfOrigin}/{id}").buildAndExpand(cpfOrigin, transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(new DataDetailingTransaction(transaction));
    }

    @GetMapping("statement/{numeroConta}")
    public ResponseEntity<Page<DataStatement>> getStatement(@PathVariable String numeroConta, @PageableDefault(size = 10)Pageable pageable){
        var page = transactionService.getStatement(numeroConta, pageable);

        return ResponseEntity.ok(page);
    }
}
