package pl.wavesoftware.testing;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 29.03.16
 */
public final class JavaAgentSkip implements TestRule {

    public static final String JAVAAGENT = "-javaagent:";
    private final boolean ifActive;

    private JavaAgentSkip(boolean ifActive) {
        this.ifActive = ifActive;
    }

    public static JavaAgentSkip ifActive() {
        return new JavaAgentSkip(true);
    }

    public static JavaAgentSkip ifNotActive() {
        return new JavaAgentSkip(false);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Assume.assumeTrue("Skipping test due to JavaAgentSkip rule that is set to " + ifActive, assumeValue());
                base.evaluate();
            }
        };
    }

    private boolean assumeValue() {
        return isAgentThere() != ifActive;
    }

    private boolean isAgentThere() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        boolean agent = false;
        for (String argument : arguments) {
            if (argument.startsWith(JAVAAGENT)) {
                agent = true;
                break;
            }
        }
        return agent;
    }
}
