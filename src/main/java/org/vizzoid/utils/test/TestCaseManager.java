package org.vizzoid.utils.test;

import org.vizzoid.utils.TimeStamp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestCaseManager {

    private final LinkedHashMap<TestCase, Exception> cases;
    private final TestTrace trace;

    public TestCaseManager() {
        this(TestTrace.SHORT);
    }

    public TestCaseManager(TestTrace trace) {
        this.trace = trace;
        this.cases = new LinkedHashMap<>();
    }

    public <T extends TestCase> T add(T t) {
        cases.put(t, new Exception()); // exception is for context
        return t;
    }

    public TestManagerResult test() {
        int id = 0;
        List<TestResult> failures = new ArrayList<>();
        for (Map.Entry<TestCase, Exception> entry : cases.entrySet()) {
            TestCase aCase = entry.getKey();
            TestResult result = aCase.test();
            id++;
            if (result.isSuccess()) continue;
            trace.followTrace(result, entry.getValue());
            result.initId(id);
            failures.add(result);
        }
        return new TestManagerResult(cases.size(), failures);
    }

    private TestThread thread;

    public void thread(Consumer<TestManagerResult> resultConsumer) {
        if (thread != null) thread.interrupt();
        thread = new TestThread(TimeStamp.now()) {
            @Override
            public void run() {
                resultConsumer.accept(test());
            }
        };
        thread.start();
    }

}
