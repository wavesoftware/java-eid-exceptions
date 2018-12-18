/*
 * Copyright (c) 2015 Wave Software
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
package pl.wavesoftware.eid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.api.EidMessage;

import java.util.ArrayList;
import java.util.Collection;
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
 * @since 2.0.0
 */
public class EidTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidNullEid() {
        // given
        String eid = null;

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Exception ID can't be null");

        // when
        DefaultEid.eid(eid);
    }

    @Test
    public void invalidEid() {
        // given
        String eid = "1234";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ID given as an Exception ID: 1234");

        // when
        DefaultEid.eid(eid);
    }

    @Test
    public void testToString() {
        // given
        DefaultEid instance = new DefaultEid("20150718:012917");
        String expResult = "[20150718:012917]";
        // when
        String result = instance.toString();
        // then
        assertThat(result).contains(expResult);
        assertThat(result).matches("^\\[20150718:012917\\]<[a-zA-Z0-9_-]+>$");
    }

    @Test
    public void testToString_Ref() {
        // given
        DefaultEid instance = new DefaultEid("20150718:013056", "ORA-38101");
        String expResult = "[20150718:013056|ORA-38101]";
        // when
        String result = instance.toString();
        // then
        assertThat(result).contains(expResult);
    }

    @Test
    public void testGetId() {
        // given
        DefaultEid instance = new DefaultEid("20150718:013209");
        String expResult = "20150718:013209";
        // when
        String result = instance.getId();
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetRef() {
        // given
        DefaultEid instance = new DefaultEid("20150718:013314", "ORA-38105");
        String expResult = "ORA-38105";
        // when
        String result = instance.getRef();
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetRef_Null() {
        // given
        Eid instance = DefaultEid.eid("20150718:013236");
        // when
        String result = instance.getRef();
        // then
        assertThat(result).isNull();
    }

    @Test
    public void testGetUniq() {
        String id = "20150718:013443";
        DefaultEid instance = new DefaultEid(id);
        DefaultEid instance2 = new DefaultEid(id);
        String result = instance.getUnique();
        String result2 = instance2.getUnique();
        assertThat(result).isNotEmpty();
        assertThat(result).isNotEqualTo(result2);
    }

    @Test
    public void message() {
        // given
        String code = "20151117:192211";
        DefaultEid eid = new DefaultEid(code);
        int filesNumber = 18;

        // when
        EidMessage message = eid.message("Files: {0}", filesNumber);

        // then
        assertThat(message).contains("[20151117:192211]<", "> => Files: 18");
    }

    @Test
    public void raceTheSun() throws InterruptedException, ExecutionException {
        // given
        final DefaultEid eid = new DefaultEid("20181114:223748");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() {
                return eid.getUnique();
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

}
