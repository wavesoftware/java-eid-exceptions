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

import pl.wavesoftware.eid.api.Eid;

/**
 * This unsafe procedure can be used to execute a code block that can throw
 * some checked Exception, that you would like not to process, because they
 * are unrecoverable bugs.
 * <p>
 * To be used with
 * {@link EidExecutions#tryToExecute(UnsafeProcedure, Eid)} or
 * {@link EidExecutions#tryToExecute(UnsafeProcedure, String)} methods.
 */
public interface UnsafeProcedure {
    /**
     * Executes a procedure code that can throw a checked exception to be cough
     *
     * @throws Exception this exception should be set to concrete one ex. IOException
     */
    @SuppressWarnings("squid:S00112")
    void execute() throws Exception;
}
