package evm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private VoteRepo voteRepo;
    @Autowired
    private BallotRepo ballotRepo;
    @Autowired
    private ContestantRepo contestantRepo;
    public void newVote(Vote vote){
    voteRepo.save(vote);
}
    public void newContestant(Contestants contestant){
        contestantRepo.save(contestant);
}
    public Vote getVote(int voteId){
        return voteRepo.findByVoteId(voteId);
}
    public Contestants getContestant(int contestantId){
        return contestantRepo.findByCid(contestantId);

}

    public void deleteContestant(int cid) {
        contestantRepo.deleteByCid(cid);
    }

    public List<Vote> getAllVotes() {
        return voteRepo.findAll();
    }
    public void ballot(Ballot ballot){
        ballotRepo.save(ballot);
    }

    public int voteCount(Contestants id) {
        return ballotRepo.countByContestant(id);
    }
}
