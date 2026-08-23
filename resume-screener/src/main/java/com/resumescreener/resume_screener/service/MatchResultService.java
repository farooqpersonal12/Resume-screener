package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.MatchResultRequestDTO;
import com.resumescreener.resume_screener.DTOs.MatchResultResponseDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchResultService {
    private final MatchResultRepository matchResultRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;

    public MatchResultService(
            MatchResultRepository matchResultRepository,
            CandidateRepository candidateRepository,
            JobRepository jobRepository,
            ResumeRepository resumeRepository) {

        this.matchResultRepository = matchResultRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
    }

    public MatchResultResponseDTO createMatch(
            MatchResultRequestDTO requestDTO) {

        Candidate candidate = candidateRepository
                .findById(requestDTO.getCandidateId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Candidate not found with id: "
                                        + requestDTO.getCandidateId()));

        Job job = jobRepository
                .findById(requestDTO.getJobId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with id: "
                                        + requestDTO.getJobId()));

        List<Resume> resumes =
                resumeRepository.findAll()
                        .stream()
                        .filter(resume ->
                                resume.getCandidate() != null
                                        && resume.getCandidate()
                                        .getId()
                                        .equals(candidate.getId()))
                        .toList();

        if (resumes.isEmpty()) {
            throw new RuntimeException(
                    "No resume found for candidate id: "
                            + candidate.getId());
        }

        Resume resume = resumes.get(resumes.size() - 1);

        String candidateSkills = resume.getSkills();
        String requiredSkills = job.getRequiredSkills();

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        if (requiredSkills != null && !requiredSkills.isBlank()) {

            String[] jobSkills =
                    requiredSkills.split(",");

            for (String jobSkill : jobSkills) {

                String requiredSkill =
                        jobSkill.trim();

                if (requiredSkill.isBlank()) {
                    continue;
                }

                if (containsSkill(candidateSkills, requiredSkill)){

                    matchedSkills.add(requiredSkill);

                } else {

                    missingSkills.add(requiredSkill);
                }
            }
        }

        double score = 0.0;

        if (!matchedSkills.isEmpty()
                || !missingSkills.isEmpty()) {

            score = ((double) matchedSkills.size()
                    / (matchedSkills.size()
                    + missingSkills.size())) * 10;
        }

        String justification =
                "Candidate matched "
                        + matchedSkills.size()
                        + " out of "
                        + (matchedSkills.size()
                        + missingSkills.size())
                        + " required skills.";

        MatchResult matchResult = new MatchResult();

        matchResult.setCandidate(candidate);
        matchResult.setJob(job);
        matchResult.setScore(score);
        matchResult.setMatchedSkills(
                String.join(", ", matchedSkills));
        matchResult.setMissingSkills(
                String.join(", ", missingSkills));
        matchResult.setJustification(justification);
        matchResult.setCreatedAt(LocalDateTime.now());

        MatchResult savedResult =
                matchResultRepository.save(matchResult);

        return convertToResponseDTO(savedResult);
    }

    public List<MatchResultResponseDTO> getAllMatches() {

        return matchResultRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public MatchResultResponseDTO getMatchById(Long id) {

        MatchResult matchResult =
                matchResultRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Match result not found with id: "
                                                + id));

        return convertToResponseDTO(matchResult);
    }

    private MatchResultResponseDTO convertToResponseDTO(
            MatchResult matchResult) {

        MatchResultResponseDTO dto =
                new MatchResultResponseDTO();

        dto.setId(matchResult.getId());
        dto.setCandidateId(
                matchResult.getCandidate().getId());
        dto.setJobId(
                matchResult.getJob().getId());
        dto.setScore(matchResult.getScore());
        dto.setJustification(
                matchResult.getJustification());
        dto.setMatchedSkills(
                matchResult.getMatchedSkills());
        dto.setMissingSkills(
                matchResult.getMissingSkills());
        dto.setCreatedAt(
                matchResult.getCreatedAt());

        return dto;
    }

    private boolean containsSkill(String candidateSkills, String requiredSkill) {

        if (candidateSkills == null || candidateSkills.isBlank()) {
            return false;
        }

        String regex = "(?i)(?<![a-zA-Z0-9])"
                + java.util.regex.Pattern.quote(requiredSkill)
                + "(?![a-zA-Z0-9])";

        return java.util.regex.Pattern
                .compile(regex)
                .matcher(candidateSkills)
                .find();
    }


}
