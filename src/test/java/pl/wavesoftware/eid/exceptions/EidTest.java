/*
 * Copyright 2015 Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.wavesoftware.eid.exceptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.exceptions.Eid.UniqIdGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class EidTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSetMessageFormat_Null() {
        try {
            // given
            String format = null;
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Format can't be null, but just received one");
            // when
            Eid.setMessageFormat(format);
        } finally {
            Eid.setMessageFormat(Eid.DEFAULT_MESSAGE_FORMAT);
        }
    }

    @Test
    public void testSetMessageFormat_Invalid() {
        try {
            // given
            String format = "%s -> s";
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Given format contains to little format specifiers, expected 2 but given \"%s -> s\"");
            // when
            Eid.setMessageFormat(format);
        } finally {
            Eid.setMessageFormat(Eid.DEFAULT_MESSAGE_FORMAT);
        }
    }

    @Test
    public void testSetUniqIdGenerator() {
        try {
            // given
            UniqIdGenerator generator = new UniqIdGenerator() {

                @Override
                public String generateUniqId() {
                    fail("Generator should not be executed while validating");
                    return "constant";
                }
            };
            // when
            UniqIdGenerator previous = Eid.setUniqIdGenerator(generator);
            UniqIdGenerator set = Eid.setUniqIdGenerator(previous);
            // then
            assertThat(set).isSameAs(generator);
            assertThat(previous).isSameAs(Eid.DEFAULT_UNIQ_ID_GENERATOR);
        } finally {
            Eid.setUniqIdGenerator(Eid.DEFAULT_UNIQ_ID_GENERATOR);
        }
    }

    @Test
    public void testSetUniqIdGenerator_Null() {
        try {
            // given
            UniqIdGenerator generator = null;
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Unique ID generator can't be null, but given one");
            // when
            Eid.setUniqIdGenerator(generator);
        } finally {
            Eid.setUniqIdGenerator(Eid.DEFAULT_UNIQ_ID_GENERATOR);
        }
    }

    @Test
    public void testSetFormat_Null() {
        try {
            // given
            String format = null;
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Format can't be null, but just received one");
            // when
            Eid.setFormat(format);
        } finally {
            Eid.setFormat(Eid.DEFAULT_FORMAT);
        }
    }

    @Test
    public void testSetFormat_Invalid() {
        try {
            // given
            String format = "";
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Given format contains to little format specifiers, expected 2 but given \"\"");
            // when
            Eid.setFormat(format);
        } finally {
            Eid.setFormat(Eid.DEFAULT_FORMAT);
        }
    }

    @Test
    public void testSetFormat() {
        try {
            // given
            String format = "%s  %s";
            // when
            String old = Eid.setFormat(format);
            String set = Eid.setFormat(Eid.DEFAULT_FORMAT);
            // then
            assertThat(old).isEqualTo(Eid.DEFAULT_FORMAT);
            assertThat(set).isEqualTo(format);
        } finally {
            Eid.setFormat(Eid.DEFAULT_FORMAT);
        }
    }

    @Test
    public void testSetRefFormat() {
        try {
            // given
            String refFormat = "%s - %s - %s";
            // when
            String old = Eid.setRefFormat(refFormat);
            String set = Eid.setRefFormat(Eid.DEFAULT_REF_FORMAT);
            // then
            assertThat(old).isEqualTo(Eid.DEFAULT_REF_FORMAT);
            assertThat(set).isEqualTo(refFormat);
        } finally {
            Eid.setRefFormat(Eid.DEFAULT_REF_FORMAT);
        }
    }

    @Test
    public void testSetRefFormat_Null() {
        try {
            // given
            String refFormat = null;
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Format can't be null, but just received one");
            // when
            Eid.setRefFormat(refFormat);
        } finally {
            Eid.setRefFormat(Eid.DEFAULT_REF_FORMAT);
        }
    }

    @Test
    public void testSetRefFormat_Invalid() {
        try {
            // given
            String refFormat = "%s -> s";
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Given format contains to little format specifiers, expected 3 but given \"%s -> s\"");
            // when
            Eid.setRefFormat(refFormat);
        } finally {
            Eid.setRefFormat(Eid.DEFAULT_REF_FORMAT);
        }
    }

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
        Eid instance = new Eid("20150718:013236");
        String expResult = "";
        // when
        String result = instance.getRef();
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetUniq() {
        String id = "20150718:013443";
        Eid instance = new Eid(id);
        Eid instance2 = new Eid(id);
        String result = instance.getUniq();
        String result2 = instance2.getUniq();
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
        String message = eid.makeLogMessage("Files: %d", filesNumber);

        // then
        assertThat(message).contains("[20151117:192211]<", "> => Files: 18");
    }

}
