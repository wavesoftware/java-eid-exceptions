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
 * @since 2.0.0
 */
public class EidIllegalArgumentExceptionTest {

    @Rule
    public ConstantUniqueIdRule uniqueIdRule = new ConstantUniqueIdRule(
        "d3m0"
    );

    @Test
    public void constructOfCharSequence() {
        // given
        String eid = "20181215:000127";

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(eid);

        // then
        assertThat(ex).hasNoCause()
            .hasMessage("[20181215:000127]<d3m0>");
    }

    @Test
    public void constructOfCharSequenceAndMessage() {
        // given
        String eid = "20181215:000410";
        String message = "This is message";

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(eid, message);

        // then
        assertThat(ex).hasNoCause()
            .hasMessage("[20181215:000410]<d3m0> => This is message");
    }

    @Test
    public void constructOfEidMessage() {
        // given
        DefaultEid eid = new DefaultEid("20181216:175048");
        EidMessage message = eid.message(
            "This is {0} message", new A()
        );

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(message);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:175048]<d3m0> => This is a message");
    }

    @Test
    public void constructOfCharSequenceAndMessageAndCause() {
        // given
        String eid = "20181215:001337";
        String message = "This is message";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(
            eid, message, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181215:001337]<d3m0> => This is message");
    }

    @Test
    public void constructOfEidMessageAndCause() {
        // given
        DefaultEid eid = new DefaultEid("20181215:000410");
        EidMessage message = eid.message(
            "This is {0} message", new A()
        );
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(message, cause);

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181215:000410]<d3m0> => This is a message");
    }

    @Test
    public void constructOfCharSequenceAndCause() {
        // given
        String eid = "20181216:175040";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(eid, cause);

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:175040]<d3m0> => A cause");
    }

    @Test
    public void constructOfEid() {
        // given
        Eid eid = DefaultEid.eid("20181216:180054");

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(eid);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:180054]<d3m0>");
    }

    @Test
    public void constructOfEidAndMessage() {
        // given
        Eid eid = DefaultEid.eid("20181216:180212");
        String message = "A simple message";

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(eid, message);

        // then
        assertThat(ex)
            .hasNoCause()
            .hasMessage("[20181216:180212]<d3m0> => A simple message");
    }

    @Test
    public void constructOfEidAndMessageAndCause() {
        // given
        Eid eid = DefaultEid.eid("20181216:180355");
        String message = "A simple message";
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(
            eid, message, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:180355]<d3m0> => A simple message");
    }

    @Test
    public void constructOfEidAndCause() {
        // given
        Eid eid = DefaultEid.eid("20181216:191804");
        Throwable cause = getCause();

        // when
        EidRuntimeException ex = new EidIllegalArgumentException(
            eid, cause
        );

        // then
        assertThat(ex)
            .hasCauseInstanceOf(IOException.class)
            .hasMessage("[20181216:191804]<d3m0> => A cause");
    }

    private static IOException getCause() {
        return new IOException("A cause");
    }

}
