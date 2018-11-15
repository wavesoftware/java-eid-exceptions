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

package pl.wavesoftware.eid.configuration;

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.EidMessage;
import pl.wavesoftware.eid.impl.BindingImpl;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class TestBinding implements Binding {

    private final Binding impl = new BindingImpl();

    @Override
    public ConfigurationSystem getConfigurationSystem() {
        return impl.getConfigurationSystem();
    }

    @Override
    public EidMessage createEidMessage(
        Eid eid,
        CharSequence messageTemplate,
        Object[] templateArguments
    ) {
        return impl.createEidMessage(
            eid, messageTemplate, templateArguments
        );
    }
}
