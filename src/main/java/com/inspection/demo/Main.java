package com.inspection.demo;
import com.inspection.element.*;
import com.inspection.visitor.*;
import java.util.List;
public final class Main {
    public static Directory sample() {
        return new Directory("fintech-project")
            .add(new Directory("src")
                .add(new SourceCodeFile("PaymentService.java", 240, 14, false, true))
                .add(new SourceCodeFile("UserService.java", 160, 7, false, false)))
            .add(new Directory("tests").add(new TestFile("PaymentServiceTest.java", 120, "PaymentService.java")))
            .add(new Directory("config").add(new ConfigurationFile("application.properties", true, false)))
            .add(new DependencyFile("pom.xml", List.of(new Dependency("legacy-crypto", "1.0", true, true), new Dependency("junit", "5.11.4", false, false))));
    }
    public static void main(String[] args) {
        Directory project = sample();
        SecurityAuditVisitor security = new SecurityAuditVisitor();
        ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor();
        QualityGateVisitor quality = new QualityGateVisitor();
        ComplianceVisitor compliance = new ComplianceVisitor();
        TechnicalDebtVisitor debt = new TechnicalDebtVisitor();
        project.accept(security); project.accept(statistics); project.accept(quality); project.accept(compliance); project.accept(debt);
        System.out.println(security.report());
        System.out.println(statistics.report());
        System.out.println(quality.report());
        System.out.println(compliance.report());
        System.out.println(debt.report());
    }
}
