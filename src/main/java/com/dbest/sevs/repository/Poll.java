package com.dbest.sevs.repository;

import com.dbest.sevs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Poll extends JpaRepository<com.dbest.sevs.entity.Poll,Long> {
    List<com.dbest.sevs.entity.Poll> findByIsClosed(boolean status);
    @Query(value = "select * from poll where id in (select poll_id from poll_users where users_id=?1)",nativeQuery = true)
    List<com.dbest.sevs.entity.Poll> findByUser(long user);
}
