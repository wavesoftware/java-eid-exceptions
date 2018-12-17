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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-03
 */
public class InternalChecksTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void instantinate() throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException,
        InstantiationException {
        // when
        Constructor<InternalChecks> constr =
            InternalChecks.class.getDeclaredConstructor();
        constr.setAccessible(true);
        InternalChecks checks = constr.newInstance();

        // then
        assertThat(checks).isNotNull();
    }

    @Test
    public void testCheckNotNull() {
        // given
        String eid = "20181203:223812";
        String val = System.getProperty("non-existing-key-in-system");

        // then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(eid);

        // when
        InternalChecks.checkNotNull(val, eid);
    }
}
