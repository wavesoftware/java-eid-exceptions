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

package pl.wavesoftware.testing;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 25.03.16
 */
public class JmhCleaner extends ExternalResource {
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
    protected void after() {
        try {
            cleanup();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
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
