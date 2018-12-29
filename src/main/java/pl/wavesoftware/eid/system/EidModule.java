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

import pl.wavesoftware.eid.api.Binding;

import java.util.ServiceLoader;

/**
 * Represents a EID library module, and it's configuration binding.
 * <p>
 * Utilizing {@link #getBinding()} method it is possible to reconfigure this
 * module to perform differently then the default behavior.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public enum EidModule {
    MODULE;

    private final Binding binding;

    EidModule() {
        ServiceLoader<Binding> loader = ServiceLoader.load(Binding.class);
        BindingChooser chooser = new BindingChooser();
        binding = chooser.chooseImplementation(loader);
    }

    /**
     * Provides a binding to Eid library. It can be used to reconfigure
     * configuration programmatically.
     *
     * @return a binding interface
     */
    public Binding getBinding() {
        return binding;
    }
}
