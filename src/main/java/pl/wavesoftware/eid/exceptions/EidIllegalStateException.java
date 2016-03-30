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

/**
 * <strong>This class shouldn't be used in any public API or library.</strong> It is designed to be used for in-house development
 * of end user applications which will report Bugs in standardized error pages or post them to issue tracker.
 * <p>
 * This id Eid version of {@link IllegalStateException}
 *
 * @see IllegalStateException
 * @see EidRuntimeException
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 */
public class EidIllegalStateException extends EidRuntimeException {

    private static final long serialVersionUID = -9876432123423443L;

    /**
     * @see EidRuntimeException#EidRuntimeException(String, String, Throwable)
     * @param eid see description on {@link EidRuntimeException#EidRuntimeException(String, String, Throwable)}
     * @param ref see description on {@link EidRuntimeException#EidRuntimeException(String, String, Throwable)}
     * @param cause see description on {@link EidRuntimeException#EidRuntimeException(String, String, Throwable)}
     */
    public EidIllegalStateException(String eid, String ref, Throwable cause) {
        super(eid, ref, cause);
    }

    /**
     * @see EidRuntimeException#EidRuntimeException(String, Throwable)
     * @param eid see description on {@link EidRuntimeException#EidRuntimeException(String, Throwable)}
     * @param cause see description on {@link EidRuntimeException#EidRuntimeException(String, Throwable)}
     */
    public EidIllegalStateException(String eid, Throwable cause) {
        super(eid, cause);
    }

    /**
     * @see EidRuntimeException#EidRuntimeException(String, String)
     * @param eid see description on {@link EidRuntimeException#EidRuntimeException(String, String)}
     * @param ref see description on {@link EidRuntimeException#EidRuntimeException(String, String)}
     */
    public EidIllegalStateException(String eid, String ref) {
        super(eid, ref);
    }

    /**
     * @see EidRuntimeException#EidRuntimeException(Eid, Throwable)
     * @param id see description on {@link EidRuntimeException#EidRuntimeException(Eid, Throwable)}
     * @param cause see description on {@link EidRuntimeException#EidRuntimeException(Eid, Throwable)}
     */
    public EidIllegalStateException(Eid id, Throwable cause) {
        super(id, cause);
    }

    /**
     * @see EidRuntimeException#EidRuntimeException(Eid)
     * @param id see description on {@link EidRuntimeException#EidRuntimeException(Eid)}
     */
    public EidIllegalStateException(Eid id) {
        super(id);
    }

    /**
     * @see EidRuntimeException#EidRuntimeException(Eid, String, Object...)
     * @param id see description on {@link EidRuntimeException#EidRuntimeException(Eid, String, Object...)}
     * @param messageFormat see description on {@link EidRuntimeException#EidRuntimeException(Eid, String, Object...)}
     * @param parameters see description on {@link EidRuntimeException#EidRuntimeException(Eid, String, Object...)}
     */
    public EidIllegalStateException(Eid id, String messageFormat, Object... parameters) {
        super(id, messageFormat, parameters);
    }

    /**
     * @return {@link IllegalStateException} class
     */
    @Override
    public Class<? extends RuntimeException> getStandardJdkClass() {
        return IllegalStateException.class;
    }

}
