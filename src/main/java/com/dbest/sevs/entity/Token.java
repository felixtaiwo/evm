package com.dbest.sevs.entity;

import javax.crypto.SealedObject;
import javax.persistence.*;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition="BLOB")
    private SealedObject sealedObject;


    public Token() {
    }
    public Token(long id) {
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
}
