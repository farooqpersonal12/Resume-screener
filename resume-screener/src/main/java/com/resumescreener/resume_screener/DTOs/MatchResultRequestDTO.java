package com.resumescreener.resume_screener.DTOs;

import lombok.Data;

@Data
public class MatchResultRequestDTO {
    private Long candidateId;
    private Long jobId;
}
