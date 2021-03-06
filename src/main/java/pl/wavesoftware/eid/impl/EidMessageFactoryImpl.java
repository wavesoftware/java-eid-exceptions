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
import pl.wavesoftware.eid.api.Eid;
import pl.wavesoftware.eid.api.EidMessage;
import pl.wavesoftware.eid.api.EidMessageFactory;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-12-17
 */
final class EidMessageFactoryImpl implements EidMessageFactory {
    private final Binding binding;

    EidMessageFactoryImpl(Binding binding) {
        this.binding = binding;
    }

    @Override
    public EidMessage create(
        Eid eid,
        CharSequence messageTemplate,
        Object[] templateArguments
    ) {
        return new DefaultEidMessage(
            eid,
            binding.getConfigurationSystem().getConfiguration(),
            messageTemplate,
            templateArguments
        );
    }
}
