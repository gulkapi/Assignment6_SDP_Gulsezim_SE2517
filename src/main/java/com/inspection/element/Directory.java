package com.inspection.element;
import com.inspection.visitor.ProjectVisitor;
import java.util.ArrayList;
import java.util.List;
public final class Directory implements ProjectElement {
    public final String name;
    private final List<ProjectElement> children = new ArrayList<>();
    public Directory(String name) { this.name = name; }
    public Directory add(ProjectElement element) { if (element == null || element == this) throw new IllegalArgumentException("Invalid child"); children.add(element); return this; }
    public List<ProjectElement> children() { return List.copyOf(children); }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
