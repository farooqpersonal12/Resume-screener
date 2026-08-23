package com.resumescreener.resume_screener.service;

import com.resumescreener.resume_screener.DTOs.CandidateRequestDTO;
import com.resumescreener.resume_screener.DTOs.CandidateResponseDTO;
import com.resumescreener.resume_screener.entity.Candidate;
import com.resumescreener.resume_screener.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    private CandidateRepository candidateRepository;
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO) {
        Candidate candidate = new Candidate();
        candidate.setName(candidateRequestDTO.getName());
        candidate.setEmail(candidateRequestDTO.getEmail());
        candidate.setPhone(candidateRequestDTO.getPhone());

        Candidate savedCandidate = candidateRepository.save(candidate);

        return  new CandidateResponseDTO(
                savedCandidate.getId(),
                savedCandidate.getName(),
                savedCandidate.getEmail(),
                savedCandidate.getPhone()
        );
    }

    public List<CandidateResponseDTO> getAllCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(candidate -> new CandidateResponseDTO(
                        candidate.getId(),
                        candidate.getName(),
                        candidate.getEmail(),
                        candidate.getPhone()
                ))
                .toList();
    }

    public CandidateResponseDTO getCandidateById(Long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        return new CandidateResponseDTO(
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getPhone()
        );
    }
}
