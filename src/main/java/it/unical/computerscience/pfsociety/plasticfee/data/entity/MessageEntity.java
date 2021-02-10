package it.unical.computerscience.pfsociety.plasticfee.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="PFE_MESSAGE")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name="CONTENT")
    private String content;

    @Column(name="TIMESTAMP")
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
