package it.unical.computerscience.pfsociety.plasticfee.data.service.impl;

import it.unical.computerscience.pfsociety.plasticfee.data.dao.ProposalDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dao.UserDao;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.service.ProposalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ProposalDto> retrieveAllProposals() {

        List<ProposalEntity> proposals = proposalDao.findAll();

        return proposals.stream().map(proposal -> modelMapper.map(proposal,ProposalDto.class)).collect(Collectors.toList());

    }

    @Override
    public ProposalDto createProposal(String title, String description, String creatorUsername) {

        if (proposalDao.findByTitleEquals(title).isPresent() || proposalDao.findByDescriptionEquals(description).isPresent()){
            throw new RuntimeException("A proposal with these data is already present");
        }

        UserEntity user = userDao.findByUsername(creatorUsername).get();

        ProposalEntity proposalEntity = new ProposalEntity();

        proposalEntity.setTitle(title);
        proposalEntity.setDescription(description);
        proposalEntity.setProposalCreator(user);
        proposalDao.save(proposalEntity);

        return modelMapper.map(proposalEntity,ProposalDto.class);


    }
}
