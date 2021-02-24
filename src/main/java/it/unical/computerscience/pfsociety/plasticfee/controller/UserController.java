package it.unical.computerscience.pfsociety.plasticfee.controller;

import it.unical.computerscience.pfsociety.plasticfee.core.service.UserService;
import it.unical.computerscience.pfsociety.plasticfee.core.service.VoteService;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.VoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.verifyUserCredentials(username,password).get()); //FIXME if user not found
    }

    /*
    @GetMapping("/logout")
    public ResponseEntity<UserDto> logout(@RequestParam String username, @RequestParam String password) {

    }
    */

}
