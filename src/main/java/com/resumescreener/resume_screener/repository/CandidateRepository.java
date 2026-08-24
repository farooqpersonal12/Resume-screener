package com.resumescreener.resume_screener.repository;

import com.resumescreener.resume_screener.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
