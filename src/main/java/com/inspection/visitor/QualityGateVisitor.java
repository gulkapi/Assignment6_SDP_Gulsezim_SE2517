package com.inspection.visitor;
import com.inspection.element.*;
public final class QualityGateVisitor extends AbstractProjectVisitor {
    private final ProjectStatisticsVisitor stats = new ProjectStatisticsVisitor();
    private final SecurityAuditVisitor security = new SecurityAuditVisitor();
    @Override public void visit(SourceCodeFile f) { stats.visit(f); security.visit(f); }
    @Override public void visit(TestFile f) { stats.visit(f); security.visit(f); }
    @Override public void visit(ConfigurationFile f) { stats.visit(f); security.visit(f); }
    @Override public void visit(DependencyFile f) { stats.visit(f); security.visit(f); }
    public boolean securityPass() { return security.criticalIssues() == 0; }
    public boolean complexityPass() { return stats.averageComplexity() <= 10; }
    public boolean testsPass() { return stats.sources > 0 && stats.testSourceRatio() >= 0.40; }
    public boolean passed() { return securityPass() && complexityPass() && testsPass(); }
    public String report() { return "Security: " + status(securityPass()) + " | Complexity: " + status(complexityPass()) + " | Tests: " + status(testsPass()) + " | FINAL RESULT: " + (passed() ? "PASSED" : "FAILED"); }
    private String status(boolean ok) { return ok ? "PASS" : "FAIL"; }
}
