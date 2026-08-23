package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.JobRequestDTO;
import com.resumescreener.resume_screener.DTOs.JobResponseDTO;
import com.resumescreener.resume_screener.entity.Job;
import com.resumescreener.resume_screener.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {
        Job job = new Job();

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setRequiredSkills(jobRequestDTO.getRequiredSkills());
        job.setCreatedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);

        return convertToResponseDTO(savedJob);
    }

    public List<JobResponseDTO> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public JobResponseDTO getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with id: " + id));

        return convertToResponseDTO(job);
    }

    private JobResponseDTO convertToResponseDTO(Job job) {

        JobResponseDTO dto = new JobResponseDTO();

        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setCreatedAt(job.getCreatedAt());

        return dto;
    }


}
