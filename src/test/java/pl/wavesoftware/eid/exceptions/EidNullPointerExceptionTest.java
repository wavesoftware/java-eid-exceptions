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
import pl.wavesoftware.eid.ConstantUniqueIdRule;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.api.EidMessage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2015-11-19
 */
public class EidNullPointerExceptionTest {

    private final String constUniq = "deadcafe";
    private final String causeString = "A cause";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public ConstantUniqueIdRule uniqueIdRule =
        new ConstantUniqueIdRule(constUniq);

    @Test
    public void throwWithString() {
        // given
        String eid = "20181217:220721";

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20181217:220721]<deadcafe>");

        // when
        throw new EidNullPointerException(eid);
    }

    @Test
    public void throwWithStringAndCause() {
        // given
        String eid = "20151119:101810";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            UnsupportedOperationException.class
        ));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:101810]<deadcafe> => A cause");

        // when
        throw new EidNullPointerException(eid, getCause());
    }

    @Test
    public void throwWithEidAndCause() {
        // given
        String eidNum = "20151119:102150";
        DefaultEid eid = new DefaultEid(eidNum);

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(UnsupportedOperationException.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:102150]<deadcafe> => A cause");

        // when
        throw new EidNullPointerException(eid, getCause());
    }

    @Test
    public void throwWithEidAndNullCause() {
        // given
        String eidNum = "20181217:222651";
        DefaultEid eid = new DefaultEid(eidNum);
        Throwable cause = null;

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20181217:222651]<deadcafe>");

        // when
        throw new EidNullPointerException(eid, cause);
    }

    @Test
    public void throwWithEidAndRefAndCause() {
        // given
        String eid = "20151119:100854";
        String ref = "PL-9584";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            UnsupportedOperationException.class
        ));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:100854|PL-9584]<deadcafe> => A cause");

        // when
        throw new EidNullPointerException(new DefaultEid(eid, ref), getCause());
    }

    @Test
    public void throwWithEid() {
        // given
        String eid = "20151119:100854";
        String ref = "PL-9584";

        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:100854|PL-9584]<deadcafe>");

        // when
        throw new EidNullPointerException(new DefaultEid(eid, ref));
    }

    @Test
    public void throwWithEidMessageAndCause() {
        // given
        String eid = "20181217:220935";
        EidMessage message = DefaultEid.eid(eid)
            .message("This is {0} message", new A());

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            UnsupportedOperationException.class
        ));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(
            "[20181217:220935]<deadcafe> => This is a message"
        );

        // when
        throw new EidNullPointerException(message, getCause());
    }

    @Test
    public void throwWithEidAndMessage() {
        // given
        Eid eid = DefaultEid.eid("20181217:221725");
        String message = "This is a message";

        // then
        thrown.expectCause(nullValue(Throwable.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(
            "[20181217:221725]<deadcafe> => This is a message"
        );

        // when
        throw new EidNullPointerException(eid, message);
    }

    @Test
    public void throwWithEidAndMessageAndCause() {
        // given
        DefaultEid eid = new DefaultEid("20181217:221901");
        String message = "This is a message";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(
            UnsupportedOperationException.class
        ));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage(
            "[20181217:221901]<deadcafe> => This is a message"
        );

        // when
        throw new EidNullPointerException(eid, message, getCause());
    }

    private Throwable getCause() {
        return new UnsupportedOperationException(causeString);
    }
}
