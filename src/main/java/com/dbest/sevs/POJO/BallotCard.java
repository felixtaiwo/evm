package com.dbest.sevs.POJO;

import java.io.Serializable;
import java.util.Date;

public class BallotCard implements Serializable {
    private Date date;
    private long contestantId;
    private boolean isSigned;


    public BallotCard(Date date, long contestantId, boolean isSigned) {
        this.date = date;
        this.contestantId = contestantId;
        this.isSigned = isSigned;
    }

    public BallotCard(boolean isSigned) {
        this.isSigned = isSigned;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getContestantId() {
        return contestantId;
    }

    public void setContestantId(long contestantId) {
        this.contestantId = contestantId;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    @Override
    public String toString() {
        return "BallotCard{" +
                "date=" + date +
                ", contestantId=" + contestantId +
                ", isSigned=" + isSigned +
                '}';
    }
}
