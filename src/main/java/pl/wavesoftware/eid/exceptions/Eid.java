/*
 * Copyright 2015 Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.wavesoftware.eid.exceptions;

import java.io.Serializable;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

/**
 * <strong>This class shouldn't be used in any public API or library.</strong> It is designed to be used for in-house development
 * of end user applications which will report Bugs in standardized error pages or post them to issue tracker.
 * <p>
 * Exception identifier for all Eid Runtime Exceptions.
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public class Eid implements Serializable {

    public static final String DEFAULT_FORMAT = "[%s]<%s>";

    public static final String DEFAULT_REF_FORMAT = "[%s|%s]<%s>";

    public static final String DEFAULT_MESSAGE_FORMAT = "%s => %s";

    public static final UniqIdGenerator DEFAULT_UNIQ_ID_GENERATOR = new StdUniqIdGenerator();

    private static final long serialVersionUID = -9876432123423401L;

    private static final int FORMAT_NUM_SPEC = 2;

    private static final int REF_FORMAT_NUM_SPEC = 3;

    private static final int MESSAGE_FORMAT_NUM_SPEC = 2;

    private static String messageFormat = DEFAULT_MESSAGE_FORMAT;

    private static UniqIdGenerator uniqIdGenerator = DEFAULT_UNIQ_ID_GENERATOR;

    private static String format = DEFAULT_FORMAT;

    private static String refFormat = DEFAULT_REF_FORMAT;

    private final String id;

    private final String ref;

    private final String uniq;

    /**
     * Constructor
     *
     * @param id the exception id, must be unique developer inserted string, from date
     * @param ref an optional reference
     */
    public Eid(String id, @Nullable String ref) {
        uniq = uniqIdGenerator.generateUniqId();
        this.id = id;
        this.ref = ref == null ? "" : ref;
    }

    /**
     * Constructor
     *
     * @param id the exception id, must be unique developer inserted string, from date
     */
    public Eid(String id) {
        this(id, null);
    }

    /**
     * Sets a format that will be used for all Eid exceptions when printing a detail message.
     * <p>
     * Format must be non-null and contain two format specifiers <tt>"%s"</tt>
     *
     * @param format a format that will be used, must be non-null and contain two format specifiers <tt>"%s"</tt>
     * @return previously used format
     * @throws IllegalArgumentException if given format hasn't got two format specifiers <tt>"%s"</tt>, or if given format was
     * null
     */
    public static String setMessageFormat(String format) {
        validateFormat(format, MESSAGE_FORMAT_NUM_SPEC);
        String oldFormat = Eid.messageFormat;
        Eid.messageFormat = format;
        return oldFormat;
    }

    /**
     * Gets actually set message format
     *
     * @return actually set message format
     */
    public static String getMessageFormat() {
        return messageFormat;
    }

    /**
     * Sets the actual unique ID generator that will be used to generate IDs for all Eid objects. It will return previously used
     * generator.
     *
     * @param uniqIdGenerator new instance of unique ID generator
     * @return a previously used unique ID generator
     * @throws IllegalArgumentException if given generator was null
     */
    public static UniqIdGenerator setUniqIdGenerator(UniqIdGenerator uniqIdGenerator) {
        if (uniqIdGenerator == null) {
            throw new IllegalArgumentException("Unique ID generator can't be null, but given one");
        }
        UniqIdGenerator previous = Eid.uniqIdGenerator;
        Eid.uniqIdGenerator = uniqIdGenerator;
        return previous;
    }

    /**
     * Sets the actual format that will be used in {@link #toString()} method. It will return previously used format.
     *
     * @param format a format compliant with {@link String#format(String, Object...)} with 2 object arguments
     * @return a previously used format
     * @throws IllegalArgumentException if given format hasn't got two format specifiers <tt>"%s"</tt>, or if given format was
     * null
     */
    public static String setFormat(String format) {
        validateFormat(format, FORMAT_NUM_SPEC);
        String prevoiusly = Eid.format;
        Eid.format = format;
        return prevoiusly;
    }

    /**
     * Sets the actual format that will be used in {@link #toString()} method
     *
     * @param refFormat a format compliant with {@link String#format(String, Object...)} with 3 object arguments
     * @return a previously used format
     * @throws IllegalArgumentException if given format hasn't got tree format specifiers <tt>"%s"</tt>, or if given format was
     * null
     */
    public static String setRefFormat(String refFormat) {
        validateFormat(refFormat, REF_FORMAT_NUM_SPEC);
        String previously = Eid.refFormat;
        Eid.refFormat = refFormat;
        return previously;
    }

    @Override
    public String toString() {
        if ("".equals(ref)) {
            return String.format(format, id, uniq);
        }
        return String.format(refFormat, id, ref, uniq);
    }

    /**
     * Getter for constant Exception ID
     *
     * @return ID of exception
     */
    public String getId() {
        return id;
    }

    /**
     * Get custom ref passed to Exception ID
     *
     * @return ID of exception
     */
    public String getRef() {
        return ref;
    }

    /**
     * Gets unique generated string for this instance of Eid
     *
     * @return a unique string
     */
    public String getUniq() {
        return uniq;
    }

    static void validateFormat(String format, int numSpecifiers) {
        if (format == null) {
            throw new IllegalArgumentException("Format can't be null, but just received one");
        }
        List<String> specifiers = new ArrayList<>();
        for (int i = 0; i < numSpecifiers; i++) {
            specifiers.add(i + "-test-id");
        }
        String formatted = String.format(format, specifiers.toArray());
        for (String specifier : specifiers) {
            if (!formatted.contains(specifier)) {
                throw new IllegalArgumentException("Given format contains to little format specifiers, "
                    + "expected " + numSpecifiers + " but given \"" + format + "\"");
            }
        }
    }

    /**
     * It is used to generate unique ID for each EID object. It's mustn't be secure because it just indicate EID object while
     * logging.
     */
    public interface UniqIdGenerator {

        /**
         * Generates a unique string ID
         *
         * @return a generated unique ID
         */
        String generateUniqId();
    }

    private static final class StdUniqIdGenerator implements UniqIdGenerator {

        private static final int BASE36 = 36;

        private final Random random;

        private StdUniqIdGenerator() {
            this.random = getUnsecureFastRandom();
        }

        @Override
        public String generateUniqId() {
            long first = abs(random.nextLong() + 1);
            int second = abs(random.nextInt(Integer.MAX_VALUE));
            int calc = (int) (first + second);
            return Integer.toString(calc, BASE36);
        }

        @SuppressWarnings("squid:S2245")
        private Random getUnsecureFastRandom() {
            return new Random(System.currentTimeMillis());
        }

    }

}
