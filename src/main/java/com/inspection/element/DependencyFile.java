package com.inspection.element;
import com.inspection.visitor.ProjectVisitor;
public final class DependencyFile implements ProjectElement {
    public final String name;
    public final java.util.List<Dependency> dependencies;
    public DependencyFile(String name, java.util.List<Dependency> dependencies) {
        this.name = name;
        this.dependencies = java.util.List.copyOf(dependencies);
    }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
