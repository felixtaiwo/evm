package evm;

import javax.persistence.*;
import java.util.List;

@Entity
public class Contestants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    private String fullname;
    private int voteCount;
    @ManyToOne
    private Vote vote;
    @OneToMany(mappedBy = "contestant")
    private List<Ballot> ballot;

    public Contestants() {
    }

    public Contestants(int cid, String fullname, int voteCount, int voteId) {
        this.cid=cid;
        this.fullname = fullname;
        this.voteCount = voteCount;
        this.vote=new Vote(voteId,"",null);
    }


    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }


    public void setVote(Vote vote) {
        this.vote = vote;
    }


}
