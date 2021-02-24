package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

    private Long id;

    private String username;

    private int reputation;

    public UserDto(){}

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

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
