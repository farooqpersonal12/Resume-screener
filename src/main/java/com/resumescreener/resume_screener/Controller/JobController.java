package com.resumescreener.resume_screener.Controller;

import com.resumescreener.resume_screener.DTOs.JobRequestDTO;
import com.resumescreener.resume_screener.DTOs.JobResponseDTO;
import com.resumescreener.resume_screener.DTOs.JobUploadResponseDTO;
import com.resumescreener.resume_screener.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(
            @RequestBody JobRequestDTO dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {

        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    @PostMapping("/upload")
    public JobUploadResponseDTO uploadJobDescription(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {

        return jobService.uploadJobDescription(
                file,
                title
        );
    }

}
