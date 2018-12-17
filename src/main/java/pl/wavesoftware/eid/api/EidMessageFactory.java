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
 * Represents a factory for Eid messages.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface EidMessageFactory {
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
    EidMessage create(
        Eid eid,
        CharSequence messageTemplate,
        Object[] templateArguments
    );
}
