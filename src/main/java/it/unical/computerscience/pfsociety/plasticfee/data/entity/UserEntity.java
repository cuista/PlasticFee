package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.Proposal;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="PFE_USER")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "REPUTATION")
    private int reputation;

    @Column(name="USERNAME", unique = true)
    private String username;

    @OneToMany(mappedBy = "proposalCreator", cascade = CascadeType.ALL)
    private List<ProposalEntity> proposalList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<VoteEntity> votes=new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public List<ProposalEntity> getProposalList() { return proposalList; }

    public void setProposalEntityList(List<ProposalEntity> proposalList) { this.proposalList = proposalList; }

    public Set<VoteEntity> getVotes() { return votes; }

    public void setVotes(Set<VoteEntity> votes) { this.votes = votes; }
}
