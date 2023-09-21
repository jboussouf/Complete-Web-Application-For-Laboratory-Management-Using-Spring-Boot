package com.jboussouf.labmanagement.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Project implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String title;
    @Column(length = 100000)
    private String imageHeader;
    @Column(length = 250)
    private String header;
    @Column(length = 1000000000)
    private String body;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AcceptedUsers> writer = new ArrayList<>();

    public Project() {
    }

    public Project(String title, String imageHeader, String header, String body, Collection<AcceptedUsers> writer) {
        this.title = title;
        this.imageHeader = imageHeader;
        this.header = header;
        this.body = body;
        this.writer = writer;
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

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Collection<AcceptedUsers> getWriter() {
        return writer;
    }

    public void setWriter(Collection<AcceptedUsers> writer) {
        this.writer = writer;
    }
}
