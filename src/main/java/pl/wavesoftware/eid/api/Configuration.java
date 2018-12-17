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

package pl.wavesoftware.eid.api;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Represents a configuration of Eid library. To reconfigure use
 * {@link Configurator} interface.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @see Configurator
 */
public interface Configuration {
    /**
     * Gets a Eid formatter
     *
     * @return a formatter of Eid
     */
    Formatter getFormatter();

    /**
     * Gets a Eid unique ID generator
     *
     * @return unique ID generator
     */
    UniqueIdGenerator getIdGenerator();

    /**
     * Gets a Eid validator if set. Returns null if validator wasn't configured.
     *
     * @return an Eid validator, or null
     */
    @Nullable
    Validator getValidator();

    /**
     * Gets a locale to be used when formatting texts. Returns null if locale
     * isn't set.
     *
     * @return a locale to be used when formatting texts, or null
     */
    @Nullable
    Locale getLocale();

    /**
     * Gets a time zone to be used when formatting texts. Returns null if time
     * zone isn't set.
     *
     * @return a time zone to be used when formatting texts, or null
     */
    @Nullable
    TimeZone getTimeZone();
}
