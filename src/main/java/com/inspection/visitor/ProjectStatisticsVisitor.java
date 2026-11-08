package com.inspection.visitor;
import com.inspection.element.*;
public final class ProjectStatisticsVisitor extends AbstractProjectVisitor {
    public int files, lines, tests, sources, dependencies, complexitySum;
    @Override public void visit(SourceCodeFile f) { files++; sources++; lines += f.lines; complexitySum += f.complexity; }
    @Override public void visit(TestFile f) { files++; tests++; lines += f.lines; }
    @Override public void visit(ConfigurationFile f) { files++; }
    @Override public void visit(DependencyFile f) { files++; dependencies += f.dependencies.size(); }
    public double averageComplexity() { return sources == 0 ? 0 : (double) complexitySum / sources; }
    public double testSourceRatio() { return sources == 0 ? 0 : (double) tests / sources; }
    public String report() { return String.format(java.util.Locale.ROOT, "Files: %d, LOC: %d, Tests: %d, Dependencies: %d, Average complexity: %.2f, Test/source ratio: %.2f", files, lines, tests, dependencies, averageComplexity(), testSourceRatio()); }
}
