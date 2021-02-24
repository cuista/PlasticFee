package it.unical.computerscience.pfsociety.plasticfee.core.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.UserByUsernameNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.core.service.UserService;
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

    @Override
    public UserDto createNewUser(String username,String password) {
        if(userExists(username))
            throw new RuntimeException();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setReputation(0);
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

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<UserEntity> userEntity = userDao.findByUsername(username);
        if(userEntity.isPresent())
            return Optional.of(modelMapper.map(userEntity.get(),UserDto.class));
        throw new RuntimeException("user not exist");
    }

    @Override
    public Optional<UserDto> verifyUserCredentials(String username, String password) {
        Optional<UserEntity> userEntity = userDao.findByUsernameAndPassword(username,password);
        if (userEntity.isPresent())
            return Optional.of(modelMapper.map(userEntity.get(),UserDto.class));
        throw new RuntimeException("user not exist");
    }

    @Override
    public void updateUserReputation(String username, int reputationReward) {

        UserEntity userEntity = userDao.findByUsername(username).orElseThrow(() -> new UserByUsernameNotFoundOnRetrieveException(username));

        userEntity.setReputation(userEntity.getReputation()+reputationReward);

        userDao.save(userEntity);

    }

    @Override
    public boolean userExists(String username) {
        Optional<UserEntity> userEntity = userDao.findByUsername(username);

        if (userEntity.isPresent()){
            return true;
        }
        return false;
    }
}
