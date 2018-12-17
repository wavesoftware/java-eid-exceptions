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

package pl.wavesoftware.eid.exceptions;

import org.junit.Rule;
import org.junit.Test;
import pl.wavesoftware.eid.ConstantUniqueIdRule;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.api.EidMessage;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-16
 */
public class EidIllegalStateExceptionTest {
    @Rule
    public ConstantUniqueIdRule uniqueIdRule = new ConstantUniqueIdRule(
        "e9b1"
    );

    @Test
    public void constructOfCharSequence() {
        // given
        String eid = "20181216:192816";

        // when
        EidRuntimeException ex = new EidIllegalStateException(eid);

        // then
        assertThat(ex).hasNoCause()
            .hasMessage("[20181216:192816]<e9b1>");
    }

    @Test
    public void constructOfCharSequenceAndMessage() {
        // given
        String eid = "20181216:192843";
        String message = "This is message";

        // when
        EidRuntimeException ex = new EidIllegalStateException(eid, message);

        // then
        assertThat(ex).hasNoCause()
            .hasMessage("[20181216:192843]<e9b1> => This is message");
    }

    @Test
    public void constructOfEidMessage() {
        // given
        DefaultEid eid = new DefaultEid("20181216:192906");
        EidMessage message = eid.message(
            "This is {0} message", new A()
        );

        // when
        EidRuntimeException ex = new EidIllegalStateException(message);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:192906]<e9b1> => This is a message");
    }

    @Test
    public void constructOfCharSequenceAndMessageAndCause() {
        // given
        String eid = "20181216:192917";
        String message = "This is message";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalStateException(
            eid, message, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:192917]<e9b1> => This is message");
    }

    @Test
    public void constructOfEidMessageAndCause() {
        // given
        DefaultEid eid = new DefaultEid("20181216:192930");
        EidMessage message = eid.message(
            "This is {0} message", new A()
        );
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalStateException(message, cause);

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:192930]<e9b1> => This is a message");
    }

    @Test
    public void constructOfCharSequenceAndCause() {
        // given
        String eid = "20181216:192938";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalStateException(eid, cause);

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:192938]<e9b1> => A cause");
    }

    @Test
    public void constructOfEid() {
        // given
        Eid eid = DefaultEid.eid("20181216:192947");

        // when
        EidRuntimeException ex = new EidIllegalStateException(eid);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:192947]<e9b1>");
    }

    @Test
    public void constructOfEidAndMessage() {
        // given
        Eid eid = DefaultEid.eid("20181216:192956");
        String message = "A simple message";

        // when
        EidRuntimeException ex = new EidIllegalStateException(eid, message);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:192956]<e9b1> => A simple message");
    }

    @Test
    public void constructOfEidAndMessageAndCause() {
        // given
        Eid eid = DefaultEid.eid("20181216:193003");
        String message = "A simple message";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalStateException(
            eid, message, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:193003]<e9b1> => A simple message");
    }

    @Test
    public void constructOfEidAndCause() {
        // given
        Eid eid = DefaultEid.eid("20181216:193015");
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalStateException(
            eid, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:193015]<e9b1> => A cause");
    }

    private static Throwable getCause() {
        return new IOException("A cause");
    }
}
