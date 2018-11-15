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

package pl.wavesoftware.eid.configuration;

import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class TestConfigurator implements Configurator {

    @Override
    public void configure(ConfigurationBuilder configuration) {
        configuration
            .locale(Locale.ENGLISH)
            .timezone(TimeZone.getTimeZone("GMT"))
            .validator(new PseudoDateValidator());
    }

    private static final class PseudoDateValidator implements Validator {
        private final Pattern pattern = Pattern.compile("^\\d{8}:\\d{6}$");

        @Override
        public boolean isValid(CharSequence id) {
            return pattern.matcher(id).matches();
        }
    }
}
