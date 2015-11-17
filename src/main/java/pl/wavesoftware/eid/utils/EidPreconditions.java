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


import pl.wavesoftware.eid.exceptions.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalArgumentException;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.eid.exceptions.EidIndexOutOfBoundsException;
import pl.wavesoftware.eid.exceptions.EidNullPointerException;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <strong>This class shouldn't be used in any public API or library.</strong> It is designed to be used for in-house development
 * of end user applications which will report Bugs in standardized error pages or post them to issue tracker.
 * <p/>
 * Static convenience methods that help a method or constructor check whether it was invoked correctly (whether its
 * <i>preconditions</i>
 * have been met). These methods generally accept a {@code boolean} expression which is expected to be {@code true} (or in the
 * case of {@code
 * checkNotNull}, an object reference which is expected to be non-null). When {@code false} (or {@code null}) is passed instead,
 * the {@code EidPreconditions} method throws an unchecked exception, which helps the calling method communicate to <i>its</i>
 * caller that
 * <i>that</i> caller has made a mistake.
 * <p/>
 * Each method accepts a EID String or {@link Eid} object, which is designed to ease of use and provide strict ID for given
 * Exception usage. This approach speed up development of large application and helps support teams to by giving the both static
 * and random ID for each possible unpredicted bug.
 * <p/>
 * This is best to use with tools and plugins like
 * <a href="http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator">EidGenerator for Netbeans IDE</a>
 * <p/>
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
 * <p/>
 * In this example, {@code checkArgument} throws an {@code EidIllegalArgumentException} to indicate that {@code exampleBadCaller}
 * made an error in <i>its</i> call to {@code sqrt}. Exception, when it will be printed will contain user given Eid and also
 * Randomly generated ID. Those fields can be displayed to user on error page on posted directly to issue tracker.
 * <p/>
 * Example:
 * <p/>
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
 * <p/>
 * <p/>
 * <h3>Functional try to execute blocks</h3>
 * <p/>
 * <p/>
 * Using functional blocks to handle operations, that are intended to operate properly, simplify the code and makes it more
 * readable. It's also good way to deal with untested, uncovered {@code catch} blocks. It's easy and gives developers nice way of
 * dealing with countless operations that suppose to work as intended.
 * <p/>
 * <p/>
 * Example:
 * <pre><code>
 *
 *     InputStream is = EidPreconditions.tryToExecute({@code new UnsafeSupplier<InputStream>}() {
 *        {@literal @}Override
 *         public InputStream get() throws IOException {
 *             return this.getClass().getClassLoader()
 *                 .getResourceAsStream("project.properties");
 *         }
 *     }, "20150718:121521");
 * </code></pre>
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 0.1.0 (idea imported from Guava Library and COI code)
 */
public final class EidPreconditions {

