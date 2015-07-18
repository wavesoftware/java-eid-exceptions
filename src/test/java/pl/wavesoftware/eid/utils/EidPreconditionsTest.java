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
package pl.wavesoftware.eid.utils;

import java.io.InterruptedIOException;
import java.lang.reflect.Constructor;
import javax.annotation.Nonnull;
import static org.assertj.core.api.Assertions.assertThat;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.exceptions.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalArgumentException;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.eid.exceptions.EidIndexOutOfBoundsException;
import pl.wavesoftware.eid.exceptions.EidNullPointerException;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public class EidPreconditionsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String eid = "20150718:075046";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCheckArgument() {
        // given
        boolean expression = false;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkArgument(expression, eid);
    }

    @Test
    public void testCheckArgument_Ok() {
        // when
        boolean expression = true;
        // when
        EidPreconditions.checkArgument(expression, eid);
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckState() {
        // given
        boolean expression = false;
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkState(expression, eid);
    }

    @Test
    public void testCheckState_Ok() {
        // when
        boolean expression = true;
        // when
        EidPreconditions.checkState(expression, eid);
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckNotNull_Ok() {
        // given
        String reference = "test string ref";
        String expResult = reference + "";
        // when
        String result = EidPreconditions.checkNotNull(reference, eid);
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testCheckNotNull() {
        // given
        Object reference = null;
        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkNotNull(reference, eid);
    }

    @Test
    public void testCheckElementIndex_Ok() {
        // given
        int index = 2;
        int size = 10;
        Integer expResult = 2;
        // when
        int result = EidPreconditions.checkElementIndex(index, size, eid);
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testCheckElementIndex_Nulls() {
        // given
        Integer index = null;
        Integer size = null;
        Matcher<String> m = Matchers.equalTo(null);
        // then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(m);
        // when
        EidPreconditions.checkElementIndex(index, size, eid);
    }

    @Test
    public void testCheckElementIndex() {
        // given
        int index = -1;
        int size = 0;
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eid);
    }

    @Test
    public void testCheckElementIndex_Positive() {
        // given
        int index = 14;
        int size = 10;
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eid);
    }

    @Test
    public void testTryToExecute_Ok() {
        // given
        final String expResult = "test string";
        EidPreconditions.RiskyCode<String> riskyCode = new EidPreconditions.RiskyCode<String>() {

            @Override
            public String execute() {
                return expResult;
            }
        };
        // when
        String result = EidPreconditions.tryToExecute(riskyCode, eid);
        // then
        assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testTryToExecute() {
        // given
        final String causeMessage = "a cause message";
        EidPreconditions.RiskyCode<String> riskyCode = new EidPreconditions.RiskyCode<String>() {

            @Override
            public String execute() throws InterruptedIOException {
                throw new InterruptedIOException(causeMessage);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(InterruptedIOException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidPreconditions.tryToExecute(riskyCode, eid);
    }

    @Test
    public void testCreate() throws NoSuchMethodException {
        // given
        Class<EidPreconditions> cls = EidPreconditions.class;
        Constructor<?> constr = cls.getDeclaredConstructor(new Class<?>[0]);
        boolean access = constr.isAccessible();

        // then
        assertThat(access).isFalse();
        thrown.expect(EidRuntimeException.class);
        thrown.expect(new CustomMatcher<Throwable>("contains matching Eid object") {

            @Override
            public boolean matches(Object item) {
                Eid eidObject = EidRuntimeException.class.cast(item).getEid();
                return eidObject.getId().equals("20150718:083450")
                    && !eidObject.getRef().isEmpty()
                    && !eidObject.getUniq().isEmpty();
            }
        });
        thrown.expectMessage(containsString("[20150718:083450|This should not be accessed]"));
        // when
        assertThat(new EidPreconditions()).isNull();
    }

    @Test
    public void testCheckArgument_boolean_Eid_Null() {
        // given
        boolean expression = false;
        Eid eidObject = getNullEid();
        // then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Pass not-null Eid to EidPreconditions first!");
        // when
        EidPreconditions.checkArgument(expression, eidObject);
    }

    @Test
    public void testCheckArgument_boolean_Eid() {
        // given
        Eid eidObject = getEid();
        boolean expression = false;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkArgument(expression, eidObject);
    }

    @Test
    public void testCheckArgument_boolean_Eid_Ok() {
        // given
        Eid eidObject = getEid();
        boolean expression = true;
        // when
        EidPreconditions.checkArgument(expression, eidObject);
        // then
        assertThat(eidObject).isNotNull();
    }

    @Test
    public void testCheckState_boolean_Eid() {
        // given
        boolean expression = false;
        Eid eidObject = getEid();
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkState(expression, eidObject);
    }

    @Test
    public void testCheckState_boolean_Eid_Ok() {
        // given
        boolean expression = true;
        Eid eidObject = getEid();
        // when
        EidPreconditions.checkState(expression, eidObject);
        // then
        assertThat(eidObject).isNotNull();
    }

    @Test
    public void testCheckNotNull_GenericType_Eid() {
        // given
        Object reference = null;
        Eid eidObject = getEid();
        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkNotNull(reference, eidObject);
    }

    @Test
    public void testCheckNotNull_GenericType_Eid_Ok() {
        // given
        String reference = "ok";
        Eid eidObject = getEid();
        // when
        String result = EidPreconditions.checkNotNull(reference, eidObject);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("ok");
    }

    @Test
    public void testCheckElementIndex_3args_2() {
        // given
        int index = 5;
        int size = 0;
        Eid eidObject = getEid();
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eidObject);
    }

    @Test
    public void testCheckElementIndex_3args_2_Negative() {
        // given
        int index = -5;
        int size = 10;
        Eid eidObject = getEid();
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eidObject);
    }

    @Test
    public void testCheckElementIndex_3args_2_Ok() {
        // given
        int index = 1;
        int size = 120;
        Eid eidObject = getEid();
        // when
        int result = EidPreconditions.checkElementIndex(index, size, eidObject);
        // then
        assertThat(result).isEqualTo(index);
    }

    @Test
    public void testTryToExecute_EidPreconditionsRiskyCode_Eid() {
        // given
        final String causeMessage = "a cause message";
        Eid eidObject = getEid();
        EidPreconditions.RiskyCode<String> riskyCode = new EidPreconditions.RiskyCode<String>() {

            @Override
            public String execute() throws InterruptedIOException {
                throw new InterruptedIOException(causeMessage);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(InterruptedIOException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidPreconditions.tryToExecute(riskyCode, eidObject);
    }

    @Test
    public void testTryToExecute_EidPreconditionsRiskyCode_Eid_Ok() {
        // given
        final String tester = "unit message";
        Eid eidObject = getEid();
        EidPreconditions.RiskyCode<String> riskyCode = new EidPreconditions.RiskyCode<String>() {

            @Override
            public String execute() {
                return tester;
            }
        };
        // when
        String result = EidPreconditions.tryToExecute(riskyCode, eidObject);
        // then
        assertThat(result).isEqualTo(tester);
    }

    private Eid getNullEid() {
        return null;
    }

    @Nonnull
    private Eid getEid() {
        return new Eid(eid);
    }

}
