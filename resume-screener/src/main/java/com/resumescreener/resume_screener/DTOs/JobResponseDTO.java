package com.resumescreener.resume_screener.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private LocalDateTime createdAt;


}
