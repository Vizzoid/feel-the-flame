package org.vizzoid.utils.test;

import java.util.Arrays;

public enum TestTrace {

    SHORT() {
        @Override
        public void followTrace(TestResult result, Exception context) {
            StackTraceElement[] print = new StackTraceElement[]{
                context.getStackTrace()[3],
                result.getFailure().getStackTrace()[0]};

            result.setStackElements(print);
        }
    },
    NORMAL(),
    FULL() {
        @Override
        public void followTrace(TestResult result, Exception context) {
            Exception failure = result.getFailure();
            if (failure.getCause() == null) failure.initCause(context);
            else {
                StackTraceElement[] oldTrace = failure.getStackTrace();
                StackTraceElement[] traceToAdd = context.getStackTrace();
                StackTraceElement[] newTrace = Arrays.copyOf(oldTrace, oldTrace.length + traceToAdd.length);

                System.arraycopy(traceToAdd, 0, newTrace, oldTrace.length, traceToAdd.length);
                result.setStackElements(newTrace);
            }
        }
    };

    public void followTrace(TestResult result, Exception context) {

    }

}
