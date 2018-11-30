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

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.EidMessage;
import pl.wavesoftware.eid.configuration.Binding;
import pl.wavesoftware.eid.configuration.ConfigurationSystem;

import java.io.Serializable;

/**
 * A default binding implementation.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class BindingImpl implements Binding {

    private final ConfigurationSystem system = new ConfigurationSystemImpl();

    @Override
    public ConfigurationSystem getConfigurationSystem() {
        return system;
    }

    @Override
    public EidMessage createEidMessage(
        Eid eid,
        CharSequence messageTemplate,
        Object[] templateArguments
    ) {
        return new DefaultEidMessage(
            eid,
            getConfigurationSystem().getConfiguration(),
            messageTemplate,
            templateArguments
        );
    }

    @Override
    public <T extends Serializable> SerializableSupplier<T> lazy(Supplier<T> supplier) {
        return SerializableLazy.serializableOf(supplier);
    }
}
