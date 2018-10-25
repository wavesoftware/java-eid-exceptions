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

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.configuration.Formatter;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultFormatter implements Formatter {

    private static final String FORMAT     = "[%s]<%s>";
    private static final String REF_FORMAT = "[%s|%s]<%s>";
    private static final String MSG_FORMAT = "%s => %s";

    @Override
    public String format(Eid eid) {
        if (eid.getRef() == null) {
            return String.format(
                FORMAT,
                eid.getId(),
                eid.getUnique()
            );
        }
        return String.format(
            REF_FORMAT,
            eid.getId(),
            eid.getRef(),
            eid.getUnique()
        );
    }

    @Override
    public String format(Eid eid, String message) {
        return String.format(
            MSG_FORMAT,
            format(eid),
            message
        );
    }
}
