package com.resumescreener.resume_screener.repository;

import com.resumescreener.resume_screener.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findTopByCandidateIdOrderByUploadedAtDesc(
            Long candidateId
    );
}