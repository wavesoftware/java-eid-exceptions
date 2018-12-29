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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.naming.NamingException;
import java.util.UnknownFormatConversionException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2015-10-07
 */
public class EidRuntimeExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetMessage() {
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(NamingException.class));
        thrown.expectCause(hasMessage(is((String) null)));
        thrown.expectMessage(containsString("20151007:212217"));
        thrown.expectMessage(containsString(
            "javax.naming.NamingException [Root exception is java.util" +
                ".UnknownFormatConversionException: Conversion = Invalid for" +
                " unit test]"
        ));

        // given
        Throwable original = new UnknownFormatConversionException("Invalid for unit test");
        NamingException cause = new NamingException(null);
        cause.setRootCause(original);
        throw new EidRuntimeException("20151007:212217", cause);
    }

}
