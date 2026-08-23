package com.resumescreener.resume_screener.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeParsingService {

    private final List<String> knownSkills = List.of(
            "Java",
            "Python",
            "JavaScript",
            "C",
            "C++",
            "SQL",
            "HTML",
            "CSS",
            "React.js",
            "Angular",
            "Node.js",
            "Spring Boot",
            "Spring Framework",
            "Spring MVC",
            "Spring Data JPA",
            "Hibernate",
            "REST APIs",
            "MySQL",
            "MongoDB",
            "Oracle SQL",
            "Git",
            "GitHub",
            "Docker",
            "Maven",
            "Postman",
            "Data Structures & Algorithms",
            "OOP",
            "DBMS",
            "Operating Systems",
            "Computer Networks"
    );

    public String extractSkills(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        List<String> extractedSkills = new ArrayList<>();

        String lowerCaseText = text.toLowerCase();

        for (String skill : knownSkills) {

            if (lowerCaseText.contains(skill.toLowerCase())) {
                extractedSkills.add(skill);
            }
        }

        return String.join(", ", extractedSkills);
    }

    public String extractExperience(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        String lowerCaseText = text.toLowerCase();

        int start = lowerCaseText.indexOf("experience");

        if (start == -1) {
            start = lowerCaseText.indexOf("work experience");
        }

        if (start == -1) {
            return "";
        }

        String experienceSection = text.substring(start);

        String lowerExperience = experienceSection.toLowerCase();

        String[] sectionEndings = {
                "education",
                "certifications",
                "projects",
                "technical skills",
                "skills",
                "relevant coursework"
        };

        int end = experienceSection.length();

        for (String ending : sectionEndings) {

            int index = lowerExperience.indexOf(ending);

            if (index > 0 && index < end) {
                end = index;
            }
        }

        return experienceSection.substring(0, end).trim();
    }

    public String extractEducation(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        String lowerCaseText = text.toLowerCase();

        int start = lowerCaseText.indexOf("education");

        if (start == -1) {
            return "";
        }

        String educationSection = text.substring(start);

        String lowerEducation = educationSection.toLowerCase();

        String[] sectionEndings = {
                "relevant coursework",
                "certifications",
                "projects",
                "technical skills",
                "skills",
                "experience"
        };

        int end = educationSection.length();

        for (String ending : sectionEndings) {

            int index = lowerEducation.indexOf(ending);

            if (index > 0 && index < end) {
                end = index;
            }
        }

        return educationSection.substring(0, end).trim();
    }



}
