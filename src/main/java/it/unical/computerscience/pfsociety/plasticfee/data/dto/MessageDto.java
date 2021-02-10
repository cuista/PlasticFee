package it.unical.computerscience.pfsociety.plasticfee.data.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDto implements Serializable {
    private Long id;
    private String content;
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
