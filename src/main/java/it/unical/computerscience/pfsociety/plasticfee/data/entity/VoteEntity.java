package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "PFE_VOTE")
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IS_IN_FAVOR")
    private Boolean isInFavor;

    @ManyToOne
    @JoinColumn(name = "PROPOSAL",referencedColumnName = "ID")
    private ProposalEntity proposal;

    @ManyToOne
    @JoinColumn(name = "USER",referencedColumnName = "ID")
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInFavor() { return isInFavor; }

    public void setInFavor(Boolean inFavor) { isInFavor = inFavor; }

    public ProposalEntity getProposal() {
        return proposal;
    }

    public void setProposal(ProposalEntity proposal) {
        this.proposal = proposal;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
