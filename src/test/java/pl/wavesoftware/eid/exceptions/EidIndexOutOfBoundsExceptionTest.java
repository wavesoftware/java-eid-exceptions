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
public class EidIndexOutOfBoundsExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String constUniq = "deadfa11";
    private String causeString = "Index seams to be invalid";
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private Throwable cause = new ArrayIndexOutOfBoundsException(causeString);
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
        EidIndexOutOfBoundsException ex = new EidIndexOutOfBoundsException(new Eid("20151119:103152"));

        // when
        Class<? extends RuntimeException> cls = ex.getStandardJdkClass();

        // then
        assertThat(cls).isEqualTo(IndexOutOfBoundsException.class);
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_String_Throwable() {
        // given
        String eid = "20151119:103158";
        String ref = "MS+1233";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103158|MS+1233]<deadfa11> => Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, ref, cause);
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_String() {
        // given
        String eid = "20151119:103217";
        String ref = "MS+1233";

        // then
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103217|MS+1233]<deadfa11>");

        // when
        throw new EidIndexOutOfBoundsException(eid, ref);
    }

    @Test
    public void testEidIndexOutOfBoundsException_String_Throwable() {
        // given
        String eid = "20151119:103232";

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103232]<deadfa11> => Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, cause);
    }

    @Test
    public void testEidIndexOutOfBoundsException_Eid_Throwable() {
        // given
        String eidNum = "20151119:103245";
        Eid eid = new Eid(eidNum);

        // then
        thrown.expectCause(hasMessage(containsString(causeString)));
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(ArrayIndexOutOfBoundsException.class));
        thrown.expect(EidIndexOutOfBoundsException.class);
        thrown.expectMessage("[20151119:103245]<deadfa11> => Index seams to be invalid");

        // when
        throw new EidIndexOutOfBoundsException(eid, cause);
    }
}