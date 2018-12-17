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
 * Implementing this validator and configuring it can help you assure that every
 * Eid in your project, conform to your rules.
 * <p><p>
 * Keep in mind that validation adds a little bit of cost, to every Eid creation,
 * even if that particular Eid don't turn up to be used.
 * <p><p>
 * To use your {@code Validator}, use {@link Configurator}.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @see Configurator
 */
public interface Validator {
    /**
     * Checks an ID that will be used as an Exception ID.
     *
     * @param id an id to check
     * @return true, if given ID is valid
     */
    boolean isValid(CharSequence id);
}
