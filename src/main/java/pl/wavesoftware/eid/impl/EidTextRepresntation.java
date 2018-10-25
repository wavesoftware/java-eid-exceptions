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
import pl.wavesoftware.eid.EidContainer;
import pl.wavesoftware.eid.configuration.Formatter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class EidTextRepresntation implements EidContainer, Serializable {
    private static final long serialVersionUID = 20181029231519L;

    private final Eid eid;
    private final TextMessage textMessage;
    private transient Formatter formatter;
    private String actual;

    EidTextRepresntation(
        Eid eid,
        TextMessage textMessage,
        Formatter formatter
    ) {
        this.eid = eid;
        this.textMessage = textMessage;
        this.formatter = formatter;
    }

    @Override
    public Eid getEid() {
        return eid;
    }

    TextMessage getTextMessage() {
        return textMessage;
    }

    String get() {
        if (actual == null) {
            actual = doGet();
            formatter = null;
        }
        return actual;
    }

    private synchronized String doGet() {
        if (actual == null) {
            return formatter.format(eid, textMessage.get());
        }
        return actual;
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
