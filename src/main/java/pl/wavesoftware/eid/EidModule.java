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

package pl.wavesoftware.eid;

import pl.wavesoftware.eid.configuration.Binding;

import java.util.ServiceLoader;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
enum EidModule {
    MODULE;

    private final Binding binding;

    EidModule() {
        ServiceLoader<Binding> loader = ServiceLoader.load(Binding.class);
        binding = chooseImplementation(loader);
    }

    Binding getBinding() {
        return binding;
    }

    private static Binding chooseImplementation(Iterable<Binding> bindings) {
        Binding best = null;
        for (Binding bond : bindings) {
            if (best == null || !isCoreImplementation(bond)) {
                best = bond;
            }
        }
        if (best == null) {
            throw new IllegalStateException("20181106:000523");
        }
        return best;
    }

    private static boolean isCoreImplementation(Binding bond) {
        return bond.getClass()
            .getPackage()
            .getName()
            .contains(
                EidModule.class.getPackage().getName()
            );
    }

}
