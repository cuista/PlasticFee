package it.unical.computerscience.pfsociety.plasticfee.data.dao;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface ProposalDao extends JpaRepository<ProposalEntity,Long>, JpaSpecificationExecutor<ProposalEntity> {

    Optional<ProposalEntity> findById(Long id);
    List<ProposalEntity> findAll();
    List<ProposalEntity> findByProposalCreator(UserEntity entity);
    Optional<ProposalEntity> findByTitleEquals(String title);
    Optional<ProposalEntity> findByActiveIsTrueAndTitleEquals(String title);
    Optional<ProposalEntity> findByDescriptionEquals(String description);
    List<ProposalEntity> findByActiveIsTrue();
    List<ProposalEntity> findAllByActiveIsFalse();
    List<ProposalEntity> findAllByProposalCreator_Username(String username);

    @Transactional
    @Modifying
    @Query("update ProposalEntity p set p.active = false where p.id=:id")
    void setProposalAsExpired(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update ProposalEntity p set p.expirationDate=:expirationDate where p.title=:title")
    void updateExpirationDate(@Param("expirationDate") LocalDate expirationDate,@Param("title") String title);

}
