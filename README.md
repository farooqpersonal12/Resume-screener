# Smart Resume Screener

An AI-powered candidate-job matching backend built with Spring Boot and the Gemini LLM.

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Problem Statement](#2-problem-statement)
3. [Objectives](#3-objectives)
4. [Key Features](#4-key-features)
5. [Technology Stack](#5-technology-stack)
6. [System Architecture](#6-system-architecture)
7. [Database Design](#7-database-design)
8. [Application Workflow](#8-application-workflow)
9. [Gemini LLM Integration](#9-gemini-llm-integration)
10. [REST API](#10-rest-api)
11. [Setup and Installation](#11-setup-and-installation)
12. [Deployment](#12-deployment)
13. [Sample Matching Result](#13-sample-matching-result)
14. [Future Enhancements](#14-future-enhancements)
15. [Project Deliverables](#15-project-deliverables)
16. [Demo Video](#16-demo-video)
17. [Author](#17-author)

---

## 1. Project Overview

**Smart Resume Screener** is a backend application that automates the initial screening of candidates against job requirements. It stores candidate and job information, processes resumes and job descriptions, and uses the **Gemini LLM** to evaluate candidate-job compatibility, producing:

- Match Score
- Matched Skills
- AI-generated Justification

The point is to cut down manual screening effort and give recruiters a structured, explainable candidate evaluation.

### Project Goal

> How well does this candidate match the requirements of this job?

Rather than relying on exact keyword matching, the system uses LLM-based semantic analysis to weigh a candidate's skills and qualifications against the job requirements.

---

## 2. Problem Statement

Manual resume screening means recruiters reviewing and comparing large numbers of resumes against job descriptions by hand, which leads to:

- High manual effort
- Increased screening time
- Difficulty comparing candidates
- Inconsistent evaluation
- Keyword-based limitations
- Lack of clear justification for candidate selection

### Proposed Solution

Smart Resume Screener automates the initial screening by comparing candidate profiles with job requirements through an LLM, producing:

1. **Match Score** — Overall candidate-job compatibility.
2. **Matched Skills** — Skills relevant to the job requirements.
3. **Justification** — Explanation of the generated score.

---

## 3. Objectives

- Store candidate information in a structured database.
- Store job descriptions and requirements.
- Upload and process candidate resumes.
- Upload job description files.
- Extract and use relevant candidate information.
- Compare candidates with job requirements.
- Perform semantic matching using Gemini LLM.
- Generate a match score.
- Identify matching skills.
- Generate an AI-based justification.
- Store matching results in the database.
- Shortlist candidates based on a minimum match score.
- Provide REST APIs for interacting with the system.

---

## 4. Key Features

### Candidate Management

- Create candidates.
- Retrieve all candidates.
- Retrieve candidates by ID.
- Store candidate-related information.

### Job Management

- Create jobs.
- Retrieve all jobs.
- Retrieve jobs by ID.
- Upload job description files.

### Resume Management

- Upload candidate resumes.
- Associate resumes with candidates.
- Retrieve stored resumes.

### AI-Based Matching

- Compare candidate profiles with job requirements.
- Use Gemini LLM for semantic analysis.
- Generate match scores.
- Identify matching skills.
- Generate explanations for matching results.

### Candidate Shortlisting

Retrieves shortlisted candidates for a specific job using a minimum match-score threshold. Default: **7.0**.

---

## 5. Technology Stack

| Technology | Purpose |
|---|---|
| **Java** | Backend programming language |
| **Spring Boot** | Backend framework |
| **Spring Data JPA** | Database persistence |
| **Hibernate** | ORM |
| **MySQL** | Relational database |
| **Gemini LLM** | AI-based semantic matching |
| **Maven** | Build and dependency management |
| **Docker** | MySQL containerization |
| **Git & GitHub** | Version control |
| **IntelliJ IDEA** | Development environment |

---

## 6. System Architecture

The application follows a layered Spring Boot architecture.

```text
                         ┌──────────────────────┐
                         │       Client         │
                         │      REST API        │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │  Controller Layer    │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    Service Layer     │
                         │    Business Logic    │
                         └───────┬───────┬──────┘
                                 │       │
                    ┌────────────┘       └────────────┐
                    ▼                                 ▼
          ┌──────────────────┐             ┌──────────────────┐
          │ Repository Layer │             │   Gemini LLM     │
          └────────┬─────────┘             └────────┬─────────┘
                   │                                │
                   ▼                                │
          ┌──────────────────┐                      │
          │      MySQL       │◄─────────────────────┘
          │     Database     │
          └──────────────────┘
```

### Architecture Layers

**Controller Layer** — Handles HTTP requests and exposes REST APIs.

**Service Layer** — Contains business logic and coordinates candidate, job, resume, and matching operations.

**Repository Layer** — Handles database operations using Spring Data JPA.

**Entity Layer** — Represents persistent database entities.

**DTO Layer** — Transfers structured request and response data between the API and application layers.

---

## 7. Database Design

MySQL handles persistent storage. The main entities:

```text
Candidate
    │
    │
    ▼
MatchResult
    ▲
    │
    │
   Job
```

**Candidate** — Stores candidate information used during screening and matching.

**Job** — Stores job information, descriptions, and requirements.

**MatchResult** — Stores the result of evaluating a candidate against a job:

- Match score
- Matched skills
- Justification
- Candidate reference
- Job reference
- Creation timestamp

### Persistence

```text
Spring Data JPA
       │
       ▼
   Hibernate
       │
       ▼
     MySQL
```

Docker runs the MySQL database in a containerized environment.

---

## 8. Application Workflow

```text
Create Candidate
       │
       ▼
Upload Resume
       │
       ▼
Create Job
       │
       ▼
Upload Job Description
       │
       ▼
Select Candidate + Job
       │
       ▼
Prepare Matching Prompt
       │
       ▼
Gemini LLM Analysis
       │
       ▼
Match Score
Matched Skills
Justification
       │
       ▼
Store MatchResult
       │
       ▼
Shortlist Candidates
```

### Matching Process

Candidate and job information are retrieved from the database and passed to Gemini, which evaluates:

- Candidate skills
- Experience
- Education
- Job requirements
- Required technologies
- Overall compatibility

The result is stored as a `MatchResult`.

---

## 9. Gemini LLM Integration

Gemini handles semantic candidate-job matching.

### Integration Flow

```text
Candidate Information
        +
Job Description
        │
        ▼
Spring Boot Service
        │
        ▼
Prompt Construction
        │
        ▼
Gemini API
        │
        ▼
Gemini LLM
        │
        ▼
Match Score
Matched Skills
Justification
```

### Basic Prompt

```text
Compare the following resume with this job description and rate fit on
1–10 with justification.
```

### Example Prompt

```text
You are an AI-powered resume screening assistant.

Compare the following candidate profile with the given job description.

Candidate:
Name: John Doe
Skills: Java, Spring Boot, Hibernate, REST APIs, MySQL
Experience: 2 years of backend development
Education: Bachelor's degree in Computer Science

Job Description:
We are looking for an Associate Java Developer with knowledge of
Java, Spring Boot, REST APIs, Hibernate, and MySQL.

Evaluate how well the candidate matches the job requirements.

Provide:
1. A match score from 1 to 10.
2. The skills that match the job requirements.
3. A clear justification for the score.
```

### Example Output

```json
{
  "score": 8.7,
  "matchedSkills": [
    "Java",
    "Spring Boot",
    "Hibernate",
    "REST APIs",
    "MySQL"
  ],
  "justification": "The candidate is a strong match for the Associate Java Developer position based on the required technical skills and relevant experience."
}
```

### LLM Responsibilities

Gemini handles the semantic evaluation. The Spring Boot application handles everything around it:

- Retrieving candidate data.
- Retrieving job data.
- Constructing the prompt.
- Calling the Gemini API.
- Processing the response.
- Creating the MatchResult.
- Saving the result in MySQL.
- Returning the result through the REST API.

---

## 10. REST API

### Candidate APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/candidates` | Create candidate |
| GET | `/api/candidates` | Get all candidates |
| GET | `/api/candidates/{id}` | Get candidate by ID |

### Job APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/jobs` | Create job |
| GET | `/api/jobs` | Get all jobs |
| GET | `/api/jobs/{id}` | Get job by ID |
| POST | `/api/jobs/upload` | Upload job description |

Job upload parameters:
- `file` → Job description file
- `title` → Job title

### Resume APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/resumes` | Get all resumes |
| GET | `/api/resumes/{id}` | Get resume by ID |
| POST | `/api/resumes/upload` | Upload resume |

Resume upload parameters:
- `file` → Resume file
- `candidateId` → Candidate ID

### Matching APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/matches` | Create candidate-job match |
| GET | `/api/matches` | Get all matches |
| GET | `/api/matches/{id}` | Get match by ID |
| GET | `/api/matches/jobs/{jobId}/shortlist` | Get shortlisted candidates |

### Shortlisting

The shortlist API accepts an optional `minScore` parameter. Default: `minScore = 7.0`.

Example:

```text
GET /api/matches/jobs/2/shortlist?minScore=8.0
```

---

## 11. Setup and Installation

### Prerequisites

- Java JDK
- Docker
- Git
- IntelliJ IDEA
- MySQL or Docker
- Gemini API key

### Clone Repository

```bash
git clone <repository-url>
cd <project-directory>
```

### Start MySQL

Start the MySQL Docker container used by the application.

### Configure Gemini API Key

In PowerShell:

```powershell
$env:GEMINI_API_KEY="YOUR_ACTUAL_KEY"
```

Do not commit the API key to GitHub.

### Build the Project

```powershell
.\mvnw clean test
```

### Run the Application

```powershell
.\mvnw spring-boot:run
```

---

## 12. Deployment

The backend is deployed on Render using Docker, with Aiven MySQL as the production database and the Gemini API for AI-powered resume-job matching.

### Live API

Render Deployment: [https://resume-screener-gbtd.onrender.com](https://resume-screener-gbtd.onrender.com/api/candidates)

### Deployment Architecture

```text
GitHub Repository
       │
       ▼
     Render
   Docker + Spring Boot
       │
       ├──────────────────┐
       │                  │
       ▼                  ▼
 Aiven MySQL          Gemini API
       │
       ▼
Candidate / Resume / Job / Match Data
```

### Technologies Used

- **Render** — Spring Boot backend deployment
- **Docker** — Containerization
- **Aiven MySQL** — Production database
- **Gemini API** — AI-powered resume and job matching
- **GitHub** — Source code and version control

### Production Configuration

Sensitive configuration values are stored as environment variables and never committed to the repository:

```text
GEMINI_API_KEY
DB_URL
DB_USERNAME
DB_PASSWORD
```

Environment-based configuration means the same codebase runs in both local development and production.

### Verified Production Features

- Candidate creation and retrieval
- Resume PDF upload and skill extraction
- Job description upload
- AI-powered resume-job matching
- Match score generation
- Matched and missing skill identification
- Match-result persistence
- Candidate shortlisting
- Production MySQL read/write operations

---

## 13. Sample Matching Result

```json
{
  "score": 8.7,
  "matchedSkills": [
    "Java",
    "Spring Boot",
    "Hibernate",
    "REST APIs",
    "MySQL"
  ],
  "justification": "The candidate is a strong match for the Associate Java Developer position based on the required technical skills and relevant experience."
}
```

The result gives both a numerical score and an explanation, so the screening decision isn't a black box.

---

## 14. Future Enhancements

- React-based recruiter dashboard.
- Advanced PDF resume parsing.
- Improved resume information extraction.
- Candidate ranking.
- Batch resume processing.
- Authentication and authorization.
- Recruiter accounts and roles.
- Advanced candidate filtering.
- Matching analytics and reports.
- Improved prompt engineering and evaluation.
- Automated candidate recommendations.

---

## 15. Project Deliverables

- GitHub repository with project source code.
- Technical README documentation.
- LLM integration and prompts.
- REST APIs for candidate, job, resume, and matching operations.
- AI-generated candidate-job matching results.

Evaluation focuses on code quality, data extraction, LLM prompt quality, and output clarity.

---

## 16. Demo Video

▶️ **[Watch the Smart Resume Screener Demo](YOUR_YOUTUBE_LINK)**

The demo covers:

- Candidate creation
- Resume upload
- Job creation
- Job description upload
- Gemini LLM-based matching
- Match score generation
- Matched skills
- AI-generated justification
- Candidate shortlisting

---

## 17. Author

**UMAR FAROOQ**

### Project

**Smart Resume Screener**
