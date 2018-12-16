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
import pl.wavesoftware.eid.Eid;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2015-11-19
 */
public class EidIndexOutOfBoundsExceptionTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final ConstantUniqueIdRule uniqueIdRule = new ConstantUniqueIdRule(
        "deadfa11"
    );

    private final String causeString = "Index seams to be invalid";

    @Test
    public void throwWithEidWithRefAndCause() {
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
        throw new EidIndexOutOfBoundsException(new Eid(eid, ref), getCause());
    }

    @Test
    public void throwWithEidWithRef() {
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
    public void throwWithStringAndCause() {
        // given
        String eid = "20151119:103232";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103232]<deadfa11> => " +
            "Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, getCause());
    }

    @Test
    public void throwWithEidAndCause() {
        // given
        String eidNum = "20151119:103245";
        Eid eid = new Eid(eidNum);

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            ArrayIndexOutOfBoundsException.class
        ));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103245]<deadfa11> => " +
            "Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, getCause());
    }

    @Test
    public void throwWithString() {
        // given
        String eid = "20181216:233656";

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181216:233656]<deadfa11>");

        // when
        throw new EidIndexOutOfBoundsException(eid);
    }

    @Test
    public void throwWithStringAndString() {
        // given
        String eid = "20181216:233958";
        String message = "This is a message";

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181216:233958]<deadfa11> => " +
            "This is a message");

        // when
        throw new EidIndexOutOfBoundsException(eid, message);
    }

    @Test
    public void throwWithEidMessage() {
        // given
        String eid = "20181216:235124";
        String message = "This is {0} message";

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181216:235124]<deadfa11> => " +
            "This is a message");

        // when
        throw new EidIndexOutOfBoundsException(
            Eid.eid(eid).message(message, new A())
        );
    }

    @Test
    public void throwWithStringStringAndCause() {
        // given
        String eid = "20181216:235554";
        String message = "This is a message";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181216:235554]<deadfa11> => " +
            "This is a message");
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            ArrayIndexOutOfBoundsException.class
        ));

        // when
        throw new EidIndexOutOfBoundsException(
            eid, message, getCause()
        );
    }

    @Test
    public void throwWithEidMessageAndCause() {
        // given
        String eid = "20181216:235729";
        String message = "This is {0} message";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181216:235729]<deadfa11> => " +
            "This is a message");
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            ArrayIndexOutOfBoundsException.class
        ));

        // when
        throw new EidIndexOutOfBoundsException(
            Eid.eid(eid).message(message, new A()), getCause()
        );
    }

    @Test
    public void throwWithEidAndMessage() {
        // given
        Eid eid = Eid.eid("20181217:001519");
        String message = "This is a message";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181217:001519]<deadfa11> => " +
            "This is a message");
        thrown.expectCause(nullValue(Throwable.class));

        // when
        throw new EidIndexOutOfBoundsException(
            eid, message
        );
    }

    @Test
    public void throwWithEidAndMessageAndCause() {
        // given
        Eid eid = Eid.eid("20181217:001727");
        String message = "This is a message";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20181217:001727]<deadfa11> => " +
            "This is a message");
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            ArrayIndexOutOfBoundsException.class
        ));

        // when
        throw new EidIndexOutOfBoundsException(
            eid, message, getCause()
        );
    }

    private Throwable getCause() {
        return new ArrayIndexOutOfBoundsException(causeString);
    }
}
