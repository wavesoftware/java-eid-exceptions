package pl.wavesoftware.eid.exceptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Krzysztof Suszyński <krzyszto.suszynski@wavesoftware.pl>
 * @since 21.07.15
 */
public class AssertionsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testJdkAssertions() {
        // then
        thrown.expect(AssertionError.class);

        // when
        assert getTestNumber() < 3 : new Eid("20150721:101958");
    }

    private int getTestNumber() {
        return 3 * 5;
    }
}
