package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.LLMMatchResponseDTO;
import com.resumescreener.resume_screener.DTOs.MatchResultRequestDTO;
import com.resumescreener.resume_screener.DTOs.MatchResultResponseDTO;
import com.resumescreener.resume_screener.DTOs.ShortlistResponseDTO;
import com.resumescreener.resume_screener.entity.Candidate;
import com.resumescreener.resume_screener.entity.Job;
import com.resumescreener.resume_screener.entity.MatchResult;
import com.resumescreener.resume_screener.entity.Resume;
import com.resumescreener.resume_screener.repository.CandidateRepository;
import com.resumescreener.resume_screener.repository.JobRepository;
import com.resumescreener.resume_screener.repository.MatchResultRepository;
import com.resumescreener.resume_screener.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchResultService {

    private final MatchResultRepository matchResultRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final LLMMatchingService llmMatchingService;

    public MatchResultService(
            MatchResultRepository matchResultRepository,
            CandidateRepository candidateRepository,
            JobRepository jobRepository,
            ResumeRepository resumeRepository,
            LLMMatchingService llmMatchingService) {

        this.matchResultRepository = matchResultRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.llmMatchingService = llmMatchingService;
    }

    public MatchResultResponseDTO createMatch(
            MatchResultRequestDTO requestDTO) {

        Candidate candidate = candidateRepository
                        .findById(requestDTO.getCandidateId())
                        .orElseThrow(
                                () ->
                                new RuntimeException(
                                        "Candidate not found with id: " + requestDTO.getCandidateId()
                                )
                        );

        Job job = jobRepository
                .findById(requestDTO.getJobId())
                .orElseThrow(
                        () -> new RuntimeException("Job not found with id: "+ requestDTO.getJobId())
                        );

        Resume resume = resumeRepository
                        .findTopByCandidateIdOrderByUploadedAtDesc(
                                candidate.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("No resume found for candidate id: " + candidate.getId())
                        );

        String resumeText = resume.getExtractedText();

        if (resumeText == null || resumeText.isBlank()) {
            throw new RuntimeException(
                    "Resume text is empty for resume id: "
                            + resume.getId()
            );
        }

        String jobDescription = job.getDescription();

        if (jobDescription == null || jobDescription.isBlank()) {

            throw new RuntimeException("Job description is empty for job id: " + job.getId());
        }

        LLMMatchResponseDTO llmResult = llmMatchingService.matchResumeWithJob(
                        resumeText,
                        jobDescription
                );

        MatchResult matchResult = new MatchResult();

        matchResult.setCandidate(candidate);
        matchResult.setJob(job);

        matchResult.setScore(
                llmResult.getScore()
        );

        matchResult.setJustification(
                llmResult.getJustification()
        );

        matchResult.setMatchedSkills(
                String.join(
                        ", ",
                        llmResult.getMatchedSkills()
                )
        );

        matchResult.setMissingSkills(
                String.join(
                        ", ",
                        llmResult.getMissingSkills()
                )
        );

        matchResult.setCreatedAt(
                LocalDateTime.now()
        );

        MatchResult savedResult =
                matchResultRepository.save(
                        matchResult
                );

        return convertToResponseDTO(
                savedResult
        );
    }

    public List<MatchResultResponseDTO> getAllMatches() {

        return matchResultRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public MatchResultResponseDTO getMatchById(Long id) {

        MatchResult matchResult = matchResultRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Match result not found with id: " + id)
                        );

        return convertToResponseDTO(matchResult);
    }

    private MatchResultResponseDTO convertToResponseDTO(MatchResult matchResult) {

        MatchResultResponseDTO dto = new MatchResultResponseDTO();

        dto.setId(matchResult.getId());

        dto.setCandidateId(
                matchResult.getCandidate().getId()
        );

        dto.setJobId(
                matchResult.getJob().getId()
        );

        dto.setScore(
                matchResult.getScore()
        );

        dto.setJustification(
                matchResult.getJustification()
        );

        dto.setMatchedSkills(
                matchResult.getMatchedSkills()
        );

        dto.setMissingSkills(
                matchResult.getMissingSkills()
        );

        dto.setCreatedAt(
                matchResult.getCreatedAt()
        );

        return dto;
    }

    public List<ShortlistResponseDTO> getShortlistedCandidates(
            Long jobId,
            Double minScore) {

        jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with id: " + jobId
                        )
                );

        List<MatchResult> results =
                matchResultRepository
                        .findByJobIdAndScoreGreaterThanEqualOrderByScoreDesc(
                                jobId,
                                minScore
                        );

        return results.stream()
                .map(this::convertToShortlistDTO)
                .toList();
    }

    private ShortlistResponseDTO convertToShortlistDTO(
            MatchResult matchResult) {

        ShortlistResponseDTO dto =
                new ShortlistResponseDTO();

        dto.setCandidateId(
                matchResult.getCandidate().getId()
        );

        dto.setCandidateName(
                matchResult.getCandidate().getName()
        );

        dto.setJobId(
                matchResult.getJob().getId()
        );

        dto.setScore(
                matchResult.getScore()
        );

        dto.setMatchedSkills(
                matchResult.getMatchedSkills()
        );

        dto.setMissingSkills(
                matchResult.getMissingSkills()
        );

        dto.setJustification(
                matchResult.getJustification()
        );

        return dto;
    }
}