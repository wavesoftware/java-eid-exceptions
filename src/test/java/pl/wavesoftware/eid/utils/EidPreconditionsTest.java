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
package pl.wavesoftware.eid.utils;

import org.hamcrest.CoreMatchers;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.ConfiguratorRule;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.api.ConfigurationBuilder;
import pl.wavesoftware.eid.api.Configurator;
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalArgumentException;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.eid.exceptions.EidIndexOutOfBoundsException;
import pl.wavesoftware.eid.exceptions.EidNullPointerException;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

/**
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 */
public class EidPreconditionsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final ConfiguratorRule configurator = new ConfiguratorRule(new Configurator() {
        @Override
        public void configure(ConfigurationBuilder configuration) {
            configuration.locale(Locale.ENGLISH);
        }
    });

    private final String eid = "20150718:075046";

    @Test
    public void testCheckArgument() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkArgument(expression, eid);
    }

    @Test
    public void testCheckArgument_WithMessage() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("PI value is 3.14"));
        // when
        EidPreconditions.checkArgument(
            expression, eid,
            "PI value is {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckArgument_Ok_WithMessage() {
        // given
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkArgument(
            expression, eid,
            "PI value is {0,number,#.##}", Math.PI
        );
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckArgument_Eid_WithMessage() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("PI value is 3.14"));
        // when
        EidPreconditions.checkArgument(
            expression, new DefaultEid(eid),
            "PI value is {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckArgument_Eid_Ok_WithMessage() {
        // given
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkArgument(
            expression, new DefaultEid(eid),
            "PI value is {0,number,#.##}", Math.PI
        );
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckArgument_Ok() {
        // when
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkArgument(expression, eid);
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckState() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkState(expression, eid);
    }

    @Test
    public void testCheckState_WithMessage() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("PI is 3.1416"));
        // when
        EidPreconditions.checkState(
            expression, eid,
            "PI is {0,number,#.####}", Math.PI
        );
    }

    @Test
    public void testCheckState_Ok_WithMessage() {
        // given
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkState(
            expression, eid,
            "PI is {0,number,#.##}", Math.PI
        );
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckState_Eid_WithMessage() {
        // given
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("PI is 3.1416"));
        // when
        EidPreconditions.checkState(
            expression, new DefaultEid(eid),
            "PI is {0,number,#.####}", Math.PI
        );
    }

    @Test
    public void testCheckState_Ok_Eid_WithMessage() {
        // given
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkState(
            expression, new DefaultEid(eid),
            "PI is {0,number,#.##}", Math.PI
        );
        // then
        assertThat(thrown).isNotNull();
    }

    @Test
    public void testCheckState_Ok() {
        // when
        boolean expression = truthyValue();
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
        Object reference = nullyValue();
        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkNotNull(reference, eid);
    }

    @Test
    public void testCheckNotNull_WithMessage() {
        // given
        Object reference = nullyValue();
        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("π <=> 3.142"));
        // when
        EidPreconditions.checkNotNull(
            reference, eid,
            "π <=> {0,number,#.###}", Math.PI
        );
    }

    @Test
    public void testCheckNotNull_Ok_WithMessage() {
        // given
        Object reference = "A test";
        // when
        Object checked = EidPreconditions.checkNotNull(
            reference, eid,
            "π <=> {0,number,#.###}", Math.PI
        );
        // then
        assertThat(checked).isSameAs(reference);
    }

    @Test
    public void testCheckNotNull_Eid_WithMessage() {
        // given
        Object reference = nullyValue();
        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("π <=> 3.142"));
        // when
        EidPreconditions.checkNotNull(
            reference, new DefaultEid(eid),
            "π <=> {0,number,#.###}", Math.PI
        );
    }

    @Test
    public void testCheckNotNull_Ok_Eid_WithMessage() {
        // given
        Object reference = "A test";
        // when
        Object checked = EidPreconditions.checkNotNull(
            reference, new DefaultEid(eid),
            "π <=> {0,number,#.###}", Math.PI
        );
        // then
        assertThat(checked).isSameAs(reference);
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
        Integer index = nullyValue();
        Integer size = nullyValue();
        Matcher<String> m = CoreMatchers.equalTo(null);
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
    public void testCheckElementIndexOnGithubIssue15() {
        // given
        int index = 4;
        int size = 4;
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eid);
    }

    @Test
    public void testCheckElementIndex_WithMessage() {
        // given
        int index = -1;
        int size = 0;
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("Pi (π): 3.14"));
        // when
        EidPreconditions.checkElementIndex(
            index, size, eid,
            "Pi (π): {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckElementIndex_Eid_WithMessage() {
        // given
        int index = -1;
        int size = 0;
        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("Pi (π): 3.14"));
        // when
        EidPreconditions.checkElementIndex(
            index, size, new DefaultEid(eid),
            "Pi (π): {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckElementIndex_SizeIllegal_WithMessage() {
        // given
        int index = 0;
        int size = -45;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("Pi (π): 3.14"));
        // when
        EidPreconditions.checkElementIndex(
            index, size, eid,
            "Pi (π): {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckElementIndex_SizeIllegal_Eid_WithMessage() {
        // given
        int index = 0;
        int size = -45;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        thrown.expectMessage(containsString("Pi (π): 3.14"));
        // when
        EidPreconditions.checkElementIndex(
            index, size, new DefaultEid(eid),
            "Pi (π): {0,number,#.##}", Math.PI
        );
    }

    @Test
    public void testCheckElementIndex_Ok_WithMessage() {
        // given
        int index = 234;
        int size = 450;
        // when
        int checked = EidPreconditions.checkElementIndex(
            index, size, eid,
            "Pi (π): {0,number,#.#}", Math.PI
        );
        // then
        assertThat(checked).isEqualTo(index);
    }

    @Test
    public void testCheckElementIndex_Ok_Eid_WithMessage() {
        // given
        int index = 234;
        int size = 450;
        // when
        int checked = EidPreconditions.checkElementIndex(
            index, size, new DefaultEid(eid),
            "Pi (π): {0,number,#.#}", Math.PI
        );
        // then
        assertThat(checked).isEqualTo(index);
    }

    @Test
    public void testCheckElementIndex_SizeInvalid() {
        // given
        int index = 1;
        int size = -5;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, eid);
    }

    @Test
    public void testCheckElementIndex_Eid_SizeInvalid() {
        // given
        int index = 2;
        int size = -1;
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkElementIndex(index, size, new DefaultEid(eid));
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
    public void testCreate() throws NoSuchMethodException {
        // given
        Class<EidPreconditions> cls = EidPreconditions.class;
        Constructor<?> constructor = cls.getDeclaredConstructor();
        boolean access = Modifier.isPublic(constructor.getModifiers());

        // then
        assertThat(access).isFalse();
        thrown.expect(EidRuntimeException.class);
        thrown.expect(new CustomMatcher<Throwable>("contains matching Eid object") {

            @Override
            public boolean matches(Object item) {
                @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                Eid eidObject = ((EidRuntimeException) item).getEid();
                return eidObject.getId().equals("20150718:083450")
                    && !eidObject.hasRef()
                    && !eidObject.getUnique().isEmpty();
            }
        });
        thrown.expectMessage(containsString("[20150718:083450]"));
        thrown.expectMessage(containsString("This should not be accessed"));
        // when
        assertThat(new EidPreconditions()).isNull();
    }

    @Test
    public void testCheckArgument_boolean_Eid_Null() {
        // given
        boolean expression = falsyValue();
        DefaultEid eidObject = nullyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString("20160329:132823|EID-NULL"));
        // when
        EidPreconditions.checkArgument(expression, eidObject);
    }

    @Test
    public void testCheckArgument_boolean_String_Null() {
        // given
        boolean expression = falsyValue();
        String eidObject = nullyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString("20160329:133052|EID-NULL"));
        // when
        EidPreconditions.checkArgument(expression, eidObject);
    }

    @Test
    public void testCheckArgument_boolean_Eid() {
        // given
        DefaultEid eidObject = getEid();
        boolean expression = falsyValue();
        // then
        thrown.expect(EidIllegalArgumentException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkArgument(expression, eidObject);
    }

    @Test
    public void testCheckArgument_boolean_Eid_Ok() {
        // given
        DefaultEid eidObject = getEid();
        boolean expression = truthyValue();
        // when
        EidPreconditions.checkArgument(expression, eidObject);
        // then
        assertThat(eidObject).isNotNull();
    }

    @Test
    public void testCheckState_boolean_Eid() {
        // given
        boolean expression = falsyValue();
        DefaultEid eidObject = getEid();
        // then
        thrown.expect(EidIllegalStateException.class);
        thrown.expectMessage(containsString(eid));
        // when
        EidPreconditions.checkState(expression, eidObject);
    }

    @Test
    public void testCheckState_boolean_Eid_Ok() {
        // given
        boolean expression = truthyValue();
        DefaultEid eidObject = getEid();
        // when
        EidPreconditions.checkState(expression, eidObject);
        // then
        assertThat(eidObject).isNotNull();
    }

    @Test
    public void testCheckNotNull_GenericType_Eid() {
        // given
        Object reference = nullyValue();
        DefaultEid eidObject = getEid();
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
        DefaultEid eidObject = getEid();
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
        DefaultEid eidObject = getEid();
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
        DefaultEid eidObject = getEid();
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
        DefaultEid eidObject = getEid();
        // when
        int result = EidPreconditions.checkElementIndex(index, size, eidObject);
        // then
        assertThat(result).isEqualTo(index);
    }

    @Nonnull
    private DefaultEid getEid() {
        return new DefaultEid(eid);
    }

    private static Boolean truthyValue() {
        return Boolean.TRUE;
    }

    private static Boolean falsyValue() {
        return Boolean.FALSE;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <T> T nullyValue() {
        // This is hack to overcome Intellij null check :-P
        Nonnull ret = Object.class.getAnnotation(Nonnull.class);
        assertThat(ret).isNull();
        return (T) ret;
    }

}
