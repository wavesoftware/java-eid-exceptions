/*
 * Copyright (c) 2015 Wave Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.wavesoftware.eid;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;
import pl.wavesoftware.testing.JavaAgentSkip;
import pl.wavesoftware.testing.JmhCleaner;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2016-03-24
 */
public class EidIT {

    private static final int PERCENT = 100;
    private static final int OPERATIONS = 1000;
    private static final Logger LOG = LoggerFactory.getLogger(EidIT.class);
    private static final double SPEED_THRESHOLD = 0.9d;

    @ClassRule
    public static RuleChain chain = RuleChain
        .outerRule(new JmhCleaner(EidIT.class))
        .around(JavaAgentSkip.ifActive());

    @Test
    public void doBenckmarking() throws Exception {
        Options opt = new OptionsBuilder()
            .include(this.getClass().getName() + ".*")
            .mode(Mode.Throughput)
            .timeUnit(TimeUnit.MILLISECONDS)
            .operationsPerInvocation(OPERATIONS)
            .warmupTime(TimeValue.seconds(1))
            .warmupIterations(2)
            .measurementTime(TimeValue.seconds(1))
            .measurementIterations(5)
            .threads(4)
            .forks(1)
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .jvmArgs(
                "-server", "-Xms256m", "-Xmx256m",
                "-XX:PermSize=128m", "-XX:MaxPermSize=128m",
                "-XX:+UseParallelGC"
            )
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

        LOG.info(String.format("Control sample method time per operation: %" +
            ".2f ops / msec", controlScore));
        LOG.info(String.format("#eid() method time per operation:         %" +
            ".2f ops / msec", eidScore));
        LOG.info(String.format("%s and is %.2f%%", eidTitle, eidTimes * PERCENT));

        assertThat(eidTimes).as(eidTitle).isGreaterThanOrEqualTo(SPEED_THRESHOLD);
    }

    @Benchmark
    public void control(Blackhole bh, DisableValidatorState state) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(new Date());
        }
    }

    @Benchmark
    public void eid(Blackhole bh, DisableValidatorState state) {
        for (int i = 0; i < OPERATIONS; i++) {
            bh.consume(new DefaultEid("20160330:144947"));
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
