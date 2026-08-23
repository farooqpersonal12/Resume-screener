package com.resumescreener.resume_screener.Controller;

import com.resumescreener.resume_screener.DTOs.ResumeResponseDTO;
import com.resumescreener.resume_screener.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponseDTO>> getAllResumes() {

        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponseDTO> getResumeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(resumeService.getResumeById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponseDTO> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateId") Long candidateId) {
        ResumeResponseDTO resumeResponseDTO = resumeService.uploadResume(file,candidateId);
        return ResponseEntity.ok(resumeResponseDTO);
    }
}