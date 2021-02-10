package it.unical.computerscience.pfsociety.plasticfee.data.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public UserDto createNewUser(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userDao.save(userEntity);
        return (modelMapper.map(userEntity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<UserEntity> userEntity = userDao.findById(id);
        if(userEntity.isPresent())
            return Optional.of(modelMapper.map(userEntity.get(),UserDto.class));
        throw new RuntimeException("user not exist");
    }
}
