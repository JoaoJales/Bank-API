package bank.api.controller;

import bank.api.domain.user.DataAuthorizationUser;
import bank.api.domain.user.User;
import bank.api.infra.security.DataTokenJWT;
import bank.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid DataAuthorizationUser data){
         var authenticationToken = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());

         var authentication = manager.authenticate(authenticationToken);

         var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

         return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
    }
}
