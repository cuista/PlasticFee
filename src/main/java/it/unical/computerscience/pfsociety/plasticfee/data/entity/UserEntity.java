package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PFE_USER")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name="USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "REPUTATION")
    private int reputation;

    @OneToMany(mappedBy = "proposalCreator", cascade = CascadeType.ALL)
    private List<ProposalEntity> proposalEntityList = new ArrayList<>();

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
}
