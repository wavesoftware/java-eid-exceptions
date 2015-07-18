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
 * of end user applications which will report Bugs in standarized error pages or post them to issue tracker.
 * <p>
 * Exception identifier for all Eid Runtime Exceptions.
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public class Eid implements Serializable {

    public static final String DEFAULT_FORMAT = "[%s]<%s>";

    public static final String DEFAULT_REF_FORMAT = "[%s|%s]<%s>";

    private static final long serialVersionUID = -9876432123423401L;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static String format = DEFAULT_FORMAT;

    private static String refFormat = DEFAULT_REF_FORMAT;

    private final String id;

    private final String ref;

    private final String uniq;

    /**
     * Sets the actual format that will be used in {@link toString()} method. It will return previously used format.
     *
     * @param format a format compliant with {@link String#format(String, Object...)} with 2 object arguments
     * @return a previously used format
     * @throws NullPointerException if given format was null
     * @throws IllegalArgumentException if given format hasn't got two format specifiers <tt>"%s"</tt>
     */
    public static String setFormat(String format) throws NullPointerException, IllegalArgumentException {
        validateFormat(format, 2);
        String prevoiusly = Eid.format;
        Eid.format = format;
        return prevoiusly;
    }

    /**
     * Sets the actual format that will be used in {@link toString()} method
     *
     * @param refFormat a format compliant with {@link String#format(String, Object...)} with 3 object arguments
     * @return a previously used format
     * @throws NullPointerException if given format was null
     * @throws IllegalArgumentException if given format hasn't got tree format specifiers <tt>"%s"</tt>
     */
    public static String setRefFormat(String refFormat) {
        validateFormat(refFormat, 3);
        String prevoiusly = Eid.refFormat;
        Eid.refFormat = refFormat;
        return prevoiusly;
    }

    /**
     * Constructor
     *
     * @param id the exception id, must be unique developer insereted string, from date
     * @param ref an optional reference
     */
    public Eid(String id, @Nullable String ref) {
        uniq = Integer.toString(abs(Long.valueOf(abs(RANDOM.nextLong()) + abs(RANDOM.nextInt())).intValue()), 36);
        this.id = id;
        this.ref = ref == null ? "" : ref;
    }

    /**
     * Constructor
     *
     * @param id the exception id, must be unique developer insereted string, from date
     */
    public Eid(String id) {
        this(id, null);
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
     * Gets uniq generated string for this instance of Eid
     *
     * @return a uniq string
     */
    public String getUniq() {
        return uniq;
    }

    static void validateFormat(String format, int numSpecifiers) throws NullPointerException, IllegalArgumentException {
        if (format == null) {
            throw new NullPointerException("Format can't be null, but just recieved one");
        }
        List<String> specifiers = new ArrayList<String>();
        for (int i = 0; i < numSpecifiers; i++) {
            specifiers.add(i + Integer.toString(abs(RANDOM.nextInt()), 36));
        }
        String formated = String.format(format, specifiers.toArray());
        for (String specifier : specifiers) {
            if (!formated.contains(specifier)) {
                throw new IllegalArgumentException("Given format contains to little format specifiers, "
                    + "expected " + numSpecifiers + " but given \"" + format + "\"");
            }
        }
    }

}
