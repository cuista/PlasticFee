package it.unical.computerscience.pfsociety.plasticfee.data.dao;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProposalDao extends JpaRepository<ProposalEntity,Long> {

    Optional<ProposalEntity> findById(Long id);
    List<ProposalEntity> findAll();
    List<ProposalEntity> findByProposalCreator(UserEntity entity);
    Optional<ProposalEntity> findByTitleEquals(String title);
    Optional<ProposalEntity> findByDescriptionEquals(String description);

}
