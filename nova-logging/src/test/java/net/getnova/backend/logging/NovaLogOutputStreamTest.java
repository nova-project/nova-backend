package net.getnova.backend.logging;

import net.getnova.backend.tests.RefectionUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NovaLogOutputStreamTest {

    @Test
    void write() throws IOException, NoSuchFieldException, IllegalAccessException {
        final NovaLogOutputStream logOutputStream = new NovaLogOutputStream("test", NovaLogLevel.INFO);

        final String message = "This is a unit test!";
        final byte[] content = message.getBytes(StandardCharsets.UTF_8);

        logOutputStream.write(content);
        assertEquals(message, this.getMemory(logOutputStream));

        logOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        assertEquals("", this.getMemory(logOutputStream));
    }

//    @Test
//    void flush() {
//    }

    private String getMemory(final NovaLogOutputStream logOutputStream)
            throws NoSuchFieldException, IllegalAccessException {
        return (String) RefectionUtils.getFieldValue(logOutputStream, "memory");
    }
}
