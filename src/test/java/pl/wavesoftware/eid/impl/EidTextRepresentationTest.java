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

import org.junit.Test;
import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.configuration.Configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-11-14
 */
public class EidTextRepresentationTest {

    @Test
    public void passivation() throws IOException, ClassNotFoundException {
        // given
        final Eid eid = new Eid("20181114:231205");
        Configuration configuration = Eid.getBinding()
            .getConfigurationSystem()
            .getConfiguration();
        TextMessage textMessage = new TextMessage(
            configuration,
            "Other {0}",
            new Object[]{new Date(3095000L)}
        );
        final EidTextRepresentation representation = new EidTextRepresentation(
            eid,
            textMessage,
            configuration
        );
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArray);

        // when
        objectOutputStream.writeObject(representation);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
            byteArray.toByteArray()
        );
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        EidTextRepresentation repr2 = (EidTextRepresentation) objectInputStream.readObject();

        // then
        assertThat(repr2.get()).isEqualTo(representation.get());
    }
}
