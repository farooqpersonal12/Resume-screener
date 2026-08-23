package com.resumescreener.resume_screener.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumeResponseDTO {

    private Long id;
    private String fileName;
    private String extractedText;
    private String skills;
    private String experience;
    private String education;
    private LocalDateTime uploadedAt;
    private Long candidateId;
}