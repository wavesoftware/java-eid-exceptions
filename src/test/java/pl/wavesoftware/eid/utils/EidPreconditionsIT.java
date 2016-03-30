package pl.wavesoftware.eid.utils;

import com.google.common.base.Preconditions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;
import pl.wavesoftware.testing.JavaAgentSkip;
import pl.wavesoftware.testing.JmhCleaner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 25.03.16
 */
public class EidPreconditionsIT {
    private static final int OPERATIONS = 1000;
    private static final double SPEED_THRESHOLD = 0.90;
    private static final double PERCENT = 100;
    private static final Logger LOG = LoggerFactory.getLogger(EidPreconditionsIT.class);

    @ClassRule
    public static RuleChain chain = RuleChain
        .outerRule(new JmhCleaner(EidPreconditionsIT.class))
        .around(JavaAgentSkip.ifActive());

    @Test
    public void doBenchmarking() throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(this.getClass().getName() + ".*")
            .mode(Mode.Throughput)
            .timeUnit(TimeUnit.MICROSECONDS)
            .operationsPerInvocation(OPERATIONS)
            .warmupTime(TimeValue.seconds(1))
            .warmupIterations(1)
            .measurementTime(TimeValue.seconds(1))
            .measurementIterations(3)
            .threads(4)
            .forks(1)
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .jvmArgs("-server", "-Xms256m", "-Xmx256m", "-XX:PermSize=128m", "-XX:MaxPermSize=128m", "-XX:+UseParallelGC")
            .build();

        Runner runner = new Runner(opt);
        Collection<RunResult> results = runner.run();
        assertThat(results.size()).isGreaterThanOrEqualTo(7);

        verifySpeedFor(TestCase.CHECK_ARGUMENT, results);
        verifySpeedFor(TestCase.CHECK_STATE, results);
        verifySpeedFor(TestCase.CHECK_NOTNULL, results);
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_ARGUMENT, framework = Framework.GUAVA)
    public void testCheckArgument(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            Preconditions.checkArgument(i >= 0, "20160325:123449");
            bh.consume(i);
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_STATE, framework = Framework.GUAVA)
    public void testCheckState(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            Preconditions.checkState(i >= 0, "20160325:123534");
            bh.consume(i);
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_NOTNULL, framework = Framework.GUAVA)
    public void testCheckNotNull(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(Preconditions.checkNotNull(bh, "20160325:123600"));
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_ARGUMENT, framework = Framework.EID)
    public void testCheckArgumentEid(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            EidPreconditions.checkArgument(i >= 0, "20160325:123701");
            bh.consume(i);
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_STATE, framework = Framework.EID)
    public void testCheckStateEid(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            EidPreconditions.checkState(i >= 0, "20160325:123705");
            bh.consume(i);
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.CHECK_NOTNULL, framework = Framework.EID)
    public void testCheckNotNullEid(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(EidPreconditions.checkNotNull(bh, "20160325:123710"));
        }
    }

    @State(Scope.Benchmark)
    public static class SupplierOfUnsafes {
        private EidPreconditions.UnsafeSupplier<String> supplier;
        private EidPreconditions.UnsafeProcedure procedure;

        @Setup
        public void setup() {
            this.supplier = new EidPreconditions.UnsafeSupplier<String>() {

                @Override
                public String get() throws Exception {
                    return "20160326:223746";
                }
            };
        }
    }

    @Benchmark
    @BenchmarkConfig(test = TestCase.TRY_TO_EXECUTE, framework = Framework.EID)
    public void testTryToExecuteSupplier(SupplierOfUnsafes supplierOfUnsafes, Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(EidPreconditions.tryToExecute(supplierOfUnsafes.supplier, "20160326:223854"));
        }
    }

    private void verifySpeedFor(TestCase testCase, Collection<RunResult> results) {
        Method guavaMethod = findMethod(testCase, Framework.GUAVA);
        Method eidMethod = findMethod(testCase, Framework.EID);

        RunResult guavaResult = lookupResult(results, guavaMethod);
        RunResult eidResult = lookupResult(results, eidMethod);

        double guava = getScore(guavaResult);
        double eid = getScore(eidResult);

        double ratio = eid / guava;

        LOG.info(String.format("%s: Guava score = %.2f vs Eid score = %.2f ==> ratio: %.2f%%, " +
            "minimum threshold: %.2f%%",
            testCase, guava, eid, ratio * PERCENT, SPEED_THRESHOLD * PERCENT));
        assertThat(ratio).isGreaterThanOrEqualTo(SPEED_THRESHOLD);
    }

    private static double getScore(RunResult result) {
        return result.getPrimaryResult().getScore();
    }

    private RunResult lookupResult(Collection<RunResult> results, Method method) {
        String name = method.getName();
        String fullName = String.format("%s.%s", this.getClass().getName(), name);
        for (RunResult result : results) {
            if (result.getParams().getBenchmark().equals(fullName)) {
                return result;
            }
        }
        throw new EidRuntimeException("20160324:225412", "Invalid name: " + name);
    }

    private Method findMethod(TestCase testCase, Framework framework) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            BenchmarkConfig config = method.getAnnotation(BenchmarkConfig.class);
            if (config == null) {
                continue;
            }
            if (config.framework() == framework && config.test() == testCase) {
                return method;
            }
        }
        throw new IllegalArgumentException(String.format("No testCase: %s for framework: %s found!",
            testCase, framework));
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface BenchmarkConfig {
        TestCase test();
        Framework framework();
    }

    private enum TestCase {
        CHECK_ARGUMENT,
        CHECK_STATE,
        TRY_TO_EXECUTE, CHECK_NOTNULL
    }

    private enum Framework {
        GUAVA, EID
    }
}
