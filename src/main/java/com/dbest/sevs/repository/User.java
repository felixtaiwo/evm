package com.dbest.sevs.repository;

import com.dbest.sevs.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface User extends JpaRepository<com.dbest.sevs.entity.User,Long> {
    void deleteById(long id);
    com.dbest.sevs.entity.User findByEmailAddress(String email);

    com.dbest.sevs.entity.User findByPhone(String phone);
    @Query(value = "select * from user where id in (select users_id from poll_users where poll_id=?1)",nativeQuery = true)
    List<com.dbest.sevs.entity.User> findUsers(long poll);

    List<com.dbest.sevs.entity.User> findByBlocked(boolean b);
}
