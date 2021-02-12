package it.unical.computerscience.pfsociety.plasticfee.data.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserService {
    UserDto createNewUser(String username,String password);
    Optional<UserDto> findById(Long id);
    Optional<UserDto> findByUsername(String username);
    Optional<UserDto> verifyUserCredentials(String username,String password);
}
