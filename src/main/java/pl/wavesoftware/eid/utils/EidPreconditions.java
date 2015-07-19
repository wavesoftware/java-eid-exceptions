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
package pl.wavesoftware.eid.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pl.wavesoftware.eid.exceptions.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalArgumentException;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.eid.exceptions.EidIndexOutOfBoundsException;
import pl.wavesoftware.eid.exceptions.EidNullPointerException;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

/**
 * <strong>This class shouldn't be used in any public API or library.</strong> It is designed to be used for in-house development
 * of end user applications which will report Bugs in standarized error pages or post them to issue tracker.
 * <p>
 * Static convenience methods that help a method or constructor check whether it was invoked correctly (whether its
 * <i>preconditions</i>
 * have been met). These methods generally accept a {@code boolean} expression which is expected to be {@code true} (or in the
 * case of {@code
 * checkNotNull}, an object reference which is expected to be non-null). When {@code false} (or {@code null}) is passed instead,
 * the {@code EidPreconditions} method throws an unchecked exception, which helps the calling method communicate to <i>its</i>
 * caller that
 * <i>that</i> caller has made a mistake.
 * <p>
 * Each method accepts a EID String or {@link Eid} object, which is designed to ease of use and provide strict ID for given
 * Exception usage. This approach speed up development of large application and helps support teams to by giving the both static
 * and random ID for each possible unpredicted bug.
 * <p>
 * This is best to use with tools and plugins like
 * <a href="http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator">EidGenerator for Netbeans IDE</a>
 * <p>
 * Example:
 * <pre>   {@code
 *
 *   /**
 *    * Returns the positive square root of the given value.
 *    *
 *    * @throws EidIllegalArgumentException if the value is negative
 *    *}{@code /
 *   public static double sqrt(double value) {
 *     EidPreconditions.checkArgument(value >= 0.0, "20150718:012333");
 *     // calculate the square root
 *   }
 *
 *   void exampleBadCaller() {
 *     double d = sqrt(-1.0);
 *   }
 * }</pre>
 *
 * In this example, {@code checkArgument} throws an {@code EidIllegalArgumentException} to indicate that {@code exampleBadCaller}
 * made an error in <i>its</i> call to {@code sqrt}. Exception, when it will be printed will contain user given Eid and also
 * Randomly generated ID. Those fields can be displayed to user on error page on posted directly to issue tracker.
 * <p>
 * Example:
 *
 * <pre>
 *
 * {@code
 *   // Main application class for ex.: http servlet
 *    try {
 *        performRequest(request, response);
 *    } catch (EidRuntimeException ex) {
 *        issuesTracker.put(ex);
 *        throw ex;
 *    }
 * }</pre>
 *
 *
 * <h3>Functional try to execute blocks</h3>
 *
 * <p>
 * Using functional blocks to handle operations, that are intended to operate properly, simplify the code and makes it more
 * readable. It's also good way to deal with untested, uncovered {@code catch} blocks. It's easy and gives developers nice way of
 * dealing with countless operations that supose to work as intended.
 *
 * <p>
 * Example:
 * <pre><code>
 *
 *     InputStream is = EidPreconditions.tryToExecute({@code new RiskyCode<InputStream>}() {
 *        {@literal @}Override
 *         public InputStream execute() throws IOException {
 *             return this.getClass().getClassLoader()
 *                 .getResourceAsStream("project.properties");
 *         }
 *     }, "20150718:121521");
 * </code></pre>
 *
 * @since 0.1.0 (idea imported from Guava Library and COI code)
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public final class EidPreconditions {

    protected EidPreconditions() {
        throw new EidRuntimeException("20150718:083450", "This should not be accessed");
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression ({@link Nonnull}) a boolean expression
     * @param eid ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     * {@link pl.wavesoftware.eid.exceptions.Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(@Nonnull boolean expression, @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        if (!expression) {
            throw new EidIllegalArgumentException(new Eid(checkedEid));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression ({@link Nonnull}) a boolean expression
     * @param eid ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     * {@link pl.wavesoftware.eid.exceptions.Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(@Nonnull boolean expression, @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (!expression) {
            throw new EidIllegalArgumentException(checkedEid);
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression a boolean expression
     * @param eid the exception message to use if the check fails; will be converted to a string using
     * {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(@Nonnull boolean expression, @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        if (!expression) {
            throw new EidIllegalStateException(new Eid(checkedEid));
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression a boolean expression
     * @param eid the exception message to use if the check fails; will be converted to a string using
     * {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(@Nonnull boolean expression, @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (!expression) {
            throw new EidIllegalStateException(checkedEid);
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>
     * @param reference an object reference
     * @param eid the exception message to use if the check fails; will be converted to a string using
     * {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(@Nullable T reference, @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        if (reference == null) {
            throw new EidNullPointerException(new Eid(checkedEid));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>
     * @param reference an object reference
     * @param eid the exception message to use if the check fails; will be converted to a string using
     * {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(@Nullable T reference, @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (reference == null) {
            throw new EidNullPointerException(checkedEid);
        }
        return reference;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size the size of that array, list or string
     * @param eid the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    @Nonnull
    public static int checkElementIndex(int index, int size, @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw new EidIndexOutOfBoundsException(new Eid(checkedEid));
        }
        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size the size of that array, list or string
     * @param eid the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    @Nonnull
    public static int checkElementIndex(int index, int size, @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw new EidIndexOutOfBoundsException(checkedEid);
        }
        return index;
    }

    /**
     * Tries to execute code in given block, and if exception is thrown, it will gets retrown as a {@link EidRuntimeException}
     * with eid given as a argument
     * <p>
     * Example:
     * <pre><code>
     *
     * Document doc = EidPreconditions.tryToExecute({@code new RiskyCode<Document>}() {
     *    {@literal @}Override
     *     public Document execute() throws SAXException, IOException {
     *          DocumentBuilder docBuilder = ...
     *          return docBuilder.parse(new InputSource(reader));
     *     }
     * }, "20150718:121521");
     * </code></pre>
     *
     * @param <R> return type
     * @param code code to be executed within a try-catch block
     * @param eid uniq developer identifier from date for ex.: "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     */
    @Nullable
    @SuppressWarnings({
        "checkstyle:com.puppycrawl.tools.checkstyle.checks.coding.IllegalCatchCheck",
        "pmd:AvoidCatchingGenericException"
    })
    public static <R> R tryToExecute(@Nonnull RiskyCode<R> code, @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        try {
            return code.execute();
        } catch (Exception throwable) {
            throw new EidRuntimeException(new Eid(checkedEid), throwable);
        }
    }

    /**
     * Tries to execute code in given block, and if exception is thrown, it will gets retrown as a {@link EidRuntimeException}
     * with eid given as a argument
     * <p>
     * Example:
     * <pre><code>
     *
     * Document doc = EidPreconditions.tryToExecute({@code new RiskyCode<Document>}() {
     *    {@literal @}Override
     *     public Document execute() throws SAXException, IOException {
     *          DocumentBuilder docBuilder = ...
     *          return docBuilder.parse(new InputSource(reader));
     *     }
     * }, "20150718:121521");
     * </code></pre>
     *
     * @param <R> return type
     * @param code code to be executed within a try-catch block
     * @param eid uniq developer identifier from date for ex.: "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     */
    @Nullable
    @SuppressWarnings({
        "checkstyle:com.puppycrawl.tools.checkstyle.checks.coding.IllegalCatchCheck",
        "pmd:AvoidCatchingGenericException"
    })
    public static <R> R tryToExecute(@Nonnull RiskyCode<R> code, @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        try {
            return code.execute();
        } catch (Exception throwable) {
            throw new EidRuntimeException(checkedEid, throwable);
        }
    }

    /**
     * Code that is risky to execute and can throw some checked Exceptions. To be used with
     *
     * @param <R> a return type from risky code block
     */
    public interface RiskyCode<R> {

        /**
         * Executes a client code
         *
         * @return a object of client code
         * @throws Exception this exception sould be set to concrete one
         */
        @SuppressWarnings({
            "pmd:SignatureDeclareThrowsException",
            "squid:S00112"
        })
        R execute() throws Exception;
    }

    private static <T> T checkNotNull(@Nullable T reference) {
        if (reference == null) {
            throw new IllegalArgumentException(new NullPointerException("Pass not-null Eid to EidPreconditions first!"));
        }
        return reference;
    }
}
