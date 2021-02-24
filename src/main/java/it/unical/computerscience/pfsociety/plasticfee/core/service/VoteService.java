package it.unical.computerscience.pfsociety.plasticfee.core.service;

import it.unical.computerscience.pfsociety.plasticfee.data.dto.VoteDto;

import java.util.List;
import java.util.Optional;

public interface VoteService {
    boolean addVote(Boolean isInFavor, String proposalTitle, String username);
    List<VoteDto> retrieveAllVotes();
}
