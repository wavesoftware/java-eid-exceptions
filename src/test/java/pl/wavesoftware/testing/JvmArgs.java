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
public final class JvmArgs {

    private static final String[] PRE_RELEASE_8 = new String[] {
        "-server", "-Xms256m", "-Xmx256m",
        "-XX:PermSize=128m", "-XX:MaxPermSize=128m",
        "-XX:+UseParNewGC"
    };

    private static final String[] RELEASE_8_PLUS = new String[] {
        "-server", "-Xms256m", "-Xmx256m",
        "-XX:+UseParallelGC"
    };

    private JvmArgs() {
        // nothing here
    }

    public static String[] get() {
        JavaVersion version = JavaVersion.get();
        return version.greaterOrEqual(JavaVersion.of(8))
            ? RELEASE_8_PLUS : PRE_RELEASE_8;
    }
}
