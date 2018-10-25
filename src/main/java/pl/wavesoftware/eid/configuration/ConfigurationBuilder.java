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

/**
 * Use {@link Configurator} to configure Eid settings.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @see Configurator
 */
public interface ConfigurationBuilder {

    /**
     * Configures a unique ID generator used by Eid objects to generate unique
     * part of Eid number.
     *
     * @param generator a generator of unique ID
     * @return a self reference for ease of use
     */
    ConfigurationBuilder uniqueIdGenerator(UniqueIdGenerator generator);

    /**
     * Configures a formatter to be used by Eid numbers.
     *
     * @param formatter a formatter to be used by Eid formatting and displaying
     * @return a self reference for ease of use
     */
    ConfigurationBuilder formatter(Formatter formatter);

    /**
     * Sets a locale to be used when formatting texts. If not set default system
     * locale will be used (platform specific). See {@link Locale#getDefault()}.
     *
     * @param locale a locale to be used
     * @return a self reference for ease of use
     * @see Locale#getDefault()
     */
    ConfigurationBuilder locale(Locale locale);

    /**
     * Configures a validator that will be called on each Eid number. By
     * default, there is no validator configured for maximum speed. Using this
     * validator you can enforce a standardization of Eid numbers.
     *
     * @param validator a validator to be used
     * @return a self reference for ease of use
     */
    ConfigurationBuilder validator(Validator validator);
}
