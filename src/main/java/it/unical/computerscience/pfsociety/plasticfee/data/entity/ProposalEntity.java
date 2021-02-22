package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROPOSAL")
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATION_DATETIME")
    private LocalDateTime creationDateTime;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID",referencedColumnName = "ID")
    private UserEntity proposalCreator;

    //TODO one to many hashset utenti a favore

    //TODO one to many hashset utenti a sfavore

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
}
