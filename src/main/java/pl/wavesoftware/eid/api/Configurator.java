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

import java.util.ServiceLoader;

/**
 * You can use this interface with Java's {@link ServiceLoader} class.
 * <p>
 * To do that, create on your classpath, a file:
 * <pre>META-INF/services/pl.wavesoftware.eid.api.Configurator</pre>
 *
 * In that file, place a fully qualified class name of your class that implements
 * {@link Configurator} interface. It should be called first time you
 * reference an {@link Eid}, or and of the eid exceptions, or utility
 * preconditions class.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @see ServiceLoader
 */
public interface Configurator {

    /**
     * Configures an Eid configuration.
     *
     * @param configuration a configuration to be configured
     */
    void configure(ConfigurationBuilder configuration);
}
