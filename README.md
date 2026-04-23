<h1>Car Rental System</h1>
<h2>Overview</h2>
<p>The Car Rental System is a web application built in Java 17 using the Spring Boot framework to manage cars and rental  efficiently. The application provides full CRUD operations (Create, Read, Update, Delete) for car and rental entities, enabling the creation, modification, deletion, and retrieval of records. The backend uses Java 17 to ensure long‑term support and maintainability.The project follows Test‑Driven Development (TDD), where test cases are written prior to implementation to guarantee robust functionality. Additionally, Mutation Testing using PIT (PITest) is applied to identify and remove weak tests, thereby ensuring strong test effectiveness. To achieve 100% code coverage, JaCoCo and Coveralls are integrated, providing detailed test validation. For CI/CD, GitHub Actions are configured to automate the build, test, and deployment process. SonarCloud is integrated for static code analysis to detect bugs, identify vulnerabilities, and suggest improvements in the codebase. The project is containerized using Docker, enabling automated testing in isolated environments and ensuring portability across multiple systems. Finally, Git and GitHub are used for version control to maintain a structured workflow, including branching strategies, pull requests, and code reviews.</p>



Sonar Cloud: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=WajahatAliFarooq_car_rental_sys&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=WajahatAliFarooq_car_rental_sys)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=WajahatAliFarooq_car_rental_sys&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=WajahatAliFarooq_car_rental_sys)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=WajahatAliFarooq_car_rental_sys&metric=coverage)](https://sonarcloud.io/summary/new_code?id=WajahatAliFarooq_car_rental_sys)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=WajahatAliFarooq_car_rental_sys&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=WajahatAliFarooq_car_rental_sys)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=WajahatAliFarooq_car_rental_sys&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=WajahatAliFarooq_car_rental_sys)

Code coverall: [![Coverage Status](https://coveralls.io/repos/github/WajahatAliFarooq/car_rental_sys/badge.svg?branch=master)](https://coveralls.io/github/WajahatAliFarooq/car_rental_sys?branch=master)

GithubAction: [![Java CI with Maven](https://github.com/WajahatAliFarooq/car_rental_sys/actions/workflows/maven.yml/badge.svg)](https://github.com/WajahatAliFarooq/car_rental_sys/actions/workflows/maven.yml)

## 📌 Project Overview

This project enables users to:

- **Manage cars:** create, update, view, delete
- **Create and manage rentals** associated with cars
- **Access features via:**
  - RESTful APIs
  - Web UI built with Thymeleaf
- **Maintain high software quality through:**
  - Automated tests
  - Code analysis
  - Full branch coverage

---

## Features

- **Java 17** – Modern LTS version
- **Spring Boot** – Rapid application setup
- **Spring MVC** – REST APIs and web layer
- **Spring Data JPA** – MySQL integration
- **Thymeleaf** – Server-side UI rendering
- **MySQL (Dockerized) / H2 (for tests)** – Production DB with in-memory test alternative
- **Selenium** – End-to-end testing
- **JaCoCo & Pitest** – Code coverage and mutation testing
- **SonarCloud** – Static analysis and quality gate
- **TestContainers** – Containerized integration tests

---

## Setup and Installation

### Prerequisites
- Java 17 or later  
- Maven  
- Docker (for MySQL database)  

### Steps
<ol>
  <li>
    <strong>Clone the Repository</strong>
    <pre><code>git clone https://github.com/WajahatAliFarooq/car_rental_sys.git</code></pre>
  </li>
  <li>
    <strong>Start the MySQL Database using Docker Compose</strong>
    <pre><code>docker-compose up -d</code></pre>
  </li>
</ol>