package it.unical.computerscience.pfsociety.plasticfee.core.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ProposalService {

    List<ProposalDto> retrieveAllActiveProposals();
    ProposalDto createProposal(String title, String description, String creatorUsername, LocalDateTime creationDateTime);
    void verifyProposalsExpiration(); //FIXME OSSESSIVO COMPULSIVO: add AndUpdateIfNeeded to method's name
    ProposalDto retrieveActiveProposalByTitle(String title);
}
