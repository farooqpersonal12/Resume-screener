package com.resumescreener.resume_screener.Controller;


import com.resumescreener.resume_screener.service.PdfExtractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/test")
public class PdfTestController {
    private final PdfExtractionService pdfExtractionService;

    public PdfTestController(PdfExtractionService pdfExtractionService) {
        this.pdfExtractionService = pdfExtractionService;
    }

    @PostMapping("/extract-pdf")
    public ResponseEntity<String> extractPdf(@RequestParam("file") MultipartFile file) {

        String extractedText = pdfExtractionService.extractText(file);
        return ResponseEntity.ok(extractedText);
    }


}
