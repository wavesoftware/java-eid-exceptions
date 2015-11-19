package pl.wavesoftware.eid.exceptions;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2015-11-19
 */
public class EidNullPointerExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String constUniq = "cafedead";
    private String causeString = "A cause";
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private Throwable cause = new UnsupportedOperationException(causeString);
    private Eid.UniqIdGenerator original;

    @Before
    public void before() {
        original = Eid.setUniqIdGenerator(new Eid.UniqIdGenerator() {
            @Override
            public String generateUniqId() {
                return constUniq;
            }
        });
    }

    @After
    public void after() {
        Eid.setUniqIdGenerator(original);
    }

    @Test
    public void testGetStandardJdkClass() throws Exception {
        // given
        @SuppressWarnings("ThrowableInstanceNeverThrown")
        EidNullPointerException ex = new EidNullPointerException(new Eid("20151119:102323"));

        // when
        Class<? extends RuntimeException> cls = ex.getStandardJdkClass();

        // then
        assertThat(cls).isEqualTo(NullPointerException.class);
    }

    @Test
    public void testEidNullPointerException_String_String_Throwable() {
        // given
        String eid = "20151119:100854";
        String ref = "PL-9584";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(UnsupportedOperationException.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:100854|PL-9584]<cafedead> => A cause");

        // when
        throw new EidNullPointerException(eid, ref, cause);
    }

    @Test
    public void testEidNullPointerException_String_String() {
        // given
        String eid = "20151119:100854";
        String ref = "PL-9584";

        // then
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:100854|PL-9584]<cafedead>");

        // when
        throw new EidNullPointerException(eid, ref);
    }

    @Test
    public void testEidNullPointerException_String_Throwable() {
        // given
        String eid = "20151119:101810";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(UnsupportedOperationException.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:101810]<cafedead> => A cause");

        // when
        throw new EidNullPointerException(eid, cause);
    }

    @Test
    public void testEidNullPointerException_Eid_Throwable() {
        // given
        String eidNum = "20151119:102150";
        Eid eid = new Eid(eidNum);

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(UnsupportedOperationException.class));
        thrown.expect(EidNullPointerException.class);
        thrown.expectMessage("[20151119:102150]<cafedead> => A cause");

        // when
        throw new EidNullPointerException(eid, cause);
    }
}