import com.buffer.textBuffer.Position;
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

    // --- Cursor movement

    @Test
    void getCursorPositionShouldReturnCurrentPosition() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());

    }

    @Test
    void setCursorPositionShouldUpdatePositionWithinBounds() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(3, 4));

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(3, cursorPosition.getRow());
        assertEquals(4, cursorPosition.getColumn());
    }

    @Test
    void setCursorPositionShouldClampRowAboveMax() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(100, 2));
        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(4, cursorPosition.getRow());
        assertEquals(2, cursorPosition.getColumn());
    }

    @Test
    void setCursorPositionShouldClampRowBelowZero() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(-5, 3));
        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(0, cursorPosition.getRow());
        assertEquals(3, cursorPosition.getColumn());
    }

    @Test
    void setCursorPositionShouldClampColumnAboveMax() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 100));
        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(9, cursorPosition.getColumn());
    }

    @Test
    void setCursorPositionShouldClampColumnBelowZero() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, -3));
        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());
    }

    @Test
    void setCursorPositionAtCorners() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        // Top-left
        buffer.setCursorPosition(new Position(0, 0));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());

        // Top-right
        buffer.setCursorPosition(new Position(0, 9));
        cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getRow());
        assertEquals(9, cursorPosition.getColumn());

        // Bottom-left
        buffer.setCursorPosition(new Position(4, 0));
        cursorPosition = buffer.getCursorPosition();
        assertEquals(4, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());

        // Bottom-right
        buffer.setCursorPosition(new Position(4, 9));
        cursorPosition = buffer.getCursorPosition();
        assertEquals(4, cursorPosition.getRow());
        assertEquals(9, cursorPosition.getColumn());
    }

    @Test
    void moveCursorUpShouldDecreaseRow() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(3, 2));
        buffer.moveCursorUp(2);

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(1, cursorPosition.getRow());
        assertEquals(2, cursorPosition.getColumn());
    }

    @Test
    void moveCursorUpShouldNotGoAboveZero() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(1, 3));
        buffer.moveCursorUp(5);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(0, cursorPosition.getRow());
        assertEquals(3, cursorPosition.getColumn());
    }

    @Test
    void moveCursorDownShouldIncreaseRow() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(1, 4));
        buffer.moveCursorDown(2);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(3, cursorPosition.getRow());
        assertEquals(4, cursorPosition.getColumn());
    }

    @Test
    void moveCursorDownShouldNotExceedHeight() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(3, 4));
        buffer.moveCursorDown(10);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(4, cursorPosition.getRow());
        assertEquals(4, cursorPosition.getColumn());
    }

    @Test
    void moveCursorLeftShouldDecreaseColumn() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 6));
        buffer.moveCursorLeft(3);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(3, cursorPosition.getColumn());
    }

    @Test
    void moveCursorLeftShouldNotGoBelowZero() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 1));
        buffer.moveCursorLeft(5);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());
    }

    @Test
    void moveCursorRightShouldIncreaseColumn() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 3));
        buffer.moveCursorRight(4);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(7, cursorPosition.getColumn());
    }

    @Test
    void moveCursorRightShouldNotExceedWidth() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 8));
        buffer.moveCursorRight(10);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(9, cursorPosition.getColumn());
    }

    @Test
    void movingCursorByZeroShouldNotChangePosition() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 5));

        buffer.moveCursorUp(0);
        buffer.moveCursorDown(0);
        buffer.moveCursorLeft(0);
        buffer.moveCursorRight(0);

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals(2, cursorPosition.getRow());
        assertEquals(5, cursorPosition.getColumn());
    }

    @Test
    void largeMovementShouldClampToBounds() {
        TextBuffer buffer = new TextBuffer(10, 5, 10);

        buffer.setCursorPosition(new Position(2, 5));

        buffer.moveCursorUp(100);
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getRow());

        buffer.moveCursorLeft(100);
        cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getColumn());

        buffer.moveCursorDown(100);
        cursorPosition = buffer.getCursorPosition();
        assertEquals(4, cursorPosition.getRow());

        buffer.moveCursorRight(100);
        cursorPosition = buffer.getCursorPosition();
        assertEquals(9, cursorPosition.getColumn());
    }

}