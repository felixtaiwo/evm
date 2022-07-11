package com.dbest.sevs.POJO;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {
    private String value;
    private Date expires;


    public Token(String value, Date expires) {
        this.value = value;
        this.expires = expires;
    }

    public Token() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public boolean isExpired(){
        return new Date().after(expires);
    }
}
