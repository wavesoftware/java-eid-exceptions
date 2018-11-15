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
import pl.wavesoftware.eid.configuration.Configuration;
import pl.wavesoftware.eid.configuration.Formatter;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultFormatter implements Formatter {

    private static final String FORMAT = "[%s]<%s>";
    private static final String REF_FORMAT = "[%s|%s]<%s>";
    private static final String MSG_FORMAT = "%s => %s";

    private final Configuration configuration;

    DefaultFormatter(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String format(Eid eid) {
        String fmt;
        Object[] params;
        if (eid.getRef() == null) {
            fmt = FORMAT;
            params = new Object[]{
                eid.getId(),
                eid.getUnique()
            };
        } else {
            fmt = REF_FORMAT;
            params = new Object[]{
                eid.getId(),
                eid.getRef(),
                eid.getUnique()
            };
        }
        java.util.Formatter formatter = getStringFormatter()
            .format(fmt, params);
        return formatter.toString();
    }

    @Override
    public String format(Eid eid, String message) {
        return getStringFormatter()
            .format(
                MSG_FORMAT,
                format(eid),
                message
            ).toString();
    }

    private java.util.Formatter getStringFormatter() {
        @Nullable
        Locale locale = configuration.getLocale();
        if (locale != null) {
            return new java.util.Formatter(locale);
        } else {
            return new java.util.Formatter();
        }
    }
}
