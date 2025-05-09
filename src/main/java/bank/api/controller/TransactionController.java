package bank.api.controller;

import bank.api.domain.transaction.DataDetailingTransaction;
import bank.api.domain.transaction.TransactionService;
import bank.api.domain.transaction.dtosTransactions.DataDeposit;
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
}
