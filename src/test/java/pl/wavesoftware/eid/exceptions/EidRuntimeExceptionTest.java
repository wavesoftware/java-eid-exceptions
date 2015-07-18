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
package pl.wavesoftware.eid.exceptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public class EidRuntimeExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSetMessageFormat_Null() {
        try {
            // given
            String format = null;
            // then
            thrown.expect(NullPointerException.class);
            thrown.expectMessage("Format can't be null, but just recieved one");
            // when
            EidRuntimeException.setMessageFormat(format);
        } finally {
            EidRuntimeException.setMessageFormat(EidRuntimeException.DEFAULT_MESSAGE_FORMAT);
        }
    }

    @Test
    public void testSetMessageFormat_Invalid() {
        try {
            // given
            String format = "%s -> s";
            // then
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("Given format contains to little format specifiers, expected 2 but given \"%s -> s\"");
            // when
            EidRuntimeException.setMessageFormat(format);
        } finally {
            EidRuntimeException.setMessageFormat(EidRuntimeException.DEFAULT_MESSAGE_FORMAT);
        }
    }

}
