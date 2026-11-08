package com.inspection.element;
import com.inspection.visitor.ProjectVisitor;
public final class TestFile implements ProjectElement {
    public final String name;
    public final int lines;
    public final String targetModule;
    public TestFile(String name, int lines, String targetModule) {
        if (lines < 0) throw new IllegalArgumentException("Negative lines");
        this.name = name;
        this.lines = lines;
        this.targetModule = targetModule;
    }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
