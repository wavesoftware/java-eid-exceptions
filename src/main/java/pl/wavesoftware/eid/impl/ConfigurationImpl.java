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
import pl.wavesoftware.eid.configuration.Formatter;
import pl.wavesoftware.eid.configuration.UniqueIdGenerator;
import pl.wavesoftware.eid.configuration.Validator;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class ConfigurationImpl implements MutableConfiguration {

    private Formatter formatter;
    private UniqueIdGenerator generator;
    @Nullable
    private Validator validator;
    @Nullable
    private Locale locale;
    @Nullable
    private TimeZone zone;

    ConfigurationImpl() {
        // nothing here
    }

    ConfigurationImpl(MutableConfiguration settings) {
        this.formatter = settings.getFormatter();
        this.generator = settings.getIdGenerator();
        this.validator = settings.getValidator();
        this.locale = settings.getLocale();
        this.zone = settings.getTimeZone();
    }

    @Override
    public ConfigurationBuilder uniqueIdGenerator(UniqueIdGenerator generator) {
        this.generator = generator;
        return this;
    }

    @Override
    public ConfigurationBuilder formatter(Formatter formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public ConfigurationBuilder locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    @Override
    public ConfigurationBuilder timezone(TimeZone zone) {
        this.zone = zone;
        return this;
    }

    @Override
    public ConfigurationBuilder validator(Validator validator) {
        this.validator = validator;
        return this;
    }

    @Override
    public Configuration getFutureConfiguration() {
        return this;
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public UniqueIdGenerator getIdGenerator() {
        return generator;
    }

    @Nullable
    @Override
    public Validator getValidator() {
        return validator;
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return locale;
    }

    @Nullable
    @Override
    public TimeZone getTimeZone() {
        return zone;
    }
}
