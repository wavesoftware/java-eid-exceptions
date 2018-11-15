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
import pl.wavesoftware.eid.EidMessage;
import pl.wavesoftware.eid.configuration.Configuration;

import java.io.Serializable;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultEidMessage implements EidMessage, Serializable {
    private static final long serialVersionUID = 20181029192322L;

    private final EidTextRepresentation represntation;

    DefaultEidMessage(
        Eid eid,
        Configuration configuration,
        CharSequence messageFormat,
        Object[] arguments
    ) {
        this.represntation = new EidTextRepresentation(
            eid,
            new TextMessage(configuration, messageFormat, arguments),
            configuration
        );
    }

    @Override
    public int length() {
        return represntation.get().length();
    }

    @Override
    public char charAt(int index) {
        return represntation.get().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return represntation.get().subSequence(start, end);
    }

    @Override
    public Eid getEid() {
        return represntation.getEid();
    }

    @Override
    public String toString() {
        return represntation.get();
    }

    @Override
    public CharSequence getFormattedMessage() {
        return represntation.getTextMessage().get();
    }

}
