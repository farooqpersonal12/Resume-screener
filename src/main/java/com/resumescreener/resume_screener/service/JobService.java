package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.JobRequestDTO;
import com.resumescreener.resume_screener.DTOs.JobResponseDTO;
import com.resumescreener.resume_screener.DTOs.JobUploadResponseDTO;
import com.resumescreener.resume_screener.entity.Job;
import com.resumescreener.resume_screener.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final PdfExtractionService pdfExtractionService;

    public JobService(
            JobRepository jobRepository,
            PdfExtractionService pdfExtractionService) {

        this.jobRepository = jobRepository;
        this.pdfExtractionService = pdfExtractionService;
    }

    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {

        Job job = new Job();

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setCreatedAt(LocalDateTime.now());

        Job savedJob =
                jobRepository.save(job);

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
                                "Job not found with id: " + id
                        )
                );

        return convertToResponseDTO(job);
    }

    private JobResponseDTO convertToResponseDTO(Job job) {

        JobResponseDTO dto =
                new JobResponseDTO();

        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setCreatedAt(job.getCreatedAt());

        return dto;
    }

    public JobUploadResponseDTO uploadJobDescription(
            MultipartFile file,
            String title) {

        if (title == null || title.isBlank()) {
            throw new RuntimeException(
                    "Job title is required"
            );
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Job description file is required"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null
                || !fileName.toLowerCase().endsWith(".pdf")) {

            throw new RuntimeException(
                    "Only PDF job description files are supported"
            );
        }

        String extractedText =
                pdfExtractionService.extractText(file);

        if (extractedText == null
                || extractedText.isBlank()) {

            throw new RuntimeException(
                    "Could not extract text from job description"
            );
        }

        Job job = new Job();

        job.setTitle(title);
        job.setDescription(extractedText);
        job.setCreatedAt(LocalDateTime.now());

        Job savedJob =
                jobRepository.save(job);

        JobUploadResponseDTO response =
                new JobUploadResponseDTO();

        response.setId(savedJob.getId());
        response.setTitle(savedJob.getTitle());
        response.setDescription(
                savedJob.getDescription()
        );

        return response;
    }
}