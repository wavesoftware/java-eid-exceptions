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

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.EidMessage;

/**
 * A binding of Eid library. This interface can be used to completely
 * replace original binding with your custom implementation.
 *
 * <p>To replace implementation use {@link java.util.ServiceLoader} like:
 *
 * <pre>META-INF/services/pl.wavesoftware.eid.configuration.Binding</pre>
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
     * Create a Exception ID message, from Eid, message template and
     * template arguments.
     *
     * @param eid               an Exception ID object
     * @param messageTemplate   message template in form accepted by
     *                          {@link java.text.MessageFormat#format(String, Object...)}
     * @param templateArguments a template arguments to be used to interpolate
     *                          into message
     * @return a create message with Eid number
     */
    EidMessage createEidMessage(
        Eid eid,
        CharSequence messageTemplate,
        Object[] templateArguments
    );
}
