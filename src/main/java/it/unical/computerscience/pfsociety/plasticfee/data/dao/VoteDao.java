package it.unical.computerscience.pfsociety.plasticfee.data.dao;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteDao extends JpaRepository<VoteEntity,Long>, JpaSpecificationExecutor<VoteEntity> {

    Optional<VoteEntity> findByProposalAndUser(ProposalEntity proposalId, UserEntity userId);
}
