package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;

public class VoteDto {

    private Long id;

    private Boolean isInFavor;

    private ProposalDto proposal;

    private UserDto user;

    public VoteDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInFavor() { return isInFavor; }

    public void setInFavor(Boolean inFavor) { isInFavor = inFavor; }

    public ProposalDto getProposal() {
        return proposal;
    }

    public void setProposal(ProposalDto proposal) { this.proposal = proposal; }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
