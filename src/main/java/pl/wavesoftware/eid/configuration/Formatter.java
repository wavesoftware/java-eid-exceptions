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

/**
 * Represents a formatter that will be used to display an Eid number in logs
 * or screen.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface Formatter {
    /**
     * Formats an Eid number to string
     *
     * @param eid an eid number
     * @return a string with formatted Eid number
     */
    String format(Eid eid);

    /**
     * Formats an Eid paired with a message.
     *
     * @param eid     an eid number to format
     * @param message a message to be pair to eid
     * @return a string with formatted eid and message
     */
    String format(Eid eid, String message);
}
