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

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * <strong>This class shouldn't be used in any public API or library.</strong> It is designed to be used for in-house development
 * of end user applications which will report Bugs in standardized error pages or post them to issue tracker.
 * <p>
 * This exception class is baseline of all Eid runtime exception classes. It is designed to ease of use and provide strict ID for
 * given Exception usage. This approach speed up development of large application and helps support teams to by giving the both
 * static and random ID for each possible unpredicted bug.
 * <p>
 * This is best to use with tools and plugins like
 * <a href="http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator">EidGenerator for Netbeans IDE</a>
 * <P>
 * <strong>Caution!</strong> There is no constructor with just a string, for reason that this forces users to add Eid in all
 * places that earlier uses {@link RuntimeException}.
 * <p>
 * For convenience use {@link pl.wavesoftware.eid.utils.EidPreconditions}
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 */
public class EidRuntimeException extends RuntimeException implements EidContainer {

    private static final long serialVersionUID = -9876432123423587L;

    private final Eid eid;

    /**
     * Constructs a new runtime exception with the specified exception Id and ref code. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param eid exception ID
     * @param ref the ref code for Eid
     */
    public EidRuntimeException(String eid, String ref) {
        this(new Eid(eid, ref));
    }

    /**
     * Constructs a new runtime exception with the specified cause, a exception Id and detail message of
     * <code>eid.toString() + " =&gt; " + (cause==null ? null : cause.toString())</code> (which typically contains
     * the class and detail message of <code>cause</code>). This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwable.
     *
     * @param eid exception ID
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <code>null</code>
     * value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EidRuntimeException(String eid, Throwable cause) {
        this(new Eid(eid), cause);
    }

    /**
     * Constructs a new runtime exception with the specified a exception Id, ref code and cause.
     *
     * @param eid exception ID
     * @param ref the ref code for Eid
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <code>null</code> value is
     * permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EidRuntimeException(String eid, String ref, Throwable cause) {
        this(new Eid(eid, ref), cause);
    }

    /**
     * Constructs a new runtime exception with the specified Eid object
     *
     * @param id exception ID
     */
    public EidRuntimeException(Eid id) {
        super(id.toString());
        eid = id;
    }

    /**
     * Constructs a new runtime exception with the specified Eid object and cause
     * <p>
     * The detail message is computed as <code>String.format(Eid.getMessageFormat(), id.toString(), message(cause))</code>
     *
     * @param id exception ID
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <code>null</code> value is
     * permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public EidRuntimeException(Eid id, Throwable cause) {
        super(String.format(
                Locale.ENGLISH, Eid.getMessageFormat(), id.toString(), message(cause)
        ), cause);
        eid = id;
    }

    /**
     * Constructs a new runtime exception with specified Eid object and custom message
     * <p>
     * The detail message is computed as:
     * <pre>String.format(Eid.getMessageFormat(), id.toString(), String.format(messageFormat, parameters))</pre>
     * @param id exception ID
     * @param messageFormat message format in form of {@link String#format(String, Object...)}
     * @param parameters parameters in form of {@link String#format(String, Object...)}
     */
    public EidRuntimeException(Eid id, String messageFormat, Object... parameters) {
        super(String.format(
                Locale.ENGLISH, Eid.getMessageFormat(), id.toString(),
                String.format(Locale.ENGLISH, messageFormat, parameters)
        ));
        eid = id;
    }

    @Override
    public Eid getEid() {
        return eid;
    }

    /**
     * Returns a standard JDK class that this ones is base on. It doesn't mean this class extends that class.
     *
     * @return a standard JDK class of exception, in this case {@link RuntimeException} class.
     */
    public Class<? extends RuntimeException> getStandardJdkClass() {
        return RuntimeException.class;
    }

    private static String message(Throwable cause) {
        String msg = coalesce(cause.getLocalizedMessage(), cause.getMessage());
        return coalesce(msg, cause.toString());
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
