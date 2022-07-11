package com.dbest.sevs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date dateCreated;
    private Date dateModified;
    private String title;
    private Date openingDate;
    private Date closingDate;
    @OneToMany(mappedBy = "poll",fetch = FetchType.EAGER)
    private List<Candidate> candidates;
    @OneToOne
    private Candidate winner;
    @ManyToMany
    private List<User> users;
    private boolean isClosed=false;
    @OneToMany(mappedBy = "poll")
    private List<RecordTracker> recordTrackers;

    public Poll() {
    }
    public Poll(long id) {
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public Candidate getWinner() {
        return winner;
    }

    public void setWinner(Candidate winner) {
        this.winner = winner;
    }
    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
    @JsonIgnore
    public List<RecordTracker> getRecordTrackers() {
        return recordTrackers;
    }

    public void setRecordTrackers(List<RecordTracker> recordTrackers) {
        this.recordTrackers = recordTrackers;
    }
}
