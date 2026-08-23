package com.resumescreener.resume_screener.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchResultRequestDTO {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Job ID is required")
    private Long jobId;
}
