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

/**
 * A binding of Eid library. This interface can be used to completely
 * replace original binding with your custom implementation.
 *
 * <p>To replace implementation use {@link java.util.ServiceLoader} like:
 *
 * <pre>META-INF/services/pl.wavesoftware.eid.api.Binding</pre>
 * <p>
 * In that file, place a fully qualified class name of your class that implements
 * {@link Binding} interface. It should be called first time you reference an
 * {@link Eid}, or and of the eid exceptions, or utility preconditions class.
 *
 * <p>Usually it's way easier to just reconfigure existing configuration using
 * {@link Configurator} interface.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @see Configurator
 * @see java.util.ServiceLoader
 * @since 2.0.0
 */
public interface Binding {

    /**
     * Gets a configuration system
     *
     * @return a configuration system
     */
    ConfigurationSystem getConfigurationSystem();

    /**
     * Get bound Eid factories to be used to perform various Eid
     * related operations.
     *
     * @return a factories for various Eid related things.
     */
    EidFactories getFactories();
}
