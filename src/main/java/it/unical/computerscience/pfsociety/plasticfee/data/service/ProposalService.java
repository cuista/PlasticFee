package it.unical.computerscience.pfsociety.plasticfee.data.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;

import java.util.List;

public interface ProposalService {

    List<ProposalDto> retrieveAllProposals();
    ProposalDto createProposal(String title, String description, String creatorUsername);

}
