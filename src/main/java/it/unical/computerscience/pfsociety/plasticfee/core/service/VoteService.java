package it.unical.computerscience.pfsociety.plasticfee.core.service;

public interface VoteService {
    boolean addVote(Boolean isInFavor, String proposalTitle, String username);
}
