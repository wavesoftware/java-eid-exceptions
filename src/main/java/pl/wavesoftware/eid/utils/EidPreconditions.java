/*
 * Copyright (c) 2015 Wave Software
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
package pl.wavesoftware.eid.utils;


import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalArgumentException;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.eid.exceptions.EidIndexOutOfBoundsException;
import pl.wavesoftware.eid.exceptions.EidNullPointerException;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;

import static pl.wavesoftware.eid.utils.EidUtil.ensureEid;

/**
 * This class consists of static convenience methods that help a method or
 * constructor check whether it was invoked correctly (whether its
 * <i>preconditions</i> have been met). These methods generally accept a
 * {@code boolean} expression which is expected to be {@code true} (or in the
 * case of {@link #checkNotNull(Object, String)}, an object reference which is
 * expected to be non-null). When {@code false} (or {@code null}) is passed
 * instead, the {@link EidPreconditions} method throws an unchecked exception,
 * which helps the calling method communicate to <i>its</i> caller <i>that</i>
 * caller has made a mistake.
 * <p>
 * Each method accepts a EID String or {@link Eid} object, which is designed to
 * ease of use and provide strict ID for given Exception usage. This approach
 * speed up development of large application and helps support teams to by giving
 * the both static and random ID for each possible unpredicted bug.
 * <p>
 * This is best to use with tools and plugins like
 * <ul>
 * <li><a href="https://goo.gl/VTHTGq">Generating Exception Id number in
 *     Intellij IDEA with Live Templates</a></li>
 * <li><a href="http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator">
 *     EidGenerator for Netbeans IDE</a></li>
 * </ul>
 * <p>
 * Example:
 * <pre>
 * &#47;**
 *  * Returns the positive square root of the given value.
 *  *
 *  * &#64;throws EidIllegalArgumentException if the value is negative
 *  *&#47;
 * public static double sqrt(double value) {
 *     EidPreconditions.checkArgument(value &gt;= 0.0, "20150718:012333");
 *     // calculate the square root
 * }
 *
 * void exampleBadCaller() {
 *     double d = sqrt(-1.0);
 * }
 * </pre>
 * <p>
 * In this example, {@link #checkArgument(boolean, String)} throws an {@link EidIllegalArgumentException} to indicate
 * that {@code exampleBadCaller} made an error in <i>its</i> call to
 * {@code sqrt}. Exception, when it will be printed will contain user given Eid
 * and also randomly generated ID. Those fields can be displayed to user on
 * error page on posted directly to issue tracker.
 * <p>
 * Example:
 * <pre>
 * // Main application class for ex.: http servlet
 * try {
 *     performRequest(request, response);
 * } catch (EidRuntimeException ex) {
 *     issuesTracker.put(ex);
 *     throw ex;
 * }
 * </pre>
 * <strong>Caution!</strong> This class shouldn't be used in any public API or
 * library. It is designed to be used for in-house development of end user
 * applications which will report bugs in standardized error pages or post them
 * to issue tracker.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 0.1.0 (idea imported from Guava Library and COI code)
 */
public final class EidPreconditions {

