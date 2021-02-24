package it.unical.computerscience.pfsociety.plasticfee.core.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.ProposalByTitleNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.ProposalDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.VoteDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.core.service.ProposalService;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.VoteEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    UserDao userDao;

    @Autowired
    ProposalDao proposalDao;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ProposalDto> retrieveAllActiveProposals() {

        List<ProposalEntity> proposals = proposalDao.findByActiveIsTrue();

        return proposals.stream().map(proposal -> modelMapper.map(proposal,ProposalDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProposalDto retrieveActiveProposalByTitle(String title) {
        return modelMapper.map(proposalDao.findByActiveIsTrueAndTitleEquals(title).orElseThrow(() -> new ProposalByTitleNotFoundOnRetrieveException(title)),ProposalDto.class);
    }

    @Override
    public ProposalDto createProposal(String title, String description, String creatorUsername, LocalDateTime creationDateTime,
                                      LocalDate expirationDate, int reputationReward) {

        if (proposalDao.findByTitleEquals(title).isPresent() || proposalDao.findByDescriptionEquals(description).isPresent()){
            throw new RuntimeException("A proposal with these data is already present");
        }

        UserEntity user = userDao.findByUsername(creatorUsername).get();

        ProposalEntity proposalEntity = new ProposalEntity();

        proposalEntity.setTitle(title);
        proposalEntity.setDescription(description);
        proposalEntity.setProposalCreator(user);
        proposalEntity.setCreationDateTime(creationDateTime);
        proposalEntity.setReputationReward(reputationReward);
        proposalEntity.setExpirationDate(expirationDate);
        proposalEntity.setActive(true);
        proposalDao.save(proposalEntity);

        return modelMapper.map(proposalEntity,ProposalDto.class);


    }

    @Override
    public void verifyProposalsExpirationAndUpdateIfNeeded() {

        List<ProposalDto> proposals = retrieveAllActiveProposals();

        for (ProposalDto proposalDto: proposals){
            if (proposalDto.getExpirationDate().compareTo(LocalDate.now())<0){
                proposalDao.setProposalAsExpired(proposalDto.getId());

                /*for (VoteDto voteDto: proposalDto.getVotesList()){

                    UserEntity userEntity = userDao.findById(voteDto.getUser().getId()).get(); //FIXME gestire se non esiste piu' l'utente


                }*/
            }
        }

    }


}
