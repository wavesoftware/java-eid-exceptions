package pl.wavesoftware.eid.exceptions;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wavesoftware.testing.JavaAgentSkip;
import pl.wavesoftware.testing.JmhCleaner;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-24
 */
public class EidIT {

    private static final int PERCENT = 100;
    private static final int OPERATIONS = 1000;
    private static final Logger LOG = LoggerFactory.getLogger(EidIT.class);
    private static final double SPEED_THRESHOLD = 0.75d;

    @ClassRule
    public static RuleChain chain = RuleChain
        .outerRule(new JmhCleaner(EidIT.class))
        .around(JavaAgentSkip.ifActive());

    @Test
    public void doBenckmarking() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.MICROSECONDS)
                .operationsPerInvocation(OPERATIONS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(5)
                .threads(Threads.MAX)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        Runner runner = new Runner(opt);
        Collection<RunResult> results = runner.run();
        assertThat(results).hasSize(2);

        RunResult control = getRunResultByName(results, "control");
        RunResult eid = getRunResultByName(results, "eid");
        assertThat(control).isNotNull();
        assertThat(eid).isNotNull();

        double controlScore = control.getAggregatedResult().getPrimaryResult().getScore();
        double eidScore = eid.getAggregatedResult().getPrimaryResult().getScore();

        String title = "method speed quotient to the control sample";
        String eidTitle = String.format("%s %s should be at least %.2f%%", "#eid()",
                title, SPEED_THRESHOLD * PERCENT);

        double eidTimes = eidScore / controlScore;

        LOG.info(String.format("Control sample method time per operation: %.2f µsec", controlScore));
        LOG.info(String.format("#eid() method time per operation:         %.2f µsec", eidScore));
        LOG.info(String.format("%s and is %.2f%%", eidTitle, eidTimes * PERCENT));

        assertThat(eidTimes).as(eidTitle).isGreaterThanOrEqualTo(SPEED_THRESHOLD);
    }

    @Benchmark
    public void control(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(new Date());
        }
    }

    @Benchmark
    public void eid(Blackhole bh) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(new Eid("20160324:223837"));
        }
    }

    private static RunResult getRunResultByName(Collection<RunResult> results, String name) {
        String fullName = String.format("%s.%s", EidIT.class.getName(), name);
        for (RunResult result : results) {
            if (result.getParams().getBenchmark().equals(fullName)) {
                return result;
            }
        }
        throw new EidRuntimeException("20160324:225412", "Invalid name: " + name);
    }
}
