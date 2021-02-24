package it.unical.computerscience.pfsociety.plasticfee.core.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProposalService {

    List<ProposalDto> retrieveAllActiveProposals();
    ProposalDto createProposal(String title, String description, String creatorUsername, LocalDateTime creationDateTime,
                               LocalDate expirationDate, int reputationReward);
    void verifyProposalsExpirationAndUpdateIfNeeded();
    ProposalDto retrieveActiveProposalByTitle(String title);
}
