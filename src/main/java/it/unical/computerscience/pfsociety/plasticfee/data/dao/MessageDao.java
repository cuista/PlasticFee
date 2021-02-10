package it.unical.computerscience.pfsociety.plasticfee.data.dao;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<MessageEntity,String>, JpaSpecificationExecutor<MessageEntity> {

}
