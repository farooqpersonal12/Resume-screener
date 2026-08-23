package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.ResumeResponseDTO;
import com.resumescreener.resume_screener.entity.Candidate;
import com.resumescreener.resume_screener.entity.Resume;
import com.resumescreener.resume_screener.repository.CandidateRepository;
import com.resumescreener.resume_screener.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final CandidateRepository candidateRepository;
    private final PdfExtractionService pdfExtractionService;
    private final ResumeParsingService resumeParsingService;

    public ResumeService(
            ResumeRepository resumeRepository,
            CandidateRepository candidateRepository,
            PdfExtractionService pdfExtractionService,
            ResumeParsingService resumeParsingService) {

        this.resumeRepository = resumeRepository;
        this.candidateRepository = candidateRepository;
        this.pdfExtractionService = pdfExtractionService;
        this.resumeParsingService = resumeParsingService;
    }

    public List<ResumeResponseDTO> getAllResumes() {

        return resumeRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public ResumeResponseDTO getResumeById(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found with id: " + id));

        return convertToResponseDTO(resume);
    }

    public ResumeResponseDTO uploadResume(
            MultipartFile file,
            Long candidateId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(
                        ()->
                        new RuntimeException("Candidate not found with id: " + candidateId)
                );

        String extractedText = pdfExtractionService.extractText(file);

        String skills =
                resumeParsingService.extractSkills(extractedText);
        String experience =
                resumeParsingService.extractExperience(extractedText);
        String education =
                resumeParsingService.extractEducation(extractedText);
        Resume resume = new Resume();

        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setSkills(skills);
        resume.setSkills(skills);
        resume.setExperience(experience);
        resume.setEducation(education);
        resume.setCandidate(candidate);
        resume.setUploadedAt(LocalDateTime.now());

        Resume savedResume = resumeRepository.save(resume);

        return convertToResponseDTO(savedResume);
    }


    private ResumeResponseDTO convertToResponseDTO(Resume resume) {

        ResumeResponseDTO dto = new ResumeResponseDTO();

        dto.setId(resume.getId());
        dto.setFileName(resume.getFileName());
        dto.setExtractedText(resume.getExtractedText());
        dto.setSkills(resume.getSkills());
        dto.setExperience(resume.getExperience());
        dto.setEducation(resume.getEducation());
        dto.setUploadedAt(resume.getUploadedAt());

        if (resume.getCandidate() != null) {
            dto.setCandidateId(resume.getCandidate().getId());
        }

        return dto;
    }
}