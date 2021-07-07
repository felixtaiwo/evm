package evm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepo extends JpaRepository<Vote,Integer> {
    Vote findByVoteId(int id);

}
