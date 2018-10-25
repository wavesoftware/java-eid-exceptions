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

import pl.wavesoftware.eid.configuration.UniqueIdGenerator;

import java.util.Random;

import static java.lang.Math.abs;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 */
final class DefaultUniqueIdGenerator implements UniqueIdGenerator {

    private static final int BASE36 = 36;

    private final Random random;

    DefaultUniqueIdGenerator() {
        this.random = getUnsecuredFastRandom();
    }

    @Override
    public String generateUniqId() {
        long first = abs(random.nextLong() + 1);
        int second = abs(random.nextInt(Integer.MAX_VALUE));
        int calc = (int) (first + second);
        return Integer.toString(abs(calc), BASE36);
    }

    /*
     Using random for speed, security of generating random unique id is
     not important.
     */
    @SuppressWarnings("squid:S2245")
    private static Random getUnsecuredFastRandom() {
        return new Random(System.currentTimeMillis());
    }

}
