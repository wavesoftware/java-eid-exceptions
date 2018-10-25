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
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pl.wavesoftware.eid.Eid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
@SuppressWarnings("unused")
@RunWith(Parameterized.class)
public class ExceptionsTest {

    private static Map<Class<? extends EidRuntimeException>, Class<? extends RuntimeException>> getClassesMapping() {
        Map<Class<? extends EidRuntimeException>, Class<? extends RuntimeException>> map = Maps.newHashMap();
        map.put(EidRuntimeException.class, RuntimeException.class);
        map.put(EidIllegalArgumentException.class, IllegalArgumentException.class);
        map.put(EidIllegalStateException.class, IllegalStateException.class);
        map.put(EidIndexOutOfBoundsException.class, IndexOutOfBoundsException.class);
        map.put(EidNullPointerException.class, NullPointerException.class);

        return map;
    }

    private static List<Object[]> getArguments() {
        String message = "A testing message";
        Throwable cause = new InterruptedException(message);
        CharSequence eid = "20150718:112954";
        Eid id = new Eid(eid);
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
            classes.add(
                Throwable.class.isAssignableFrom(arg.getClass())
                    ? Throwable.class
                    : arg.getClass()
            );
        }
        Class<?>[] empty = new Class<?>[0];
        return classes.toArray(empty);
    }

    @Parameters(name = "{index}: {0}({1})")
    public static Iterable<Object[]> data() {
        List<Object[]> argsList = getArguments();
        Map<Class<? extends EidRuntimeException>, Class<? extends RuntimeException>> mapping =
            getClassesMapping();
        List<Object[]> parameters = Lists.newArrayList();
        for (Map.Entry<Class<? extends EidRuntimeException>, Class<? extends RuntimeException>> entrySet : mapping.entrySet()) {
            for (Object[] args : argsList) {
                addParameters(entrySet, parameters, args);
            }
        }
        return parameters;
    }

    private static void addParameters(
        Map.Entry<Class<? extends EidRuntimeException>,
            Class<? extends RuntimeException>> entrySet,
        List<Object[]> parameters, Object[] args
    ) {
        try {
            Class<? extends EidRuntimeException> eidClass = entrySet.getKey();
            Class<? extends RuntimeException> jdkClass = entrySet.getValue();

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
                jdkClass,
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

    private final Class<? extends RuntimeException> jdkClass;

    private final Constructor<? extends EidRuntimeException> constructor;

    private final Object[] arguments;

    @SuppressWarnings("unchecked")
    public ExceptionsTest(String classSimpleName, String argsClasses, Class<? extends EidRuntimeException> eidClass,
        Class<? extends RuntimeException> jdkClass, Constructor<?> constructor, Object... arguments) {

        assertThat(classSimpleName).isNotEmpty();
        assertThat(argsClasses).isNotEmpty();
        this.eidClass = eidClass;
        this.jdkClass = jdkClass;
        this.constructor = (Constructor<? extends EidRuntimeException>) constructor;
        this.arguments = arguments;
    }

    @Test
    public void testGetStandardJdkClass() {
        // given
        EidRuntimeException exception = construct();

        // when
        Eid eid = exception.getEid();
        Class<? extends RuntimeException> jdkCls = exception.getJavaClass();

        // then
        assertThat(exception).isNotNull();
        assertThat(eid).isNotNull();
        assertThat(jdkCls).isNotNull();
        assertThat(jdkCls).isEqualTo(jdkClass);
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
