package it.unical.computerscience.pfsociety.plasticfee.core.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.core.service.VoteService;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.ProposalByIdNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.ProposalByTitleNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.UserByIdNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.UserByUsernameNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.ProposalDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.VoteDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.VoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private ProposalDao proposalDao;

    @Autowired
    private UserDao userDao;

    @Override
    public boolean addVote(Boolean isInFavor, String proposalTitle, String username) {

        ProposalEntity proposalEntity = proposalDao.findByTitleEquals(proposalTitle).orElseThrow(() -> new ProposalByTitleNotFoundOnRetrieveException(proposalTitle));
        if(!proposalEntity.isActive())
            return false;

        UserEntity userEntity= userDao.findByUsername(username).orElseThrow(() -> new UserByUsernameNotFoundOnRetrieveException(username));
        if (voteDao.findByProposalAndUser(proposalEntity, userEntity).isEmpty() && proposalEntity.getProposalCreator().getId()!=userEntity.getId()) {

            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setProposal(proposalEntity);
            voteEntity.setUser(userEntity);
            voteEntity.setInFavor(isInFavor);

            voteDao.save(voteEntity);
            return true;
        }

        return false;
    }
}
