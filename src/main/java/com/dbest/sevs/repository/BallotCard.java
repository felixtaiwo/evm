package com.dbest.sevs.repository;

import com.dbest.sevs.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BallotCard extends JpaRepository<com.dbest.sevs.entity.BallotCard,Long> {
    List<com.dbest.sevs.entity.BallotCard> findByPoll(Poll poll);
}
