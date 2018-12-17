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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.wavesoftware.eid.DefaultEid;

/**
 * @author Krzysztof Suszyński <krzyszto.suszynski@wavesoftware.pl>
 * @since 21.07.15
 */
public class AssertionsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testJdkAssertions() {
        // then
        thrown.expect(AssertionError.class);

        // when
        assert getTestNumber() < 3 : new DefaultEid("20150721:101958");
    }

    private int getTestNumber() {
        return 3 * 5;
    }
}
