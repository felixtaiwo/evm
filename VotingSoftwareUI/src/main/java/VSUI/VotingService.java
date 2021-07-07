package VSUI;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class VotingService {
    @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private int voteId;
    private int authCode;
    private boolean accredited=false;
    private boolean hasVoted=false;
    private LocalDateTime voteDate=null;
    private LocalTime authExp=null;

    public VotingService() {
    }

    public VotingService(int userId, int voteId) {
        this.userId = userId;
        this.voteId = voteId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

    public boolean isAccredited() {
        return accredited;
    }

    public void setAccredited(boolean accredited) {
        this.accredited = accredited;
    }

    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    public LocalTime getAuthExp() {
        return authExp;
    }

    public void setAuthExp(LocalTime authExp) {
        this.authExp = authExp;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return "VotingService{" +
                "id=" + id +
                ", userId=" + userId +
                ", voteId=" + voteId +
                ", authCode=" + authCode +
                ", accredited=" + accredited +
                ", hasVoted=" + hasVoted +
                '}';
    }
}
