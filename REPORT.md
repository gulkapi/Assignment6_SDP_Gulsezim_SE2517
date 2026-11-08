# Assignment 6 — Implementation Report

## Objective

Implement a Smart Code Inspection & Compliance Engine with the Visitor design pattern using Java 17 and Maven. The engine processes a stable hierarchy of project elements with independent analysis operations.

## Design

`ProjectElement.accept` calls the overloaded `ProjectVisitor.visit` method using the concrete element type. `Directory` owns a collection of project elements and visitors recursively traverse nested directories through `AbstractProjectVisitor`. No `instanceof` or type-based switch is used.

## Elements

`SourceCodeFile` stores name, lines, complexity, credentials flag and risk flag. `TestFile` stores name, lines and target module. `ConfigurationFile` stores security flags. `DependencyFile` holds immutable dependency metadata (version, vulnerability, prohibition). `Directory` contains nested elements.

## Operations

1. Security Audit reports hardcoded/exposed credentials, vulnerable dependencies, insecure configuration and risky source files, and computes a 0–100 score.
2. Project Statistics reports files, lines, test count, dependency count, average source complexity and test/source ratio.
3. Quality Gate checks critical security issues, average complexity <= 10, and test/source ratio >= 0.40. An empty project fails the test threshold.
4. Compliance verifies credentials, dependency versions, prohibited libraries and test coverage for production modules.
5. Technical Debt estimates debt points from complexity, source risk and vulnerable dependencies. It was introduced without editing element classes.

## Demonstration

`Main.sample()` creates `fintech-project` with `src/PaymentService.java`, `src/UserService.java`, `tests/PaymentServiceTest.java`, `config/application.properties` and `pom.xml`. The intentionally imperfect project produces actionable reports and a FAILED release gate.

## Verification

Run `mvn clean test` for 20 JUnit 5 cases and `mvn exec:java` for the demonstration. Tests cover nested directories, empty directories, a passing release gate, failing gates, compliance violations, security flags, and invalid input.

## UML

`docs/class-diagram.puml` shows interfaces, element implementations, visitor implementations, directory composition and the quality gate's collaborators. `docs/sequence-diagram.puml` shows `accept` and `visit` calls during nested traversal.

## Trade-offs

The Visitor pattern makes adding new inspections straightforward, while new element types require changes to the visitor contract. This solution analyzes explicit in-memory metadata rather than parsing real Java files, which keeps the demonstration deterministic and focused on Visitor.

## Submission

Public GitHub repository, README and exported PDF report. Screenshot the test result and demo output after running locally. During defense explain double dispatch and demonstrate a new visitor without changing the elements.
