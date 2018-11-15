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

package pl.wavesoftware.eid.impl;

import org.junit.Test;
import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.EidMessage;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public class DefaultEidMessageTest {

    @Test
    public void testToString() {
        // given
        Eid eid = new Eid("20181114:225421");
        EidMessage message = eid.message(
            "An example message with {0,date} {0,time}",
            new Date(1024000L)
        );

        // when
        String repr = message.toString();
        CharSequence fomatted = message.getFormattedMessage();
        char forthChar = message.charAt(32);
        int len = message.length();
        CharSequence sub = message.subSequence(35, 38);

        // then
        assertThat(fomatted).isEqualTo("An example message with Jan 1, 1970 12:17:04 AM");
        assertThat(repr).contains(
            "[20181114:225421]<",
            "> => An example message with Jan 1, 1970 12:17:04 AM"
        );
        assertThat(forthChar).isEqualTo('e');
        assertThat(len).isEqualTo(76);
        assertThat(sub).isEqualTo("mpl");
    }
}
