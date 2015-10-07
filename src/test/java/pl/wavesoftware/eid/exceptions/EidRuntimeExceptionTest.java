package pl.wavesoftware.eid.exceptions;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.bind.JAXBException;
import java.util.UnknownFormatConversionException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2015-10-07
 */
@SuppressWarnings("ConstantConditions")
public class EidRuntimeExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetMessage() {
        // then
        thrown.expect(EidRuntimeException.class);
        thrown.expectCause(CoreMatchers.<Throwable>instanceOf(JAXBException.class));
        thrown.expectCause(hasMessage(is((String) null)));
        thrown.expectMessage(containsString("20151007:212217"));
        thrown.expectMessage(containsString("javax.xml.bind.JAXBException\n - with linked exception:\n" +
                "[java.util.UnknownFormatConversionException: Conversion = 'Invalid for unit test']"));

        // given
        String message = null;
        Throwable original = new UnknownFormatConversionException("Invalid for unit test");
        JAXBException cause = new JAXBException(message);
        cause.setLinkedException(original);
        throw new EidRuntimeException("20151007:212217", cause);
    }

}