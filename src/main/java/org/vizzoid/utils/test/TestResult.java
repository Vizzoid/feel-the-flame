package org.vizzoid.utils.test;

public class TestResult {

    private static final TestResult SUCCESS = new TestResult(true);
    private static final TestResult FAILURE = new TestResult(false);

    private final boolean success;
    private final Exception failure;
    private StackTraceElement[] stackElements;
    private int id = -1;

    private TestResult(boolean success, Exception failure) {
        this.success = success;
        this.failure = failure;
        this.stackElements = failure == null ? null : failure.getStackTrace();
    }

    private TestResult(boolean success) {
        this(success, null);
    }

    private TestResult(Exception failure) {
        this(false, failure);
    }

    public static TestResult success() {
        return SUCCESS;
    }

    public static TestResult failure(Exception failure) {
        return new TestResult(failure);
    }

    @Deprecated
    public static TestResult create(boolean success) {
        return success ? SUCCESS : FAILURE;
    }

    public boolean isSuccess() {
        return success;
    }

    public Exception getFailure() {
        return failure;
    }

    public void setStackElements(StackTraceElement[] stackElements) {
        this.stackElements = stackElements;
    }

    public String toString() {
        if (success) return "Test succeeded";

        StringBuilder failureDescription = new StringBuilder("Test failed");
        if (failure == null) return failureDescription.toString();
        int startIndex = 0;

        buildTrace(startIndex,
            failureDescription
                .append(": ")
                .append(failure.getMessage()),
            stackElements);

        if (stackElements == failure.getStackTrace()) { // can be changed, if so, we don't want cause to be included
            Throwable cause = failure.getCause();
            if (cause != null) {
                buildTrace(startIndex,
                    failureDescription
                        .append("\n\tCaused by: ")
                        .append(cause.getMessage()),
                    cause.getStackTrace());
            }
        }

        return failureDescription.toString();
    }

    private StringBuilder buildTrace(int index, StringBuilder builder, StackTraceElement[] stackElements) {
        assert failure != null;
        if (index >= stackElements.length) return builder;
        return buildTrace(index + 1,
            builder
            .append("\n")
            .append("\tat ")
            .append(stackElements[index]),
            stackElements);
    }

    public int getId() {
        return id;
    }

    public void initId(int id) {
        if (this.id != -1) return;
        this.id = id;
    }

}
