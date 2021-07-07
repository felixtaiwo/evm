package VSUI;


import java.util.List;


public class Vote {

    private int voteId;
    private String title;
    private List<Contestants> contestants;
    private Contestants winner =null;
    private boolean registrationON=false;
    private boolean voteON=false;

    public Vote() {
    }

    public Vote(int voteId, String title, List<Contestants> contestants) {
        this.voteId = voteId;
        this.title = title;
        this.contestants = contestants;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Contestants> getContestants() {
        return contestants;
    }

    public void setContestants(List<Contestants> contestants) {
        this.contestants = contestants;
    }

    public Contestants getWinner() {
        return winner;
    }

    public void setWinner(Contestants win) {
        winner=win;
    }

    public boolean isRegistrationON() {
        return registrationON;
    }

    public void setRegistrationON(boolean registrationON) {
        this.registrationON = registrationON;
    }

    public boolean isVoteON() {
        return voteON;
    }

    public void setVoteON(boolean voteON) {
        this.voteON = voteON;
    }
}
