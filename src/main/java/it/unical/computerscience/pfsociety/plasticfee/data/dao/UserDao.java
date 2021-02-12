package it.unical.computerscience.pfsociety.plasticfee.data.dao;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserEntity,String>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String sername);
    Optional<UserEntity> findByUsernameAndPassword(String username,String password);
}
