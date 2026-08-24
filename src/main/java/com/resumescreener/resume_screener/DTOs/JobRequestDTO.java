package com.resumescreener.resume_screener.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobRequestDTO {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

}
