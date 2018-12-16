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

package pl.wavesoftware.eid.exceptions;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.configuration.ConfigurationBuilder;
import pl.wavesoftware.eid.configuration.Configurator;
import pl.wavesoftware.eid.configuration.UniqueIdGenerator;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class ConstantUniqueIdRule implements TestRule {

    private final String constantId;

    ConstantUniqueIdRule(String constantId) {
        this.constantId = constantId;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Configurator configurator = configureConstantId();
                try {
                    base.evaluate();
                } finally {
                    Eid.getBinding().getConfigurationSystem().configure(configurator);
                }
            }
        };
    }

    private Configurator configureConstantId() {
        return Eid.getBinding().getConfigurationSystem().configure(new Configurator() {
            @Override
            public void configure(ConfigurationBuilder configuration) {
                configuration.uniqueIdGenerator(new UniqueIdGenerator() {
                    @Override
                    public String generateUniqId() {
                        return constantId;
                    }
                });
            }
        });
    }
}
