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

package pl.wavesoftware.eid.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public class EidExecutionsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final String eid = "20181029:230523";

    @Test
    public void testTryToExecute_UnsafeProcedure_String() {
        // given
        final String causeMessage = "An error occured while parsing JSON document at char 178";
        UnsafeProcedure procedure = new UnsafeProcedure() {
            @Override
            public void execute() throws ParseException {
                throw new ParseException(causeMessage, 178);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(ParseException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidExecutions.tryToExecute(procedure, eid);
    }

    @Test
    public void testTryToExecute_UnsafeProcedure_String_Ok() {
        // given
        UnsafeProcedure procedure = new UnsafeProcedure() {
            @Override
            public void execute() {
                // nothing special here, for unit test
            }
        };
        // when
        EidExecutions.tryToExecute(procedure, eid);
        // then
        assertThat(procedure).isNotNull();
    }

    @Test
    public void testTryToExecute_UnsafeProcedure_Eid() {
        // given
        final String causeMessage = "An error occured while parsing JSON document at char 178";
        UnsafeProcedure procedure = new UnsafeProcedure() {
            @Override
            public void execute() throws ParseException {
                throw new ParseException(causeMessage, 178);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(ParseException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidExecutions.tryToExecute(procedure, new DefaultEid(eid));
    }

    @Test
    public void testTryToExecute_UnsafeProcedure_Eid_Ok() {
        // given
        UnsafeProcedure procedure = new UnsafeProcedure() {
            @Override
            public void execute() {
                // nothing special here, for unit test
            }
        };
        // when
        EidExecutions.tryToExecute(procedure, new DefaultEid(eid));
        // then
        assertThat(procedure).isNotNull();
    }

    @Test
    public void testTryToExecute_UnsafeSupplier_String() {
        // given
        final String causeMessage = "An error occured while parsing JSON document at char 178";
        UnsafeSupplier<String> supplier = new UnsafeSupplier<String>() {

            @Override
            public String get() throws ParseException {
                throw new ParseException(causeMessage, 178);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(ParseException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidExecutions.tryToExecute(supplier, eid);
    }

    @Test
    public void testTryToExecute_UnsafeSupplier_String_Ok() {
        // given
        final String returning = "An answer to universe and everything";
        UnsafeSupplier<String> supplier = new UnsafeSupplier<String>() {
            @Override
            public String get() throws ParseException {
                return returning;
            }
        };
        // when
        String answer = EidExecutions.tryToExecute(supplier, eid);
        // then
        assertThat(supplier).isNotNull();
        assertThat(answer).isNotNull().isNotEmpty().isEqualTo(returning);
    }

    @Test
    public void testTryToExecute_UnsafeSupplier_Eid() {
        // given
        final String causeMessage = "An error occured while parsing JSON document at char 178";
        UnsafeSupplier<String> supplier = new UnsafeSupplier<String>() {

            @Override
            public String get() throws ParseException {
                throw new ParseException(causeMessage, 178);
            }
        };
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(isA(ParseException.class));
        thrown.expectCause(hasMessage(equalTo(causeMessage)));
        // when
        EidExecutions.tryToExecute(supplier, new DefaultEid(eid));
    }

    @Test
    public void testTryToExecute_UnsafeSupplier_Eid_Ok() {
        // given
        final String returning = "An answer to universe and everything";
        UnsafeSupplier<String> supplier = new UnsafeSupplier<String>() {
            @Override
            public String get() throws ParseException {
                return returning;
            }
        };
        // when
        String answer = EidExecutions.tryToExecute(supplier, new DefaultEid(eid));
        // then
        assertThat(supplier).isNotNull();
        assertThat(answer).isNotNull().isNotEmpty().isEqualTo(returning);
    }
}
