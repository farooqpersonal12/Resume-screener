package com.resumescreener.resume_screener.Controller;

import com.resumescreener.resume_screener.DTOs.MatchResultRequestDTO;
import com.resumescreener.resume_screener.DTOs.MatchResultResponseDTO;
import com.resumescreener.resume_screener.DTOs.ShortlistResponseDTO;
import com.resumescreener.resume_screener.service.MatchResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchResultController {

    private MatchResultService matchResultService;
    public MatchResultController(MatchResultService matchResultService) {
        this.matchResultService = matchResultService;
    }

    @PostMapping
    public ResponseEntity<MatchResultResponseDTO> createMatch(
            @RequestBody MatchResultRequestDTO requestDTO) {

        return ResponseEntity.ok(
                matchResultService.createMatch(requestDTO)
        );
    }

    @GetMapping
    public ResponseEntity<List<MatchResultResponseDTO>> getAllMatches() {

        return ResponseEntity.ok(
                matchResultService.getAllMatches()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResultResponseDTO> getMatchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                matchResultService.getMatchById(id)
        );
    }

    @GetMapping("/jobs/{jobId}/shortlist")
    public List<ShortlistResponseDTO> getShortlistedCandidates(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "7.0") Double minScore) {

        return matchResultService.getShortlistedCandidates(
                jobId,
                minScore
        );
    }

}
