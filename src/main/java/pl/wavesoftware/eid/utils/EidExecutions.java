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

package pl.wavesoftware.eid.utils;

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.exceptions.EidRuntimeException;

import static pl.wavesoftware.eid.utils.EidUtil.ensureEid;

/**
 * <h3>Functional try to execute blocks</h3>
 * Using functional blocks to handle operations, that are intended to operate
 * properly, simplify the code and makes it more readable. It's also good way to
 * deal with untested, uncovered {@code catch} blocks. It's easy and gives
 * developers nice way of dealing with countless operations that suppose to work
 * as intended.
 * <p>
 * Example:
 * <pre>
 * InputStream is = EidExecutions.tryToExecute(new UnsafeSupplier&lt;InputStream&gt;() {
 *     &#64;Override
 *     public InputStream get() throws IOException {
 *         return this.getClass().getClassLoader()
 *             .getResourceAsStream("project.properties");
 *     }
 * }, "20150718:121521");
 * </pre>
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class EidExecutions {

    private EidExecutions() {
        // nothing here
    }

    /**
     * Tries to execute code in given unsafe supplier code block, and if
     * exception is thrown, it will gets rethrown as a {@link EidRuntimeException}
     * with eid given as a argument. This is because this exception is
     * treated as a software bug!
     * <p>
     * Example:
     * <pre>
     * Document doc = EidExecutions.tryToExecute(new UnsafeSupplier&lt;Document&gt;}() {
     *     &#64;Override
     *     public Document get() throws SAXException, IOException {
     *         DocumentBuilder docBuilder = ...
     *         return docBuilder.parse(new InputSource(reader));
     *     }
     * }, new Eid("20150718:121521"));
     * </pre>
     *
     * @param <R>      return type
     * @param supplier unsafe supplier code to be executed within a try-catch
     *                 block
     * @param eid      unique developer identifier from date for ex.:
     *                 "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     * @throws EidRuntimeException if code block thrown any exception, which
     *                             in that case is wrapped in EidRuntimeException
     */
    public static <R> R tryToExecute(
        final UnsafeSupplier<R> supplier,
        final Eid eid
    ) {
        try {
            return supplier.get();
        } catch (Exception throwable) {
            throw new EidRuntimeException(ensureEid(eid), throwable);
        }
    }

    /**
     * Tries to execute code in given unsafe procedure code block, and if
     * exception is thrown, it will gets rethrown as a {@link EidRuntimeException}
     * with eid given as a argument. This is because this exception is treated
     * as a software bug!
     * <p>
     * Example:
     * <pre>
     * EidExecutions.tryToExecute(new UnsafeProcedure() {
     *     &#64;Override
     *     public void execute() throws SQLException {
     *         stmt.executeQuery(userPersist);
     *     }
     * }, new Eid("20151117:184627"));
     * </pre>
     *
     * @param procedure unsafe procedure code to be executed within a try-catch
     *                  block
     * @param eid       unique developer identifier from date for ex.:
     *                  "20150716:123200"
     * @throws EidRuntimeException if code block thrown any exception, which in
     *                             that case is wrapped in EidRuntimeException
     */
    public static void tryToExecute(
        final UnsafeProcedure procedure,
        final Eid eid
    ) {
        try {
            procedure.execute();
        } catch (Exception throwable) {
            throw new EidRuntimeException(ensureEid(eid), throwable);
        }
    }

    /**
     * For more info in JavaDoc see {@link
     * EidExecutions#tryToExecute(UnsafeSupplier, Eid)}
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #tryToExecute(UnsafeSupplier, Eid)} instead.
     *
     * @param <R>      return type
     * @param supplier unsafe supplier code to be executed within a try-catch
     *                 block
     * @param eid      unique developer identifier from date for ex.:
     *                 "20150716:123200"
     * @return A block of code return type, if exception is not thrown
     * @throws EidRuntimeException if code block thrown any exception, which in
     *                             that case is wrapped in EidRuntimeException
     * @see EidExecutions#tryToExecute(UnsafeSupplier, Eid)
     */
    public static <R> R tryToExecute(
        final UnsafeSupplier<R> supplier,
        final String eid
    ) {
        try {
            return supplier.get();
        } catch (Exception throwable) {
            throw new EidRuntimeException(ensureEid(eid), throwable);
        }
    }

    /**
     * For more info in JavaDoc see {@link
     * EidExecutions#tryToExecute(UnsafeProcedure, Eid)}
     * <p>
     * Please, note that for performance reasons, Eid is not evaluated until
     * it's needed. If you are using {@code Validator},
     * please use {@link #tryToExecute(UnsafeProcedure, Eid)} instead.
     *
     * @param procedure unsafe procedure code to be executed within a try-catch
     *                  block
     * @param eid       unique developer identifier from date for ex.:
     *                  "20150716:123200"
     * @throws EidRuntimeException if code block thrown any exception, which in
     *                             that case is wrapped in EidRuntimeException
     * @see EidExecutions#tryToExecute(UnsafeProcedure, Eid)
     */
    public static void tryToExecute(
        final UnsafeProcedure procedure,
        final String eid
    ) {
        try {
            procedure.execute();
        } catch (Exception throwable) {
            throw new EidRuntimeException(ensureEid(eid), throwable);
        }
    }
}
