package com.dbest.sevs.repository;

import com.dbest.sevs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordTracker extends JpaRepository<com.dbest.sevs.entity.RecordTracker,Long> {

    List<com.dbest.sevs.entity.RecordTracker> findByUser(User user);
}
