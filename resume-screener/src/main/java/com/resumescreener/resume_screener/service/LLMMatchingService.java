package com.resumescreener.resume_screener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumescreener.resume_screener.DTOs.LLMMatchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LLMMatchingService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LLMMatchResponseDTO matchResumeWithJob(
            String resumeText,
            String jobDescription) {

        String prompt = buildPrompt(resumeText, jobDescription);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", model);
        requestBody.put("input", prompt);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

        try {

            String jsonResponse =
                    extractModelOutput(response.getBody());

            return objectMapper.readValue(
                    jsonResponse,
                    LLMMatchResponseDTO.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process Gemini response",
                    e
            );
        }
    }

    private String buildPrompt(
            String resumeText,
            String jobDescription) {

        return """
            You are an expert technical recruiter.

            Evaluate the candidate ONLY against the requirements
            explicitly stated in the Job Description.

            The Job Description is the single source of truth.

            Analyze the Job Description for:
            - Required technical skills
            - Technologies
            - Tools
            - Experience requirements
            - Education requirements
            - Responsibilities
            - Other explicit qualifications

            Then compare those requirements against the complete
            candidate resume.

            Determine:

            1. Overall suitability.
            2. Score from 1 to 10.
            3. Requirements satisfied by the candidate.
            4. Important requirements not demonstrated by the candidate.
            5. A concise justification for the score.

            Important rules:

            - Do not invent requirements.
            - Do not use requirements that are not present in the JD.
            - Do not assume a skill simply because it is related to
              another skill.
            - Consider equivalent terminology when appropriate.
            - Consider projects, internships, and academic experience.
            - Distinguish academic/project experience from professional
              industry experience in the justification.
            - matchedSkills should contain requirements supported by
              evidence in the resume.
            - missingSkills should contain important JD requirements
              that are not supported by the resume.
            - Score must be between 1 and 10.
            - Return ONLY valid JSON.
            - Do not use Markdown.
            - Do not add ```json.

            Return exactly:

            {
              "score": 8.5,
              "justification": "Explanation based on the JD and resume.",
              "matchedSkills": [
                "Java",
                "Spring Boot"
              ],
              "missingSkills": [
                "AWS"
              ]
            }

            JOB DESCRIPTION:
            %s

            CANDIDATE RESUME:
            %s
            """.formatted(
                jobDescription,
                resumeText
        );
    }

    private String extractModelOutput(
            Map responseBody) {

        if (responseBody == null) {
            throw new RuntimeException(
                    "Empty response received from Gemini"
            );
        }

        Object stepsObject =
                responseBody.get("steps");

        if (!(stepsObject instanceof List<?> steps)) {
            throw new RuntimeException(
                    "Gemini response does not contain steps"
            );
        }

        for (Object stepObject : steps) {

            if (!(stepObject instanceof Map<?, ?> step)) {
                continue;
            }

            Object type = step.get("type");

            if ("model_output".equals(type)) {

                Object content =
                        step.get("content");

                if (!(content instanceof List<?> contentList)) {
                    continue;
                }

                for (Object contentObject : contentList) {

                    if (!(contentObject instanceof Map<?, ?> contentMap)) {
                        continue;
                    }

                    Object text =
                            contentMap.get("text");

                    if (text != null) {
                        return cleanJson(text.toString());
                    }
                }
            }
        }

        throw new RuntimeException(
                "Could not find model output in Gemini response"
        );
    }

    private String cleanJson(String response) {

        String cleaned = response.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        }

        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(
                    0,
                    cleaned.length() - 3
            );
        }

        return cleaned.trim();
    }
}