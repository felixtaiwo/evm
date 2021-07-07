package VSUI;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotingServiceRepo extends JpaRepository<VotingService,Integer> {
    List<VotingService> findByUserId(int userId);
    VotingService findById(int id);
    List<VotingService> findByVoteId(int id);
}
