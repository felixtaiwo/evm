package com.dbest.sevs.entity;



import javax.crypto.SealedObject;
import javax.persistence.*;

@Entity
public class BallotCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition="BLOB")
    private SealedObject sealedObject;
    @ManyToOne
    private Poll poll;

    public BallotCard() {
    }
    public BallotCard(long id) {
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SealedObject getSealedObject() {
        return sealedObject;
    }

    public void setSealedObject(SealedObject sealedObject) {
        this.sealedObject = sealedObject;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
