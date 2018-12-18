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

import pl.wavesoftware.eid.api.Configuration;
import pl.wavesoftware.eid.api.EidContainer;
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.api.Supplier;

import java.io.Serializable;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class EidTextRepresentation implements EidContainer, Serializable {
    private static final long serialVersionUID = 20181029231519L;

    private final Eid eid;
    private final TextMessage textMessage;
    private final SerializableLazy<String> actual;

    EidTextRepresentation(
        final Eid eid,
        final TextMessage textMessage,
        final Configuration configuration
    ) {
        this.eid = eid;
        this.textMessage = textMessage;
        this.actual = SerializableLazy.serializableOf(new Supplier<String>() {
            @Override
            public String get() {
                return configuration.getFormatter()
                    .format(eid, textMessage.get());
            }
        });
    }

    @Override
    public Eid getEid() {
        return eid;
    }

    TextMessage getTextMessage() {
        return textMessage;
    }

    String get() {
        return actual.get();
    }
}
