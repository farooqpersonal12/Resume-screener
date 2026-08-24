package com.resumescreener.resume_screener.repository;

import com.resumescreener.resume_screener.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
