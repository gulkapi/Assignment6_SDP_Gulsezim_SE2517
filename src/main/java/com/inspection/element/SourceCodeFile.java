package com.inspection.element;
import com.inspection.visitor.ProjectVisitor;
public final class SourceCodeFile implements ProjectElement {
    public final String name;
    public final int lines;
    public final int complexity;
    public final boolean hardcodedCredentials;
    public final boolean highRisk;
    public SourceCodeFile(String name, int lines, int complexity, boolean hardcodedCredentials, boolean highRisk) {
        if (lines < 0) throw new IllegalArgumentException("Negative lines");
        if (complexity < 0) throw new IllegalArgumentException("Negative complexity");
        this.name = name;
        this.lines = lines;
        this.complexity = complexity;
        this.hardcodedCredentials = hardcodedCredentials;
        this.highRisk = highRisk;
    }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
