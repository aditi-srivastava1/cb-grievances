# CB Grievances — Progress Log

A simple record of what I did, step by step, so I can look back and actually understand what happened — not just "I did something."

---

## Step 1: Generated the Spring Boot project using Spring Initializr

**Date:** 26 September 2026

### What I actually did
I went to a website called **start.spring.io**. This is an official tool made by the Spring team — it's basically a project "starter kit" generator. Instead of manually creating folders and config files for a Java backend project, this tool builds the skeleton for you, ready to download as a ZIP file.

### What each setting meant

**Project: Maven**
Maven is a *build tool* — it manages your project's dependencies (external code libraries you use) and knows how to compile and run your project. Think of it like a manager that keeps track of "which tools does this project need" (`pom.xml` file), so you don't manually download and configure every library.

*(Hindi: Maven ek tool hai jo tumhare project ki saari libraries aur dependencies manage karta hai — bina iske tumhe manually sab kuch download aur setup karna padta.)*

**Language: Java**
Straightforward — the programming language the whole project will be written in.

**Spring Boot version: 4.1.1**
Spring Boot is the *framework* — a large, ready-made set of tools built on top of Java specifically for building backend web applications quickly (handles things like servers, routing, database connections, etc. so you don't build them from scratch).

**Group: com.cbgrievances**
This is like a "namespace" — a unique identifying label for your organization/project, following reverse-domain naming convention. It doesn't do anything functional by itself; it's just how Java organizes code into packages.

**Artifact: cb-grievances**
This is the actual project/app name. It becomes your folder name and your main class name (e.g., `CbGrievancesApplication`).

**Java version: 17**
The specific version of Java the project will run on. 17 is a stable, widely-used "LTS" (Long Term Support) version.

**Packaging: Jar**
This decides how your final compiled project gets packaged — a `.jar` file is a single bundled executable file, the standard choice for Spring Boot apps.

### Dependencies added (and why)

| Dependency | What it does |
|---|---|
| **Spring Web** | Lets the app receive and respond to web requests (this is what makes it a "web application" — handles HTTP requests) |
| **Spring Data JPA** | Makes it easy to connect Java code to a database without writing raw SQL for every single operation |
| **Thymeleaf** | The templating engine — lets me write HTML pages that can display dynamic data (e.g., show a list of complaints) coming from the backend |
| **MySQL Driver** | The actual connector that lets the Java app talk to a MySQL database specifically |
| **Spring Security** | Handles login, authentication, and access control (e.g., making sure only a Warden can access the Warden dashboard) |

*(Hindi: Ye saari dependencies wo "ready-made tools" hain jo backend banane ke alag-alag kaam handle karte hain — jaise database se baat karna, login system banana, HTML pages dikhana, etc. Inhe khud se code karne ki zarurat nahi, Spring already bana ke deta hai.)*

### Result
Clicked **Generate** → downloaded a `.zip` file. This ZIP contains the full starting skeleton of the CB Grievances project — folders, a basic config file, and a placeholder main class — ready to be opened in an IDE (IntelliJ) and built upon.

### What I have NOT done yet (for next time)
- Extract the ZIP file
- Open the project in IntelliJ
- Understand the folder structure that got generated
- Set up the MySQL database connection

---

## Today's plan
- ✅ Step 1 done (project generated)
- 📚 Now: DSA practice
- 🌙 Tonight: continue from here (extracting + opening project)
