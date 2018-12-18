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

package pl.wavesoftware.eid.impl;

import org.junit.Rule;
import org.junit.Test;
import pl.wavesoftware.eid.ConstantUniqueIdRule;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.api.Configuration;
import pl.wavesoftware.eid.api.Formatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-03
 */
public class DefaultFormatterTest {

    @Rule
    public ConstantUniqueIdRule uniqueIdRule = new ConstantUniqueIdRule(
        "deadcafe"
    );

    @Test
    public void format() {
        // given
        Configuration configuration = new ConfigurationImpl();
        Formatter formatter = new DefaultFormatter(configuration);

        // when
        String formatted = formatter.format(new DefaultEid("20181203:224055"));

        // then
        assertThat(formatted).isEqualTo("[20181203:224055]<deadcafe>");
    }

    @Test
    public void formatWithMessage() {
        // given
        Configuration configuration = new ConfigurationImpl();
        Formatter formatter = new DefaultFormatter(configuration);

        // when
        String formatted = formatter.format(
            new DefaultEid("20181203:224137"),
            "a message"
        );

        // then
        assertThat(formatted).isEqualTo("[20181203:224137]<deadcafe> => a message");
    }
}
