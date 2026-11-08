package com.inspection.visitor;
import com.inspection.element.*;
import java.util.ArrayList;
import java.util.List;
public final class SecurityAuditVisitor extends AbstractProjectVisitor {
    private final List<String> issues = new ArrayList<>();
    @Override public void visit(SourceCodeFile f) { if (f.hardcodedCredentials) issues.add("CRITICAL: hardcoded credentials in " + f.name); if (f.highRisk) issues.add("HIGH: risky source " + f.name); }
    @Override public void visit(ConfigurationFile f) { if (f.exposedCredentials) issues.add("CRITICAL: exposed credentials in " + f.name); if (f.insecure) issues.add("HIGH: insecure configuration " + f.name); }
    @Override public void visit(DependencyFile f) { for (Dependency d : f.dependencies) if (d.vulnerable()) issues.add("CRITICAL: vulnerable dependency " + d.name()); }
    public List<String> issues() { return List.copyOf(issues); }
    public long criticalIssues() { return issues.stream().filter(s -> s.startsWith("CRITICAL")).count(); }
    public int score() { return Math.max(0, 100 - (int)criticalIssues() * 25 - (issues.size() - (int)criticalIssues()) * 10); }
    public String report() { return "Security score: " + score() + "/100; issues=" + issues; }
}
