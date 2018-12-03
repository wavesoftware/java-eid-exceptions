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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.configuration.Configuration;
import pl.wavesoftware.eid.configuration.ConfigurationBuilder;
import pl.wavesoftware.eid.configuration.Configurator;
import pl.wavesoftware.eid.configuration.Formatter;
import pl.wavesoftware.eid.configuration.UniqueIdGenerator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-03
 */
public class DefaultFormatterTest {

    private Configurator restore;

    @Before
    public void before() {
        restore = Eid.getBinding().getConfigurationSystem().configure(new Configurator() {
            @Override
            public void configure(ConfigurationBuilder configuration) {
                configuration.uniqueIdGenerator(new UniqueIdGenerator() {
                    @Override
                    public String generateUniqId() {
                        return "deadcafe";
                    }
                });
            }
        });
    }

    @After
    public void after() {
        Eid.getBinding().getConfigurationSystem().configure(restore);
    }

    @Test
    public void format() {
        // given
        Configuration configuration = new ConfigurationImpl();
        Formatter formatter = new DefaultFormatter(configuration);

        // when
        String formatted = formatter.format(new Eid("20181203:224055"));

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
            new Eid("20181203:224137"),
            "a message"
        );

        // then
        assertThat(formatted).isEqualTo("[20181203:224137]<deadcafe> => a message");
    }
}
