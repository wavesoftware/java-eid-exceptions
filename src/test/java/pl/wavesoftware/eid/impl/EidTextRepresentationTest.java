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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-11-14
 */
public class EidTextRepresentationTest {

    @Test
    public void raceTheSun() throws InterruptedException, ExecutionException {
        // given
        final Eid eid = new Eid("20181114:230849");
        Configuration configuration = Eid.getBinding()
            .getConfigurationSystem()
            .getConfiguration();
        TextMessage textMessage = new TextMessage(
            configuration,
            "Example {0}",
            new Object[]{new Date(2048000L)}
        );
        final EidTextRepresentation representation = new EidTextRepresentation(
            eid,
            textMessage,
            configuration
        );
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() {
                return representation.get();
            }
        };
        Collection<Callable<String>> tasks =
            new ArrayList<Callable<String>>(20);
        for (int i = 0; i < 20; i++) {
            tasks.add(task);
        }
        executorService.invokeAll(tasks);

        // when
        List<Future<String>> executed = executorService.invokeAll(tasks);

        // then
        Set<String> collected = new HashSet<String>();
        for (Future<String> future : executed) {
            collected.add(future.get());
        }
        assertThat(collected).hasSize(1);
    }

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
