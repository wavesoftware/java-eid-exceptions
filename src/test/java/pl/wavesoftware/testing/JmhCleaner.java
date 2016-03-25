package pl.wavesoftware.testing;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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
        Path testAnnotationsPath = Paths.get(location).getParent().resolve(GENERATED_TEST_SOURCES).resolve(TEST_ANNOTATIONS);
        if (!testAnnotationsPath.toFile().isDirectory()) {
            return;
        }
        Files.walkFileTree(testAnnotationsPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
