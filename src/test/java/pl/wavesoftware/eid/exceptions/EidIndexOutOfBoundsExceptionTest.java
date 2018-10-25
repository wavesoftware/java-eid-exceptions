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

package pl.wavesoftware.eid.exceptions;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.configuration.ConfigurationBuilder;
import pl.wavesoftware.eid.configuration.Configurator;
import pl.wavesoftware.eid.configuration.UniqueIdGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2015-11-19
 */
public class EidIndexOutOfBoundsExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String constUniq = "deadfa11";
    private String causeString = "Index seams to be invalid";
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private Throwable cause = new ArrayIndexOutOfBoundsException(causeString);
    private Configurator original;

    @Before
    public void before() {
        original = Eid.getBinding().getConfigurationSystem().configure(new Configurator() {
            @Override
            public void configure(ConfigurationBuilder configuration) {
                configuration.uniqueIdGenerator(new UniqueIdGenerator() {
                    @Override
                    public String generateUniqId() {
                        return constUniq;
                    }
                });
            }
        });
    }

    @After
    public void after() {
        Eid.getBinding().getConfigurationSystem().configure(original);
    }

    @Test
    public void testGetStandardJdkClass() {
        // given
        @SuppressWarnings("ThrowableInstanceNeverThrown")
        EidIndexOutOfBoundsException ex = new EidIndexOutOfBoundsException(
            new Eid("20151119:103152")
        );

        // when
        Class<? extends RuntimeException> cls = ex.getJavaClass();

        // then
        assertThat(cls).isEqualTo(IndexOutOfBoundsException.class);
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_String_Throwable() {
        // given
        String eid = "20151119:103158";
        String ref = "MS+1233";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(
            "[20151119:103158|MS+1233]<deadfa11> => " +
                "Index seams to be invalid"
        );

        // when
        throw new EidIndexOutOfBoundsException(new Eid(eid, ref), cause);
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_String() {
        // given
        String eid = "20151119:103217";
        String ref = "MS+1233";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103217|MS+1233]<deadfa11>");

        // when
        throw new EidIndexOutOfBoundsException(new Eid(eid, ref));
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_Throwable() {
        // given
        String eid = "20151119:103232";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103232]<deadfa11> => Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, cause);
    }

    @Test
    public void testEidIndexOutOfBoundsException_Eid_Throwable() {
        // given
        String eidNum = "20151119:103245";
        Eid eid = new Eid(eidNum);

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103245]<deadfa11> => Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, cause);
    }
}
