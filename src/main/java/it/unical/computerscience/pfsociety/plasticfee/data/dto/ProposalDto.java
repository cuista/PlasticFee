package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProposalDto implements Serializable {

    private String title;

    private String description;

    private Timestamp creationTimestamp;

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

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }
}
