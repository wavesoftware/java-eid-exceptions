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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-11-24
 */
final class SerializableLazy<T extends Serializable>
    extends Lazy<T>
    implements SerializableSupplier<T> {

    private static final long serialVersionUID = 20181124011908L;
    private volatile T serializable;

    private SerializableLazy(Supplier<T> supplier) {
        super(supplier);
    }

    static <R extends Serializable> SerializableLazy<R> serializableOf(Supplier<R> supplier) {
        return new SerializableLazy<R>(supplier);
    }

    @Override
    public T get() {
        if (serializable == null) {
            synchronized (this) {
                if (serializable == null) {
                    serializable = super.get();
                }
            }
        }
        return serializable;
    }

    /**
     * Ensures that the value is evaluated before serialization.
     *
     * @param stream An object serialization stream.
     * @throws java.io.IOException If an error occurs writing to the stream.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        // evaluates the values if it isn't evaluated yet!
        get();
        stream.defaultWriteObject();
    }
}
