package com.resumescreener.resume_screener.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchResultResponseDTO {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private Double score;
    private String justification;
    private String matchedSkills;
    private String missingSkills;
    private LocalDateTime createdAt;
}
