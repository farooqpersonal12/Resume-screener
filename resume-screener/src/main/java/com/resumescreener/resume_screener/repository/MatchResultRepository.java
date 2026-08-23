package com.resumescreener.resume_screener.repository;

import com.resumescreener.resume_screener.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
