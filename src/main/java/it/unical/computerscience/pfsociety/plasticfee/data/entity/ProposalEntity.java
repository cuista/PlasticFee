package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PFE_PROPOSAL")
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", unique = true)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATION_DATETIME")
    private LocalDateTime creationDateTime;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(name = "REPUTATION_REWARD")
    private int reputationReward;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID", referencedColumnName = "ID")
    private UserEntity proposalCreator;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<VoteEntity> votesList = new HashSet<>();

    public ProposalEntity() {
    }

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

    public UserEntity getProposalCreator() {
        return proposalCreator;
    }

    public void setProposalCreator(UserEntity proposalCreator) {
        this.proposalCreator = proposalCreator;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Set<VoteEntity> getVotesList() { return votesList; }

    public void setVotesList(Set<VoteEntity> votesList) { this.votesList = votesList; }
}