    protected EidPreconditions() {
        throw new EidRuntimeException("20150718:083450", "This should not be accessed");
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression ({@link Nullable}) a boolean expression
     * @param eid        ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     *                   {@link pl.wavesoftware.eid.exceptions.Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     * @throws EidNullPointerException     if {@code expression} is null
     */
    public static void checkArgument(final @Nullable Boolean expression, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        checkArgument(expression, new Eid(checkedEid));
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression    ({@link Nullable}) a boolean expression
     * @param eid           ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     *                      {@link pl.wavesoftware.eid.exceptions.Eid}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @throws EidIllegalArgumentException if {@code expression} is false
     * @throws EidNullPointerException     if {@code expression} or {@code eid} are null
     */
    public static void checkArgument(final @Nullable Boolean expression, final @Nonnull String eid,
                                     final @Nonnull String messageFormat, final Object... parameters) {
        String checkedEid = checkNotNull(eid);
        checkArgument(expression, new Eid(checkedEid), messageFormat, parameters);
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression ({@link Nullable}) a boolean expression
     * @param eid        ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     *                   {@link pl.wavesoftware.eid.exceptions.Eid}
     * @throws EidIllegalArgumentException if {@code expression} is false
     * @throws EidNullPointerException     if {@code expression} is null
     */
    public static void checkArgument(final @Nullable Boolean expression, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (!checkNotNull(expression, checkedEid)) {
            throw new EidIllegalArgumentException(checkedEid);
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression    ({@link Nullable}) a boolean expression
     * @param eid           ({@link Nonnull}) the exception ID to use if the check fails; will be converted to
     *                      {@link pl.wavesoftware.eid.exceptions.Eid}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @throws EidIllegalArgumentException if {@code expression} is false
     * @throws EidNullPointerException     if {@code expression} is null
     */
    public static void checkArgument(final @Nullable Boolean expression, final @Nonnull Eid eid,
                                     final @Nonnull String messageFormat, final Object... parameters) {
        Eid checkedEid = checkNotNull(eid);
        if (!checkNotNull(expression, checkedEid)) {
            throw new EidIllegalArgumentException(checkedEid, messageFormat, parameters);
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression ({@link Nullable}) a boolean expression
     * @param eid        ({@link Nonnull}) the exception message to use if the check fails; will be converted to a string using
     *                   {@link String#valueOf(Object)}
     * @throws EidIllegalStateException if {@code expression} is false
     * @throws EidNullPointerException  if {@code expression} is null
     */
    public static void checkState(final @Nullable Boolean expression, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        checkState(expression, new Eid(checkedEid));
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression    ({@link Nullable}) a boolean expression
     * @param eid           ({@link Nonnull}) the exception message to use if the check fails; will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @throws EidIllegalStateException if {@code expression} is false
     * @throws EidNullPointerException  if {@code expression} is null
     */
    public static void checkState(final @Nullable Boolean expression, final @Nonnull String eid,
                                  final @Nonnull String messageFormat, final Object... parameters) {
        String checkedEid = checkNotNull(eid);
        checkState(expression, new Eid(checkedEid), messageFormat, parameters);
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression ({@link Nullable}) a boolean expression
     * @param eid        ({@link Nonnull}) the exception message to use if the check fails; will be converted to a string using
     *                   {@link String#valueOf(Object)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(final @Nullable Boolean expression, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (!checkNotNull(expression, checkedEid)) {
            throw new EidIllegalStateException(checkedEid);
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the
     * calling method.
     *
     * @param expression    ({@link Nullable}) a boolean expression
     * @param eid           ({@link Nonnull}) the exception message to use if the check fails; will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @throws EidIllegalStateException if {@code expression} is false
     */
    public static void checkState(final @Nullable Boolean expression, final @Nonnull Eid eid,
                                  final @Nonnull String messageFormat, final Object... parameters) {
        Eid checkedEid = checkNotNull(eid);
        if (!checkNotNull(expression, checkedEid)) {
            throw new EidIllegalStateException(checkedEid, messageFormat, parameters);
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>       type of object reference being checked
     * @param reference an object reference
     * @param eid       the exception message to use if the check fails; will be converted to a string using
     *                  {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(final @Nullable T reference, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        return checkNotNull(reference, new Eid(checkedEid));
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>           type of object reference being checked
     * @param reference     an object reference
     * @param eid           the exception message to use if the check fails; will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(final @Nullable T reference, final @Nonnull String eid,
                                     final @Nonnull String messageFormat, final Object... parameters) {
        String checkedEid = checkNotNull(eid);
        return checkNotNull(reference, new Eid(checkedEid), messageFormat, parameters);
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>       type of object reference being checked
     * @param reference an object reference
     * @param eid       the exception message to use if the check fails; will be converted to a string using
     *                  {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(final @Nullable T reference, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (reference == null) {
            throw new EidNullPointerException(checkedEid);
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>           type of object reference being checked
     * @param reference     an object reference
     * @param eid           the exception message to use if the check fails; will be converted to a string using
     *                      {@link String#valueOf(Object)}
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @return the non-null reference that was validated
     * @throws EidNullPointerException if {@code reference} is null
     */
    @Nonnull
    public static <T> T checkNotNull(final @Nullable T reference, final @Nonnull Eid eid,
                                     final @Nonnull String messageFormat, final Object... parameters) {
        Eid checkedEid = checkNotNull(eid);
        if (reference == null) {
            throw new EidNullPointerException(checkedEid, messageFormat, parameters);
        }
        return reference;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @param eid   the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        return checkElementIndex(index, size, new Eid(checkedEid));
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index         a user-supplied index identifying an element of an array, list or string
     * @param size          the size of that array, list or string
     * @param eid           the text to use to describe this index in an error message
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final @Nonnull String eid,
                                        final @Nonnull String messageFormat, final Object... parameters) {
        String checkedEid = checkNotNull(eid);
        return checkElementIndex(index, size, new Eid(checkedEid), messageFormat, parameters);
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @param eid   the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(checkedEid);
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(checkedEid);
        }
        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size {@code size}. An element
     * index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index         a user-supplied index identifying an element of an array, list or string
     * @param size          the size of that array, list or string
     * @param eid           the text to use to describe this index in an error message
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters    parameters fo message format in for of {@link String#format(String, Object...)}
     * @return the value of {@code index}
     * @throws EidIndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws EidIllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, final @Nonnull Eid eid, final @Nonnull String messageFormat,
                                        final Object... parameters) {
        Eid checkedEid = checkNotNull(eid);
        if (isSizeIllegal(size)) {
            throw new EidIllegalArgumentException(checkedEid, messageFormat, parameters);
        }
        if (isIndexAndSizeIllegal(index, size)) {
            throw new EidIndexOutOfBoundsException(checkedEid, messageFormat, parameters);
        }
        return index;
    }

    private static boolean isIndexAndSizeIllegal(int index, int size) {
        return index < 0 || index > size;
    }

    private static boolean isSizeIllegal(int size) {
        return size < 0;
    }

    /**
     * @deprecated Use instead {@link EidPreconditions#tryToExecute(UnsafeSupplier, String)}. To be removed in next release.
     */
    @Nullable
    @Deprecated
    public static <R> R tryToExecute(final @Nonnull RiskyCode<R> code, final @Nonnull String eid) {
        return tryToExecute(new UnsafeSupplier<R>() {
            @Override
            public R get() throws Exception {
                return code.execute();
            }
        }, eid);
    }

    /**
     * @deprecated Use instead {@link EidPreconditions#tryToExecute(UnsafeSupplier, Eid)}. To be removed in next release.
     */
    @Deprecated
    @Nullable
    public static <R> R tryToExecute(final @Nonnull RiskyCode<R> code, final @Nonnull Eid eid) {
        return tryToExecute(new UnsafeSupplier<R>() {
            @Override
            public R get() throws Exception {
                return code.execute();
            }
        }, eid);
    }

    /**
     * For more info in JavaDoc see {@link EidPreconditions#tryToExecute(UnsafeSupplier, Eid)}
     *
     * @param <R>      return type
     * @param supplier unsafe supplier code to be executed within a try-catch block
     * @param eid      unique developer identifier from date for ex.: "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     * @throws EidRuntimeException if code block thrown any exception, which in that case is wrapped in EidRuntimeException
     * @see EidPreconditions#tryToExecute(UnsafeSupplier, Eid)
     */
    @Nullable
    public static <R> R tryToExecute(final @Nonnull UnsafeSupplier<R> supplier, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        return tryToExecute(supplier, new Eid(checkedEid));
    }

    /**
     * For more info in JavaDoc see {@link EidPreconditions#tryToExecute(UnsafeProcedure, Eid)}
     *
     * @param procedure unsafe procedure code to be executed within a try-catch block
     * @param eid       unique developer identifier from date for ex.: "20150716:123200"
     * @throws EidRuntimeException if code block thrown any exception, which in that case is wrapped in EidRuntimeException
     * @see EidPreconditions#tryToExecute(UnsafeProcedure, Eid)
     */
    public static void tryToExecute(final @Nonnull UnsafeProcedure procedure, final @Nonnull String eid) {
        String checkedEid = checkNotNull(eid);
        tryToExecute(procedure, new Eid(checkedEid));
    }

    /**
     * Tries to execute code in given unsafe supplier code block, and if exception is thrown, it will gets rethrown as a
     * {@link EidRuntimeException} with eid given as a argument. This is because this exception is threaded as a software bug!
     * <p/>
     * Example:
     * <pre><code>
     *
     * Document doc = EidPreconditions.tryToExecute({@code new UnsafeSupplier<Document>}() {
     *    {@literal @}Override
     *     public Document get() throws SAXException, IOException {
     *          DocumentBuilder docBuilder = ...
     *          return docBuilder.parse(new InputSource(reader));
     *     }
     * }, new Eid("20150718:121521"));
     * </code></pre>
     *
     * @param <R>      return type
     * @param supplier unsafe supplier code to be executed within a try-catch block
     * @param eid      unique developer identifier from date for ex.: "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     * @throws EidRuntimeException if code block thrown any exception, which in that case is wrapped in EidRuntimeException
     */
    @Nullable
    public static <R> R tryToExecute(final @Nonnull UnsafeSupplier<R> supplier, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        try {
            return supplier.get();
        } catch (Exception throwable) {
            throw new EidRuntimeException(checkedEid, throwable);
        }
    }

    /**
     * Tries to execute code in given unsafe procedure code block, and if exception is thrown, it will gets rethrown as a
     * {@link EidRuntimeException} with eid given as a argument. This is because this exception is threaded as a software bug!
     * <p/>
     * Example:
     * <pre><code>
     *
     * EidPreconditions.tryToExecute({@code new UnsafeProcedure}() {
     *    {@literal @}Override
     *     public void execute() throws EJBException {
     *          em.persist(user);
     *     }
     * }, new Eid("20151117:184627"));
     * </code></pre>
     *
     * @param procedure unsafe procedure code to be executed within a try-catch block
     * @param eid       unique developer identifier from date for ex.: "20150716:123200"
     * @throws EidRuntimeException if code block thrown any exception, which in that case is wrapped in EidRuntimeException
     */
    public static void tryToExecute(final @Nonnull UnsafeProcedure procedure, final @Nonnull Eid eid) {
        Eid checkedEid = checkNotNull(eid);
        try {
            procedure.execute();
        } catch (Exception throwable) {
            throw new EidRuntimeException(checkedEid, throwable);
        }
    }

    /**
     * Code that is risky to execute and can throw some checked Exceptions. To be used with
     *
     * @param <R> a return type from risky code block
     * @deprecated Use instead {@link UnsafeSupplier} or {@link UnsafeProcedure}. To be removed in next release.
     */
    @Deprecated
    public interface RiskyCode<R> {

        /**
         * Executes a client code
         *
         * @return a object of client code
         * @throws Exception this exception should be set to concrete one
         */
        R execute() throws Exception;
    }

    /**
     * This unsafe supplier can be used to execute a code block that needs to return some value and can throw some checked
     * Exceptions, that you would like not to process, because they are unrecoverable bugs. To be used with
     * {@link EidPreconditions#tryToExecute(UnsafeSupplier, Eid)} or
     * {@link EidPreconditions#tryToExecute(UnsafeSupplier, String)} methods
     *
     * @param <T> a return type from unsafe supplier
     */
    public interface UnsafeSupplier<T> {
        /**
         * Executes a supplier function that can throw a checked exception to be cough
         *
         * @return a return value from unsafe supplier function
         * @throws Exception this exception should be set to concrete one ex. IOException
         */
        T get() throws Exception;
    }

    /**
     * This unsafe procedure can be used to execute a code block that can throw some checked Exceptions, that you would
     * like not to process, because they are unrecoverable bugs. To be used with
     * {@link EidPreconditions#tryToExecute(UnsafeProcedure, Eid)} or
     * {@link EidPreconditions#tryToExecute(UnsafeProcedure, String)} methods.
     */
    public interface UnsafeProcedure {
        /**
         * Executes a procedure code that can throw a checked exception to be cough
         *
         * @throws Exception this exception should be set to concrete one ex. IOException
         */
        void execute() throws Exception;
    }

    private static boolean isNull(@Nullable Object reference) {
        return reference == null;
    }

    private static <T> T checkNotNull(@Nullable T reference) {
        if (isNull(reference)) {
            throw new IllegalArgumentException("Pass not-null Eid to EidPreconditions first!");
        }
        return reference;
    }
}
