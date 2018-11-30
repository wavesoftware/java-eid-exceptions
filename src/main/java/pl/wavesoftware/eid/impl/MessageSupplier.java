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

import pl.wavesoftware.eid.configuration.Configuration;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class MessageSupplier
    implements Supplier<String> {

    private final Configuration configuration;
    private final CharSequence messageFormat;
    private final Object[] arguments;

    MessageSupplier(
        Configuration configuration,
        CharSequence messageFormat,
        Object[] arguments
    ) {
        this.configuration = configuration;
        this.messageFormat = messageFormat;
        this.arguments = arguments.clone();
    }

    @Override
    public String get() {
        return getFormatter().format(arguments);
    }

    private MessageFormat getFormatter() {
        @Nullable
        Locale locale = configuration.getLocale();
        MessageFormat format;
        if (locale == null) {
            format = new MessageFormat(messageFormat.toString());
        } else {
            format = new MessageFormat(messageFormat.toString(), locale);
        }
        return ensureTimeZone(format);
    }

    private MessageFormat ensureTimeZone(MessageFormat messageFormat) {
        @Nullable
        TimeZone zone = configuration.getTimeZone();
        if (zone != null) {
            Object[] formats = messageFormat.getFormats();
            for (Object format : formats) {
                if (format instanceof SimpleDateFormat) {
                    ((SimpleDateFormat) format).setTimeZone(zone);
                }
            }
        }
        return messageFormat;
    }
}
