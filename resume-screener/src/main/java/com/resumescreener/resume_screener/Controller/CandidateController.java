package com.resumescreener.resume_screener.Controller;

import com.resumescreener.resume_screener.DTOs.CandidateRequestDTO;
import com.resumescreener.resume_screener.DTOs.CandidateResponseDTO;
import com.resumescreener.resume_screener.service.CandidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> createCandidate(
            @RequestBody CandidateRequestDTO candidateRequestDTO) {

        CandidateResponseDTO responseDTO = candidateService.createCandidate(candidateRequestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {

        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                candidateService.getCandidateById(id)
        );
    }


}
