package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;

import java.io.Serializable;

public class ProposalDto implements Serializable {

    private String title;

    private String description;

    private UserEntity creator;

    public ProposalDto() {

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

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }
}
