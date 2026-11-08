package com.inspection.element;
import com.inspection.visitor.ProjectVisitor;
public final class ConfigurationFile implements ProjectElement {
    public final String name;
    public final boolean insecure;
    public final boolean exposedCredentials;
    public ConfigurationFile(String name, boolean insecure, boolean exposedCredentials) {
        this.name = name;
        this.insecure = insecure;
        this.exposedCredentials = exposedCredentials;
    }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
