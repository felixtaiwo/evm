package com.dbest.sevs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Audit extends JpaRepository<com.dbest.sevs.entity.Audit,Long> {
}
