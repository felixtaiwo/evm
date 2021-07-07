package evm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BallotRepo extends JpaRepository<Ballot,Integer> {
    int countByContestant(Contestants contestant);
}
