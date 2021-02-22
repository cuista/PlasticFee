package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import it.unical.computerscience.pfsociety.plasticfee.data.entity.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProposalDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime creationDateTime;

    private boolean active;

    private UserEntity creator;

    public ProposalDto() {

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

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }
}
