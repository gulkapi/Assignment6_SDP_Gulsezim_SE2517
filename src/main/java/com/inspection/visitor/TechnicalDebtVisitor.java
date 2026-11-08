package com.inspection.visitor;
import com.inspection.element.*;
public final class TechnicalDebtVisitor extends AbstractProjectVisitor {
    private int debtPoints;
    @Override public void visit(SourceCodeFile f) { debtPoints += Math.max(0, f.complexity - 10) * 2; if (f.highRisk) debtPoints += 5; }
    @Override public void visit(DependencyFile f) { for (Dependency d : f.dependencies) if (d.vulnerable()) debtPoints += 10; }
    public int debtPoints() { return debtPoints; }
    public String report() { return "Technical debt points: " + debtPoints; }
}
