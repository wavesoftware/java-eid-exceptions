/*
 * Copyright (c) 2018 Wave Software
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

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-29
 */
public final class JavaVersion implements Comparable<JavaVersion> {
    private final int major;

    private JavaVersion(int major) {
        this.major = major;
    }

    public static JavaVersion get() {
        return new JavaVersion(getMajor());
    }

    public static JavaVersion of(int major) {
        return new JavaVersion(major);
    }

    private static int getMajor() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }
        // Allow these formats:
        // 1.8.0_72-ea
        // 9-ea
        // 9
        // 9.0.1
        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return Integer.parseInt(version.substring(
            0,
            dotPos > -1
                ? dotPos
                : dashPos > -1
                ? dashPos : 1
        ));
    }

    public boolean greaterOrEqual(JavaVersion other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(JavaVersion other) {
        return this.major - other.major;
    }
}
