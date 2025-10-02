package bank.api.domain.customer;

import bank.api.dto.customer.DataGetCustomers;
import bank.api.dto.customer.DataPutCustomer;
import bank.api.infra.security.SecurityService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private SecurityService securityService;

    @Transactional
    public Customer putCustomers(@Valid DataPutCustomer data) {
        var cpfLogged = securityService.getCpfUserLogged();

        var customer = repository.findByCpf(cpfLogged).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        if (!customer.getId().equals(data.id())){
            throw new IllegalArgumentException("Você não tem permissão de atualizar dados desse usuário");
        }
        customer.updateInfo(data);
        repository.save(customer);
        return customer;
    }

    public Page<DataGetCustomers> getCustomers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(DataGetCustomers::new);
    }

    public Customer getCustomer(Long id) {
        return repository.getReferenceById(id);
    }
}
