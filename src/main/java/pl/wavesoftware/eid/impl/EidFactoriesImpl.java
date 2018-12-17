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

import pl.wavesoftware.eid.api.Binding;
import pl.wavesoftware.eid.api.EidFactories;
import pl.wavesoftware.eid.api.EidFactory;
import pl.wavesoftware.eid.api.EidMessageFactory;
import pl.wavesoftware.eid.api.LazyFactory;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-17
 */
final class EidFactoriesImpl implements EidFactories {
    private final EidMessageFactory eidMessageFactory;
    private final LazyFactory lazyFactory;
    private final EidFactory eidFactory;

    EidFactoriesImpl(Binding binding) {
        eidMessageFactory = new EidMessageFactoryImpl(binding);
        lazyFactory = new LazyFactoryImpl();
        eidFactory = new EidFactoryImpl();
    }

    @Override
    public EidMessageFactory getMessageFactory() {
        return eidMessageFactory;
    }

    @Override
    public LazyFactory getLazyFactory() {
        return lazyFactory;
    }

    @Override
    public EidFactory getEidFactory() {
        return eidFactory;
    }
}