    EidPreconditions() {
        throw new EidRuntimeException(
            "20150718:083450", "This should not be accessed"
        );
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to
     * the calling method.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkArgument(boolean, Eid)} instead.
     *
     * @param expression a boolean expression
     * @param eid        the exception ID to use if the check fails; will be
     *                   converted to {@link Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(
        final boolean expression,
        final String eid
    ) {
        if (!expression) {
            throw new EidIllegalArgumentException(ensureEid(eid));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to
     * the calling method.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkArgument(boolean, Eid, String, Object[])} instead.
     *
     * @param expression    a boolean expression
     * @param eid           the exception ID to use if the check fails; will be
     *                      converted to {@link Eid}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(
        final boolean expression,
        final String eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (!expression) {
            throw new EidIllegalArgumentException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to
     * the calling method.
     *
     * @param expression a boolean expression
     * @param eid        the exception ID to use if the check fails; will be
     *                   converted to {@link Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(
        final boolean expression,
        final Eid eid
    ) {
        if (!expression) {
            throw new EidIllegalArgumentException(ensureEid(eid));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to
     * the calling method.
     *
     * @param expression    a boolean expression
     * @param eid           the exception ID to use if the check fails; will be
     *                      converted to {@link Eid}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @throws EidIllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(
        final boolean expression,
        final Eid eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (!expression) {
            throw new EidIllegalArgumentException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkState(boolean, Eid)} instead.
     *
     * @param expression a boolean expression
     * @param eid        the exception message to use if the check fails; will
     *                   be converted to a string using
     *                   {@link String#valueOf(Object)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(final boolean expression, final String eid) {
        if (!expression) {
            throw new EidIllegalStateException(ensureEid(eid));
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkState(boolean, Eid, String, Object[])} instead.
     *
     * @param expression    a boolean expression
     * @param eid           the exception message to use if the check fails;
     *                      will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(
        final boolean expression,
        final String eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (!expression) {
            throw new EidIllegalStateException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param eid        the exception message to use if the check fails; will
     *                   be converted to a string using
     *                   {@link String#valueOf(Object)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(final boolean expression, final Eid eid) {
        if (!expression) {
            throw new EidIllegalStateException(ensureEid(eid));
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     *
     * @param expression    a boolean expression
     * @param eid           the exception message to use if the check fails;
     *                      will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(
        final boolean expression,
        final Eid eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (!expression) {
            throw new EidIllegalStateException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkNotNull(Object, Eid)} instead.
     *
     * @param <T>       type of object reference being checked
     * @param reference an object reference
     * @param eid       the exception message to use if the check fails; will be
     *                  converted to a string using
     *                  {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(
        @Nullable final T reference,
        final String eid
    ) {
        if (reference == null) {
            throw new EidNullPointerException(ensureEid(eid));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkNotNull(Object, Eid, String, Object[])} instead.
     *
     * @param <T>           type of object reference being checked
     * @param reference     an object reference
     * @param eid           the exception message to use if the check fails;
     *                      will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(
        @Nullable final T reference,
        final String eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (reference == null) {
            throw new EidNullPointerException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param <T>       type of object reference being checked
     * @param reference an object reference
     * @param eid       the exception message to use if the check fails; will
     *                  be converted to a string using
     *                  {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(
        @Nullable final T reference,
        final Eid eid
    ) {
        if (reference == null) {
            throw new EidNullPointerException(ensureEid(eid));
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param <T>           type of object reference being checked
     * @param reference     an object reference
     * @param eid           the exception message to use if the check fails;
     *                      will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(
        @Nullable final T reference,
        final Eid eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (reference == null) {
            throw new EidNullPointerException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        return reference;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array,
     * list or string of size {@code size}. An element index may range from
     * zero, inclusive, to {@code size}, exclusive.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkElementIndex(int, int, Eid)} instead.
     *
     * @param index a user-supplied index identifying an element of an array,
     *              list or string
     * @param size  the size of that array, list or string
     * @param eid   the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is
     *                                      not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final String eid) {
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(ensureEid(eid));
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(ensureEid(eid));
        }
        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array,
     * list or string of size {@code size}. An element index may range from
     * zero, inclusive, to {@code size}, exclusive.
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #checkElementIndex(int, int, Eid, String, Object[])}
     * instead.
     *
     * @param index         a user-supplied index identifying an element of an
     *                      array, list or string
     * @param size          the size of that array, list or string
     * @param eid           the text to use to describe this index in an error
     *                      message
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or
     *                                      isn't less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(
        int index,
        int size,
        final String eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array,
     * list or string of size {@code size}. An element index may range from
     * zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array,
     *              list or string
     * @param size  the size of that array, list or string
     * @param eid   the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is
     *                                      not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final Eid eid) {
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(ensureEid(eid));
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(ensureEid(eid));
        }
        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array,
     * list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index         a user-supplied index identifying an element of an
     *                      array, list or string
     * @param size          the size of that array, list or string
     * @param eid           the text to use to describe this index in an error
     *                      message
     * @param messageFormat message format in form accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @param parameters    parameters fo message format in accepted by
     *                      {@link MessageFormat#format(String, Object...)}
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is
     *                                      not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(
        int index,
        int size,
        final Eid eid,
        final String messageFormat,
        final Object... parameters
    ) {
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(
                ensureEid(eid).message(messageFormat, parameters)
            );
        }
        return index;
    }

    private static boolean isIndexAndSizeIllegal(int index, int size) {
        return index < 0 || index >= size;
    }

    private static boolean isSizeIllegal(int size) {
        return size < 0;
    }
}
