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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public class EidTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testToString() {
        // given
        Eid instance = new Eid("20150718:012917");
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
        Eid instance = new Eid("20150718:013056", "ORA-38101");
        String expResult = "[20150718:013056|ORA-38101]";
        // when
        String result = instance.toString();
        // then
        assertThat(result).contains(expResult);
    }

    @Test
    public void testGetId() {
        // given
        Eid instance = new Eid("20150718:013209");
        String expResult = "20150718:013209";
        // when
        String result = instance.getId();
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetRef() {
        // given
        Eid instance = new Eid("20150718:013314", "ORA-38105");
        String expResult = "ORA-38105";
        // when
        String result = instance.getRef();
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetRef_Null() {
        // given
        Eid instance = Eid.eid("20150718:013236");
        // when
        String result = instance.getRef();
        // then
        assertThat(result).isNull();
    }

    @Test
    public void testGetUniq() {
        String id = "20150718:013443";
        Eid instance = new Eid(id);
        Eid instance2 = new Eid(id);
        String result = instance.getUnique();
        String result2 = instance2.getUnique();
        assertThat(result).isNotEmpty();
        assertThat(result).isNotEqualTo(result2);
    }

    @Test
    public void testMakeLogMessage() {
        // given
        String code = "20151117:192211";
        Eid eid = new Eid(code);
        int filesNumber = 18;

        // when
        EidMessage message = eid.message("Files: {0}", filesNumber);

        // then
        assertThat(message).contains("[20151117:192211]<", "> => Files: 18");
    }

}
