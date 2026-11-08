package com.inspection;
import com.inspection.element.*;
import com.inspection.visitor.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class InspectionTest {
    private SourceCodeFile source(int complexity) { return new SourceCodeFile("App.java", 100, complexity, false, false); }
    private Directory healthy() { return new Directory("root").add(source(5)).add(new TestFile("AppTest.java", 40, "App.java")); }
    @Test void sourceDispatch() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); source(7).accept(v); assertEquals(1, v.sources); }
    @Test void testDispatch() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new TestFile("T.java", 10, "App.java").accept(v); assertEquals(1, v.tests); }
    @Test void configurationCountsAsFile() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new ConfigurationFile("a", false, false).accept(v); assertEquals(1, v.files); }
    @Test void dependencyCounts() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new DependencyFile("pom", List.of(new Dependency("a", "1", false, false))).accept(v); assertEquals(1, v.dependencies); }
    @Test void securityDetectsCredentials() { SecurityAuditVisitor v = new SecurityAuditVisitor(); new SourceCodeFile("a", 1, 1, true, false).accept(v); assertEquals(1, v.criticalIssues()); }
    @Test void securityDetectsVulnerability() { SecurityAuditVisitor v = new SecurityAuditVisitor(); new DependencyFile("pom", List.of(new Dependency("x", "1", true, false))).accept(v); assertEquals(75, v.score()); }
    @Test void statisticsRatio() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); healthy().accept(v); assertEquals(1, v.testSourceRatio()); assertEquals(5, v.averageComplexity()); }
    @Test void qualityPass() { QualityGateVisitor v = new QualityGateVisitor(); healthy().accept(v); assertTrue(v.passed()); }
    @Test void qualityFailsComplexity() { QualityGateVisitor v = new QualityGateVisitor(); new Directory("r").add(source(11)).add(new TestFile("t", 1, "App.java")).accept(v); assertFalse(v.complexityPass()); }
    @Test void qualityFailsTests() { QualityGateVisitor v = new QualityGateVisitor(); source(5).accept(v); assertFalse(v.testsPass()); }
    @Test void qualityFailsSecurity() { QualityGateVisitor v = new QualityGateVisitor(); new SourceCodeFile("x", 1, 2, true, false).accept(v); assertFalse(v.securityPass()); }
    @Test void complianceMissingTest() { ComplianceVisitor v = new ComplianceVisitor(); source(2).accept(v); assertFalse(v.compliant()); }
    @Test void complianceMissingVersion() { ComplianceVisitor v = new ComplianceVisitor(); new DependencyFile("pom", List.of(new Dependency("x", "", false, false))).accept(v); assertTrue(v.violations().stream().anyMatch(x -> x.contains("Missing version"))); }
    @Test void complianceProhibited() { ComplianceVisitor v = new ComplianceVisitor(); new DependencyFile("pom", List.of(new Dependency("x", "1", false, true))).accept(v); assertFalse(v.compliant()); }
    @Test void nestedDirectories() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new Directory("a").add(new Directory("b").add(source(3))).accept(v); assertEquals(1, v.files); }
    @Test void emptyDirectory() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new Directory("empty").accept(v); assertEquals(0, v.files); assertEquals(0, v.averageComplexity()); }
    @Test void technicalDebt() { TechnicalDebtVisitor v = new TechnicalDebtVisitor(); source(15).accept(v); assertEquals(10, v.debtPoints()); }
    @Test void negativeLinesRejected() { assertThrows(IllegalArgumentException.class, () -> new TestFile("x", -1, "a")); }
    @Test void immutableDependencies() { var list = new java.util.ArrayList<Dependency>(); var f = new DependencyFile("pom", list); list.add(new Dependency("x", "1", false, false)); assertTrue(f.dependencies.isEmpty()); }
    @Test void selfReferenceRejected() { Directory d = new Directory("x"); assertThrows(IllegalArgumentException.class, () -> d.add(d)); }
}
