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

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class TextMessage implements Serializable {
    private static final long serialVersionUID = 20181029231527L;

    private transient CharSequence messageFormat;
    private transient Object[] arguments;
    private String message;

    TextMessage(
        CharSequence messageFormat,
        Object[] arguments
    ) {
        this.messageFormat = messageFormat;
        this.arguments = arguments.clone();
    }

    String get() {
        if (message == null) {
            message = doGet();
            messageFormat = null;
            arguments = null;
        }
        return message;
    }

    private synchronized String doGet() {
        if (message == null) {
            @Nullable
            Locale locale = Eid.getBinding()
                .getConfigurationSystem()
                .getConfiguration()
                .getLocale();
            MessageFormat formatter = getFormatter(locale);
            return formatter.format(arguments);
        }
        return message;
    }

    private MessageFormat getFormatter(@Nullable Locale locale) {
        if (locale == null) {
            return new MessageFormat(messageFormat.toString());
        } else {
            return new MessageFormat(messageFormat.toString(), locale);
        }
    }

    /**
     * Ensures that the value is evaluated before serialization.
     *
     * @param stream An object serialization stream.
     * @throws java.io.IOException If an error occurs writing to the stream.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        // evaluates the values if it isn't evaluated yet!
        get();
        stream.defaultWriteObject();
    }
}
