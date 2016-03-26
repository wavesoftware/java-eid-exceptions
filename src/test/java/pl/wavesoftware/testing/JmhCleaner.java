package pl.wavesoftware.testing;

import com.google.common.io.Files;
import java.io.File;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 25.03.16
 */
public class JmhCleaner implements TestRule {
    private static final String GENERATED_TEST_SOURCES = "generated-test-sources";
    private static final String TEST_ANNOTATIONS = "test-annotations";
    private final Class<?> testClass;

    public JmhCleaner(Class<?> testClass) {
        this.testClass = validateTestClass(testClass);
    }

    private Class<?> validateTestClass(Class<?> testClass) {
        boolean hasTests = false;
        for (Method method : testClass.getDeclaredMethods()) {
            Test annot = method.getAnnotation(Test.class);
            if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && annot != null) {
                hasTests = true;
                break;
            }
        }
        if (!hasTests) {
            throw new IllegalArgumentException("You need to pass a test class to constructor of JmhCleaner!!");
        }
        return testClass;
    }


    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {
                    cleanup();
                }
            }
        };
    }

    private void cleanup() throws IOException, URISyntaxException {
        String location = testClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File file = new File(location).getCanonicalFile().getParentFile();
        File testAnnotationsPath = resolve(file, GENERATED_TEST_SOURCES, TEST_ANNOTATIONS);
        if (!testAnnotationsPath.isDirectory()) {
            return;
        }
        FileUtils.deleteDirectory(testAnnotationsPath);
    }
    
    private File resolve(File parent, String... paths) {
        StringBuilder sb = new StringBuilder(parent.getPath());
        for (String path : paths) {
            sb.append(File.separator).append(path);
        }
        return new File(sb.toString());
    }
}
