# Smart Code Inspection & Compliance Engine

Java 17 / Maven implementation of the **Visitor design pattern** for a simulated fintech project. Five element types support five independent inspections, including the separately added `TechnicalDebtVisitor`.

## Run

```bash
mvn clean test
mvn exec:java
```

## Architecture

- `ProjectElement` defines `accept(ProjectVisitor)`.
- `SourceCodeFile`, `TestFile`, `ConfigurationFile`, `DependencyFile`, and `Directory` are concrete elements.
- `ProjectVisitor` declares five overloaded `visit` methods.
- `AbstractProjectVisitor` provides traversal of nested directories and default no-op visits.
- `SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, `ComplianceVisitor`, and `TechnicalDebtVisitor` implement separate operations.
- `Main.sample()` builds the realistic `fintech-project` demonstration. Metadata is simulated; this is not a filesystem or source-code scanner.

## Visitor pattern defense questions

**1. What problem does Visitor solve?** It keeps inspection operations separate from project element classes, avoiding unrelated audit methods in every file class.

**2. Where are Element and Visitor?** `ProjectElement` and its five implementations are in `element`; `ProjectVisitor` and concrete visitors are in `visitor`.

**3. What is double dispatch?** `element.accept(visitor)` dispatches based on the element implementation, which invokes `visitor.visit(this)` and selects the matching overload for the concrete element type.

**4. Why not put operations in Elements?** Security, statistics, compliance and quality would be coupled to the data model, making each new inspection require edits to all elements.

**5. Is it easier to add a Visitor or an Element?** A visitor: add a class implementing existing visit methods without editing elements. A new element requires changes to the visitor interface and every visitor implementation (or their common abstract base).

**6. When is Visitor a bad choice?** When element types change frequently, since each new element changes the Visitor API and existing implementations.

**7. Visitor versus Strategy?** Strategy selects one interchangeable algorithm for a task; Visitor applies multiple operations across a stable, heterogeneous object structure with type-specific visit methods.

**8. SOLID principles?** Single Responsibility (one inspection per visitor), Open/Closed (new inspections without changing element classes), Dependency Inversion (elements depend on the ProjectVisitor abstraction).

## Visitor limitation challenge

Adding `DatabaseMigrationFile` requires a new `ProjectElement` implementation with `accept`, a new `visit(DatabaseMigrationFile)` overload in `ProjectVisitor`, and a corresponding implementation in `AbstractProjectVisitor` (or each direct implementer). Each existing visitor must decide whether to process or ignore migrations. The new overload is a compile-time breaking change for direct implementers of `ProjectVisitor`.

## Demo behavior

The example contains two source files, one test, one configuration file and one dependency file. It intentionally includes a vulnerable/prohibited dependency, insecure configuration, an untested module, and a high-complexity source. Consequently the demo release gate fails. The pass case is covered in JUnit tests.

Rules: complexity <= 10, test/source ratio >= 0.40, zero critical security findings. Complexity is the arithmetic mean over source files. An empty project fails the test gate. Security score starts at 100 and deducts 25 per critical issue and 10 per high issue, with a floor of zero. Technical debt adds 2 points per complexity unit over 10, 5 for high-risk source, and 10 per vulnerable dependency.

## Diagrams

PlantUML source: `docs/class-diagram.puml` and `docs/sequence-diagram.puml`. Render them in a PlantUML plugin or online PlantUML renderer. Both describe the actual project implementation.

## Tests

20 JUnit 5 tests cover element dispatch, all required visitors, quality pass/fail, compliance, nested and empty directories, additional visitor, and edge cases.
