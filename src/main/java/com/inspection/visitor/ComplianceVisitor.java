package com.inspection.visitor;
import com.inspection.element.*;
import java.util.*;
public final class ComplianceVisitor extends AbstractProjectVisitor {
    private final List<String> violations = new ArrayList<>();
    private final Set<String> modules = new HashSet<>(), tested = new HashSet<>();
    @Override public void visit(SourceCodeFile f) { modules.add(f.name); if (f.hardcodedCredentials) violations.add("Exposed credentials: " + f.name); }
    @Override public void visit(TestFile f) { tested.add(f.targetModule); }
    @Override public void visit(ConfigurationFile f) { if (f.exposedCredentials) violations.add("Exposed credentials: " + f.name); }
    @Override public void visit(DependencyFile f) { for (Dependency d : f.dependencies) { if (d.version() == null || d.version().isBlank()) violations.add("Missing version: " + d.name()); if (d.prohibited()) violations.add("Prohibited dependency: " + d.name()); } }
    public List<String> violations() { List<String> result = new ArrayList<>(violations); for (String m : modules) if (!tested.contains(m)) result.add("Missing test for: " + m); return List.copyOf(result); }
    public boolean compliant() { return violations().isEmpty(); }
    public String report() { return "Compliance: " + (compliant() ? "PASS" : "FAIL") + "; violations=" + violations(); }
}
