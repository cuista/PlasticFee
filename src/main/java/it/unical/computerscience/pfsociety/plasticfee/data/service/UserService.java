package it.unical.computerscience.pfsociety.plasticfee.data.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserService {
    UserDto createNewUser(String username);
    Optional<UserDto> findById(Long id);
}
