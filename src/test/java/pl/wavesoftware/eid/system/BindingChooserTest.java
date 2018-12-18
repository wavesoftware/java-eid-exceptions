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

package pl.wavesoftware.eid.system;

import org.junit.Test;
import pl.wavesoftware.eid.api.Binding;
import pl.wavesoftware.eid.impl.BindingImpl;
import pl.wavesoftware.eid.impl.TestBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-18
 */
public class BindingChooserTest {

    @Test
    public void testChooseImplementation() {
        // given
        BindingChooser chooser = new BindingChooser();
        Binding testBinding = new TestBinding();
        Binding binding = new BindingImpl();
        List<Binding> bindings = Arrays.asList(
            testBinding,
            binding
        );
        Collections.shuffle(bindings);

        // when

        Binding chosen = chooser.chooseImplementation(bindings);

        // then
        assertThat(chosen).isSameAs(testBinding);
    }

    @Test
    public void testChooseImplementationOnSingle() {
        // given
        BindingChooser chooser = new BindingChooser();

        // when
        Binding binding = new BindingImpl();
        Binding chosen = chooser.chooseImplementation(Collections.singletonList(
            binding
        ));

        // then
        assertThat(chosen).isSameAs(binding);
    }

    @Test(expected = AssertionError.class)
    public void testChooseImplementationOnEmpty() {
        // given
        BindingChooser chooser = new BindingChooser();

        // when
        Binding chosen = chooser.chooseImplementation(new ArrayList<Binding>());

        // then
        assertThat(chosen).isNull();
    }
}
