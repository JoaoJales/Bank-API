package bank.api.controller;

import bank.api.domain.user.DataPutPassword;
import bank.api.domain.user.DataRegisterUser;
import bank.api.domain.user.User;
import bank.api.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/password")
    public ResponseEntity alterPassword(@RequestBody @Valid DataPutPassword data){
        User user = userService.alterPassword(data);

        return ResponseEntity.ok().body(new DataRegisterUser(data.newPassword()));
    }
}
