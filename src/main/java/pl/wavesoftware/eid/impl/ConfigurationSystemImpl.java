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

import pl.wavesoftware.eid.configuration.Configuration;
import pl.wavesoftware.eid.configuration.ConfigurationBuilder;
import pl.wavesoftware.eid.configuration.ConfigurationSystem;
import pl.wavesoftware.eid.configuration.Configurator;

import java.util.ServiceLoader;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-10-29
 */
final class ConfigurationSystemImpl implements ConfigurationSystem {

    private volatile MutableConfiguration configuration;

    ConfigurationSystemImpl() {
        // nothing here
    }

    @Override
    public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = getMutableConfiguration();
        }
        return configuration;
    }

    /**
     * Configures an Eid library programmatically.
     *
     * @param configurator a configurator to use to configure Eid library
     * @return a reference to a configurator that can be used to restore
     * previous configuration
     */
    @Override
    public Configurator configure(Configurator configurator) {
        // ensure system configuration are loaded
        getConfiguration();
        MutableConfiguration configuredSettings = configuration;
        configuration = new ConfigurationImpl(configuredSettings);
        configurator.configure(configuration);
        return new RestoreConfigurator(configuredSettings);
    }

    private synchronized MutableConfiguration getMutableConfiguration() {
        if (configuration == null) {
            return loadConfiguration();
        }
        return configuration;
    }

    private static MutableConfiguration loadConfiguration() {
        MutableConfiguration mutableConfiguration = new ConfigurationImpl();
        new DefaultConfigurator().configure(mutableConfiguration);
        ServiceLoader<Configurator> configurators =
            ServiceLoader.load(Configurator.class);

        for (Configurator configurator : configurators) {
            configurator.configure(mutableConfiguration);
        }
        return mutableConfiguration;
    }

    private static final class RestoreConfigurator
        implements Configurator {
        private final MutableConfiguration configuration;

        RestoreConfigurator(MutableConfiguration configuration) {
            this.configuration = configuration;
        }

        /*
        Suppress is for forcing nullable values to non null methods
        for copy constructor.
         */
        @SuppressWarnings("ConstantConditions")
        @Override
        public void configure(ConfigurationBuilder builder) {
            builder.formatter(configuration.getFormatter())
                .uniqueIdGenerator(configuration.getIdGenerator())
                .validator(configuration.getValidator())
                .locale(configuration.getLocale());
        }
    }
}
