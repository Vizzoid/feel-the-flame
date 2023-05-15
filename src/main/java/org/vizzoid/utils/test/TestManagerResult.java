package org.vizzoid.utils.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TestManagerResult {

    private final int cases;
    private final List<TestResult> failures;
    private final boolean noFailures;

    public TestManagerResult(int cases, List<TestResult> failures) {
        this.cases = cases;
        this.failures = failures;
        this.noFailures = failures.isEmpty();
    }

    public int getSuccesses() {
        if (noFailures) return cases;
        return cases - failures.size();
    }

    public int getCases() {
        return cases;
    }

    public List<TestResult> getFailures() {
        return failures;
    }

    public String toString() {
        StringBuilder description = new StringBuilder();
        if (noFailures) description.append("All ");
        description.append(getSuccesses()).append(" cases succeeded");

        if (!noFailures) {
            for (TestResult failure : failures) {
                description.append("\n\n\tTest case #");
                description.append(failure.getId());
                description.append(" failed:\n\t");
                description.append(failure);
            }
            description.append("\n");
        }

        return description.toString();
    }

    public LogRecord log() {
        LogRecord log = new LogRecord(noFailures ? Level.INFO : Level.WARNING, toString());
        log.setLoggerName("TestManagerResult");
        return log;
    }

}
