package com.resumescreener.resume_screener.repository;

import com.resumescreener.resume_screener.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
