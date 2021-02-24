package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.VoteEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ProposalDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime creationDateTime;

    private boolean active;

    private LocalDate expirationDate;

    private int reputationReward;

    private UserDto creator;

    private Set<VoteDto> votesList = new HashSet<>();

    public ProposalDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getReputationReward() {
        return reputationReward;
    }

    public void setReputationReward(int reputationReward) {
        this.reputationReward = reputationReward;
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public Set<VoteDto> getVotesList() { return votesList; }

    public void setVotesList(Set<VoteDto> votesList) {
        this.votesList = votesList;
    }

    public Set<VoteDto> getVotesInFavorList() {
        Set<VoteDto> votesInFavor=new HashSet<>();
        for (VoteDto voteDto:votesList) {
            if(voteDto.getInFavor())
                votesInFavor.add(voteDto);
        }
        return votesInFavor;
    }

    public Set<VoteDto> getVotesAgainstList() {
        Set<VoteDto> votesAgainst=new HashSet<>();
        for (VoteDto voteDto:votesList)
        {
            if(!voteDto.getInFavor())
                votesAgainst.add(voteDto);
        }
        return votesAgainst;
    }
}
