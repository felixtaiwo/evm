package VSUI;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private VotingServiceRepo votingServiceRepo;
    public void add(VotingService votingService){
        votingServiceRepo.save(votingService);

    }
    public List<VotingService> findByUserId(int userId){
        return votingServiceRepo.findByUserId(userId);
    }
    public VotingService getVotingService(int id){
        return votingServiceRepo.findById(id);
    }

    public List<VotingService> findAll() {
        return votingServiceRepo.findAll();
    }

    public List<VotingService> findByVoteId(int voteId) {
        return votingServiceRepo.findByVoteId(voteId);
    }
}
