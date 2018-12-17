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

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pl.wavesoftware.eid.DefaultEid;
import pl.wavesoftware.eid.api.Eid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
@SuppressWarnings("unused")
@RunWith(Parameterized.class)
public class ExceptionsTest {

    @Parameters(name = "{index}: {0}({1})")
    public static Iterable<Object[]> data() {
        List<Object[]> argsList = getArguments();
        List<Object[]> parameters = Lists.newArrayList();
        for (Class<? extends EidRuntimeException> exception : getExceptions()) {
            for (Object[] args : argsList) {
                addParameters(exception, parameters, args);
            }
        }
        return parameters;
    }

    private static List<Object[]> getArguments() {
        String message = "A testing message";
        Throwable cause = new InterruptedException(message);
        CharSequence eid = "20150718:112954";
        Eid id = new DefaultEid(eid);
        List<Object[]> arguments = Lists.newArrayList();
        arguments.add(new Object[]{eid, message, cause});
        arguments.add(new Object[]{eid, cause});
        arguments.add(new Object[]{eid, message});
        arguments.add(new Object[]{id, cause});
        arguments.add(new Object[]{id});
        return arguments;
    }

    private static String argumentsTypesToString(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (Class<?> argumentType : getArgumentsTypes(args)) {
            sb.append(argumentType.getSimpleName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private static Class<?>[] getArgumentsTypes(Object[] args) {
        List<Class<?>> classes = Lists.newArrayList();
        for (Object arg : args) {
            classes.add(guessType(arg));
        }
        Class<?>[] empty = new Class<?>[0];
        return classes.toArray(empty);
    }

    private static Class<?> guessType(Object arg) {
        return Throwable.class.isAssignableFrom(arg.getClass())
            ? Throwable.class
            : Eid.class.isAssignableFrom(arg.getClass())
            ? Eid.class
            : arg.getClass();
    }

    @SuppressWarnings("unchecked")
    private static Iterable<? extends Class<? extends EidRuntimeException>> getExceptions() {
        return Arrays.asList(
            EidRuntimeException.class,
            EidIllegalArgumentException.class,
            EidIllegalStateException.class,
            EidIndexOutOfBoundsException.class,
            EidNullPointerException.class
        );
    }

    private static void addParameters(
        Class<? extends EidRuntimeException> eidClass,
        List<Object[]> parameters,
        Object[] args
    ) {
        try {
            Class<?>[] types = getArgumentsTypes(args);
            if (types[0] == String.class) {
                types[0] = CharSequence.class;
            }
            Constructor<? extends EidRuntimeException> constructor =
                eidClass.getDeclaredConstructor(types);
            parameters.add(new Object[]{
                eidClass.getSimpleName(),
                argumentsTypesToString(args),
                eidClass,
                constructor,
                args
            });
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private final Class<? extends EidRuntimeException> eidClass;


    private final Constructor<? extends EidRuntimeException> constructor;

    private final Object[] arguments;

    @SuppressWarnings("unchecked")
    public ExceptionsTest(
        String classSimpleName,
        String argsClasses,
        Class<? extends EidRuntimeException> eidClass,
        Constructor<?> constructor,
        Object... arguments
    ) {

        assertThat(classSimpleName).isNotEmpty();
        assertThat(argsClasses).isNotEmpty();
        this.eidClass = eidClass;
        this.constructor = (Constructor<? extends EidRuntimeException>) constructor;
        this.arguments = arguments;
    }

    @Test
    public void testMessage() {
        // given
        EidRuntimeException exception = construct();
        boolean hasCause = exception.getCause() != null;

        // then
        assertThat(exception).hasMessageContaining("20150718:112954");
        if (hasCause) {
            assertThat(exception).hasMessageContaining("A testing message");
        }
    }

    @Test
    public void testConstruction() {
        // given
        EidRuntimeException exception = construct();

        // then
        assertThat(exception).isNotNull();
        assertThat(exception).isExactlyInstanceOf(eidClass);
    }

    @SuppressWarnings("unchecked")
    private <T extends EidRuntimeException> T construct() {
        try {
            return (T) constructor.newInstance(arguments);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
