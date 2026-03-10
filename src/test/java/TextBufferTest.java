import com.buffer.textBuffer.TextBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextBufferTest {


    // --- Buffer init ---

    @Test
    void shouldInitializeScreenWithCorrectHeight() {
        TextBuffer buffer = new TextBuffer(10, 5, 100);

        for (int i = 0; i < 5; i++) {
            assertNotNull(buffer.getScreenLine(i));
        }

        assertThrows(IllegalArgumentException.class, () -> buffer.getScreenLine(5));
    }

    @Test
    void shouldInitializeLinesWithCorrectWidth() {
        TextBuffer buffer = new TextBuffer(8, 3, 100);

        for (int i = 0; i < 3; i++) {
            String line = buffer.getScreenLine(i);
            assertEquals(8, line.length());
        }
    }

    @Test
    void shouldInitializeCellsWithSpaces() {
        TextBuffer buffer = new TextBuffer(6, 2, 100);

        for (int i = 0; i < 2; i++) {
            String line = buffer.getScreenLine(i);
            assertEquals("      ", line);
        }
    }

    @Test
    void shouldHandleWidthOne() {
        TextBuffer buffer = new TextBuffer(1, 3, 10);

        for (int i = 0; i < 3; i++) {
            assertEquals(" ", buffer.getScreenLine(i));
        }
    }

    @Test
    void shouldHandleHeightOne() {
        TextBuffer buffer = new TextBuffer(5, 1, 10);

        assertEquals(5, buffer.getScreenLine(0).length());
    }

    @Test
    void shouldThrowIfHeightIsZero() {
        assertThrows(IllegalArgumentException.class, () -> new TextBuffer(5, 0, 10));
    }

    @Test
    void shouldThrowIfWidthIsZero() {
        assertThrows(IllegalArgumentException.class, () -> new TextBuffer(0, 3, 10));
    }

    @Test
    void shouldThrowIfHeightIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new TextBuffer(5, -1, 10));
    }

    @Test
    void shouldThrowIfWidthIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new TextBuffer(-1, 3, 10));
    }

    @Test
    void shouldThrowIfMaxScrollbackIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new TextBuffer(0, 3, -1));
    }

}