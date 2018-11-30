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
package pl.wavesoftware.eid.exceptions;

import pl.wavesoftware.eid.Eid;
import pl.wavesoftware.eid.EidContainer;
import pl.wavesoftware.eid.EidMessage;

import javax.annotation.Nullable;

/**
 * This exception class is baseline of all Eid runtime exception classes. It is
 * designed to ease of use and provide strict ID for given Exception usage.
 * This approach speed up development of large application and helps support
 * teams to by giving the both static and random ID for each possible
 * unpredicted bug.
 * <p>
 * This is best to use with tools and plugins like
 * <a href="https://goo.gl/VTHTGq">
 * Generating Exception ID number in Intellij IDEA with Live Templates</a> or
 * <a href="http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator">
 * EidGenerator for Netbeans IDE</a>
 * <p>
 * <strong>Caution!</strong> This class shouldn't be used in any public API or
 * library. It is designed to be used for in-house development of end user
 * applications which will report bugs in standardized error pages or post them
 * to issue tracker.
 * <p>
 * For convenience use {@link pl.wavesoftware.eid.utils.EidPreconditions}
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @see pl.wavesoftware.eid.utils.EidPreconditions
 * @see Eid
 * @since 0.1.0
 */
public class EidRuntimeException extends RuntimeException implements EidContainer {

    private static final long serialVersionUID = 20181029202308L;
    private final Eid eid;

    /**
     * Constructs a new runtime exception with the specified Exception ID as
     * it's detail message, that's {@code new Eid(eid).toString()}.
     * <p>
     * The cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param eid an exception ID as character sequence
     */
    public EidRuntimeException(CharSequence eid) {
        this(new Eid(eid));
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID and
     * detail message.
     * <p>
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param eid     an exception ID as character sequence
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public EidRuntimeException(CharSequence eid, String message) {
        this(new Eid(eid), message);
    }

    /**
     * Constructs a new runtime exception with the specified Eid message.
     * <p>
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the Eid message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public EidRuntimeException(EidMessage message) {
        super(message.toString());
        this.eid = message.getEid();
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID,
     * detail message and cause.
     *
     * <p>
     * Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this runtime exception's
     * detail message.
     *
     * @param eid     an exception ID as character sequence
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public EidRuntimeException(CharSequence eid, String message, @Nullable Throwable cause) {
        this(new Eid(eid), message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID,
     * detail message and cause.
     *
     * <p>
     * Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this runtime exception's
     * detail message.
     *
     * @param message the Eid message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public EidRuntimeException(EidMessage message, @Nullable Throwable cause) {
        this(message.getEid(), message.toString(), cause);
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID and
     * cause.
     * <p>
     * A detail message of <code>eid.toString() + (cause==null ? "" : cause.toString())</code>
     * (which typically contains the class and detail message of
     * <code>cause</code>).
     * <p>
     * This constructor is useful for runtime exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param eid   an exception ID as character sequence
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public EidRuntimeException(CharSequence eid, @Nullable Throwable cause) {
        this(new Eid(eid), cause);
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID as
     * it's detail message, that's {@code eid.toString()}.
     * <p>
     * The cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param id an exception ID
     */
    public EidRuntimeException(Eid id) {
        super(id.toString());
        eid = id;
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID and
     * detail message.
     * <p>
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param id      an exception ID
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public EidRuntimeException(Eid id, String message) {
        super(id.message(message).toString());
        this.eid = id;
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID,
     * detail message and cause.
     *
     * <p>
     * Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this runtime exception's
     * detail message.
     *
     * @param id     an exception ID
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public EidRuntimeException(Eid id, String message, @Nullable Throwable cause) {
        super(id.message(message).toString(), cause);
        this.eid = id;
    }

    /**
     * Constructs a new runtime exception with the specified Exception ID and
     * cause.
     * <p>
     * A detail message of <code>eid.toString() + (cause==null ? "" : cause.toString())</code>
     * (which typically contains the class and detail message of
     * <code>cause</code>).
     * <p>
     * This constructor is useful for runtime exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param id   an exception ID
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public EidRuntimeException(Eid id, @Nullable Throwable cause) {
        super(messageOf(id, cause), cause);
        eid = id;
    }

    @Override
    public Eid getEid() {
        return eid;
    }

    private static String messageOf(Eid eid, @Nullable Throwable cause) {
        if (cause != null) {
            return eid.message(messageOf(cause)).toString();
        }
        return eid.toString();
    }

    private static String messageOf(Throwable cause) {
        return coalesce(
            coalesce(cause.getLocalizedMessage(), cause.getMessage()),
            cause.toString()
        );
    }

    @Nullable
    private static <T> T coalesce(@Nullable T first, @Nullable T second) {
        if (first == null) {
            return second;
        } else {
            return first;
        }
    }
}
