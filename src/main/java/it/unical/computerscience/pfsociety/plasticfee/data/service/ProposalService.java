package it.unical.computerscience.pfsociety.plasticfee.data.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ProposalService {

    List<ProposalDto> retrieveAllActiveProposals();
    ProposalDto createProposal(String title, String description, String creatorUsername, LocalDateTime creationDateTime);
    void verifyProposalsExpiration();

}
