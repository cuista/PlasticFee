package it.unical.computerscience.pfsociety.plasticfee.data.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.data.dao.ProposalDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.service.ProposalService;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    public ProposalDto createProposal(String title, String description, String creatorUsername, LocalDateTime localDateTime) {

        if (proposalDao.findByTitleEquals(title).isPresent() || proposalDao.findByDescriptionEquals(description).isPresent()){
            throw new RuntimeException("A proposal with these data is already present");
        }

        UserEntity user = userDao.findByUsername(creatorUsername).get();

        ProposalEntity proposalEntity = new ProposalEntity();

        proposalEntity.setTitle(title);
        proposalEntity.setDescription(description);
        proposalEntity.setProposalCreator(user);
        proposalEntity.setCreationDateTime(localDateTime);
        proposalEntity.setActive(true);
        proposalDao.save(proposalEntity);

        return modelMapper.map(proposalEntity,ProposalDto.class);


    }

    @Override
    public void verifyProposalsExpiration() {

        List<ProposalDto> proposals = retrieveAllActiveProposals();

        for (ProposalDto p: proposals){
            if (Duration.between(p.getCreationDateTime(),LocalDateTime.now()).toSeconds()>=5){
                proposalDao.setProposalAsExpired(p.getId());
            }
        }

    }
}
