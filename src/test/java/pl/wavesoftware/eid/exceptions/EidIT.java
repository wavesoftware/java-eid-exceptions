package pl.wavesoftware.eid.exceptions;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-24
 */
public class EidIT {

    private static final int OPERATIONS = 1000;

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
}