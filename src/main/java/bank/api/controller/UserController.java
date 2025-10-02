package bank.api.controller;

import bank.api.dto.user.DataPutPassword;
import bank.api.dto.user.DataRegisterUser;
import bank.api.domain.user.User;
import bank.api.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "6 - Usuário", description = "Endpoint de usuário")
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Atualiza senha")
    @PutMapping("/password")
    public ResponseEntity alterPassword(@RequestBody @Valid DataPutPassword data){
        User user = userService.alterPassword(data);

        return ResponseEntity.ok().body(new DataRegisterUser(data.newPassword()));
    }
}
