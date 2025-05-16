package bank.api.infra.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public String getCpfUserLogged(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
