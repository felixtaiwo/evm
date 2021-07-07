package evm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestantRepo extends JpaRepository<Contestants,Integer> {
    Contestants findByCid(int cid);
    Contestants deleteByCid(int cid);
}
