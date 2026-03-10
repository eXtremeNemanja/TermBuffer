import com.buffer.textBuffer.CellAttributes;
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
            assertNotNull(buffer.getLine(i));
        }

        assertThrows(IndexOutOfBoundsException.class, () -> buffer.getLine(5));
    }

    @Test
    void shouldInitializeLinesWithCorrectWidth() {
        TextBuffer buffer = new TextBuffer(8, 3, 100);

        for (int i = 0; i < 3; i++) {
            String line = buffer.getLine(i);
            assertEquals(8, line.length());
        }
    }

    @Test
    void shouldInitializeCellsWithSpaces() {
        TextBuffer buffer = new TextBuffer(6, 2, 100);

        for (int i = 0; i < 2; i++) {
            assertTrue(buffer.getLine(i).isBlank());
        }
    }

    @Test
    void shouldHandleWidthOne() {
        TextBuffer buffer = new TextBuffer(1, 3, 10);

        for (int i = 0; i < 3; i++) {
            assertEquals(1, buffer.getLine(i).length());
        }
    }

    @Test
    void shouldHandleHeightOne() {
        TextBuffer buffer = new TextBuffer(5, 1, 10);

        assertEquals(5, buffer.getLine(0).length());
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

    // --- Write text ---

    @Test
    void writeTextShouldFitInOneLine() {

        TextBuffer buffer = new TextBuffer(5,5,100);

        buffer.writeText("ABC");

        assertTrue(buffer.getLine(0).startsWith("ABC"));
    }

    @Test
    void writeTextShouldMoveCursorCorrectly() {
        TextBuffer buffer = new TextBuffer(10, 3, 10);

        buffer.writeText("ABC");

        assertEquals(0, buffer.getCursorPosition().getRow());
        assertEquals(3, buffer.getCursorPosition().getColumn());
    }

    @Test
    void writeTextShouldOverride() {
        TextBuffer buffer = new TextBuffer(3, 2, 5);

        buffer.writeText("ABC");

        buffer.setCursorPosition(new Position(0,0));
        buffer.writeText("DEF");

        assertEquals("DEF", buffer.getLine(0));
    }

    @Test
    void writeTextExactlyFitsLine() {
        TextBuffer buffer = new TextBuffer(3, 2, 5);

        buffer.writeText("ABC");

        Position cursorPosition = buffer.getCursorPosition();

        assertEquals("ABC", buffer.getLine(0));
        assertEquals(0, cursorPosition.getRow());
        assertEquals(3, cursorPosition.getColumn());
    }

    @Test
    void writeTextShouldWrapWhenExceedingScreenWidth() {

        TextBuffer buffer = new TextBuffer(3,5,100);

        buffer.writeText("ABCD");

        assertEquals("ABC", buffer.getLine(0));
        assertTrue(buffer.getLine(1).startsWith("D"));
    }

    @Test
    void writeTextMultipleLinesWithWrap() {
        TextBuffer buffer = new TextBuffer(4, 3, 5);

        buffer.writeText("AAAABBBBCCC");

        assertEquals("AAAA", buffer.getLine(0));
        assertEquals("BBBB", buffer.getLine(1));
        assertTrue(buffer.getLine(2).startsWith("CCC"));
    }

    @Test
    void writeTextShouldScrollWhenExceedingScreenHeight() {
        TextBuffer buffer = new TextBuffer(3, 3, 100);

        buffer.writeText("AAABBBCCCD");

        assertEquals("AAA", buffer.getLine(0));
        assertEquals("BBB", buffer.getLine(1));
        assertEquals("CCC", buffer.getLine(2));
        assertTrue(buffer.getLine(3).startsWith("D"));
    }

    @Test
    void writeTextEmptyStringShouldNotChangeScreen() {
        TextBuffer buffer = new TextBuffer(5, 2, 5);

        buffer.writeText("");
        assertEquals(0, buffer.getCursorPosition().getRow());
        assertEquals(0, buffer.getCursorPosition().getColumn());
        assertTrue(buffer.getLine(0).isBlank());
    }


    @Test
    void writeTextCursorAtScreenEndDoesNotOverflow() {
        TextBuffer buffer = new TextBuffer(4, 2, 2);

        buffer.setCursorPosition(new Position(1, 3));
        buffer.writeText("AB");

        Position cursorPosition = buffer.getCursorPosition();
        assertTrue(cursorPosition.getRow() <= 1);
        assertTrue(cursorPosition.getColumn() <= 3);
    }

    // --- Insert text ---

    @Test
    void insertTextAtStartOfEmptyLine() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.insertText("AB");

        assertTrue(buffer.getLine(0).startsWith("AB"));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(2, cursorPosition.getColumn());
        assertEquals(0, cursorPosition.getRow());
    }

    @Test
    void insertTextInMiddleOfLine() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");

        buffer.setCursorPosition(new Position(0, 2));
        buffer.insertText("XY");

        assertEquals("ABXYC", buffer.getLine(0));
        assertTrue(buffer.getLine(1).startsWith("DE"));

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(2, cursorPosition.getColumn());
        assertEquals(1, cursorPosition.getRow());
    }

    @Test
    void insertTextAtLineEnd() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABC");
        buffer.setCursorPosition(new Position(0, 3));
        buffer.insertText("DE");

        assertEquals("ABCDE", buffer.getLine(0));

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(5, cursorPosition.getColumn());
        assertEquals(0, cursorPosition.getRow());
    }

    @Test
    void insertTextThatExceedsLineWidthShouldWrap() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.setCursorPosition(new Position(0, 1));
        buffer.insertText("XYZ");

        assertEquals("AXYZB", buffer.getLine(0));
        assertTrue(buffer.getLine(1).startsWith("CDE"));

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(3, cursorPosition.getColumn());
        assertEquals(1, cursorPosition.getRow());
    }

    @Test
    void insertTextAtStartOfFullLineShouldWrapExistingText() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.setCursorPosition(new Position(0, 0));
        buffer.insertText("XY");

        assertEquals("XYABC", buffer.getLine(0));
        assertTrue(buffer.getLine(1).startsWith("DE"));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(2, cursorPosition.getColumn());
    }

    @Test
    void insertTextAtEndOfFullLineShouldWrapExistingText() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.setCursorPosition(new Position(0, 5));
        buffer.insertText("XY");

        assertEquals("ABCDX", buffer.getLine(0));
        assertTrue(buffer.getLine(1).startsWith("YE"));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(2, cursorPosition.getColumn());
        assertEquals(1, cursorPosition.getRow());
    }

    @Test
    void insertTextMultipleWraps() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        buffer.writeText("ABCDE");
        buffer.setCursorPosition(new Position(0, 5));
        buffer.insertText("UVWXYZ");

        assertEquals("ABCDU", buffer.getLine(0));
        assertEquals("VWXYZ", buffer.getLine(1));
        assertTrue(buffer.getLine(2).startsWith("E"));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(1, cursorPosition.getColumn());
        assertEquals(2, cursorPosition.getRow());
    }

    @Test
    void insertTextMultipleTimes() {
        TextBuffer buffer = new TextBuffer(6, 2, 10);

        buffer.writeText("ABC");
        buffer.setCursorPosition(new Position(0, 1));
        buffer.insertText("XY");
        buffer.setCursorPosition(new Position(0, 2));
        buffer.insertText("Z");

        assertEquals("AXZYBC", buffer.getLine(0));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(3, cursorPosition.getColumn());
    }

    @Test
    void insertTextEmptyStringDoesNothing() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABC");
        buffer.setCursorPosition(new Position(0, 1));
        buffer.insertText("");

        assertTrue(buffer.getLine(0).startsWith("ABC"));
        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(1, cursorPosition.getColumn());
    }

    // --- Fill line ---

    @Test
    void fillLineShouldFillEntireLineWithCharacter() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        buffer.fillLine('X');

        assertEquals("XXXXX", buffer.getLine(0));
    }

    @Test
    void fillLineShouldNotMoveCursor() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.setCursorPosition(new Position(1, 3));

        buffer.fillLine('X');

        Position cursor = buffer.getCursorPosition();
        assertEquals(1, cursor.getRow());
        assertEquals(3, cursor.getColumn());
    }

    @Test
    void fillLineShouldOverrideExistingContent() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAAAA");

        buffer.fillLine('B');

        assertEquals("BBBBB", buffer.getLine(0));
    }

    @Test
    void fillLineDoesNotAffectOtherLines() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.fillLine('A');

        buffer.setCursorPosition(new Position(1,0));
        buffer.fillLine('B');

        assertEquals("AAAAA", buffer.getLine(0));
        assertEquals("BBBBB", buffer.getLine(1));
    }

    @Test
    void fillLineFillWithSpaceCharacterShouldEmptyLine() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAAAA");

        buffer.fillLine(' ');

        assertTrue(buffer.getLine(0).isBlank());
    }

    // --- Empty Line ---

    @Test
    void emptyLineShouldOverride() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAAAA");

        buffer.emptyLine();

        assertTrue(buffer.getLine(0).isBlank());
    }

    // --- Insert empty line

    @Test
    void insertEmptyLineShouldMoveTopLineToScrollback() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAAAA");
        buffer.writeText("BBBBB");
        buffer.writeText("CCCCC");

        buffer.insertEmptyLine();

        assertEquals("BBBBB", buffer.getLine(1));
        assertEquals("CCCCC", buffer.getLine(2));
        assertTrue(buffer.getLine(3).isBlank());

        assertEquals("AAAAA", buffer.getLine(0));
    }

    @Test
    void insertEmptyLineShouldScrollEvenIfTopLineIsEmpty() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        buffer.insertEmptyLine();

        assertTrue(buffer.getLine(2).isBlank());
        assertTrue(buffer.getLine(0).isBlank());
    }

    @Test
    void insertEmptyLineShouldRespectScrollbackLimit() {
        TextBuffer buffer = new TextBuffer(5, 2, 1);

        buffer.writeText("AAAAA");
        buffer.writeText("BBBBB");

        buffer.insertEmptyLine();
        buffer.insertEmptyLine();

        assertEquals("BBBBB", buffer.getLine(0));
    }

    @Test
    void insertEmptyLineShouldNotChangeCursorPosition() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.setCursorPosition(new Position(1, 2));

        buffer.insertEmptyLine();

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(1, cursorPosition.getRow());
        assertEquals(2, cursorPosition.getColumn());
    }

    // --- Clear screen ---

    @Test
    void clearScreen_removesAllContentFromScreen() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAA");

        buffer.clearScreen();

        assertEquals("     ", buffer.getLine(0));
        assertEquals("     ", buffer.getLine(1));
        assertEquals("     ", buffer.getLine(2));
    }

    @Test
    void clearScreenShouldNotClearScrollback() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.writeText("FGHIJ");
        buffer.writeText("KLMNO");

        buffer.clearScreen();

        assertEquals("ABCDE", buffer.getLine(0));
    }

    @Test
    void clearScreenResetsCursorPosition() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.setCursorPosition(new Position(2, 3));

        buffer.clearScreen();

        Position cursor = buffer.getCursorPosition();
        assertEquals(0, cursor.getRow());
        assertEquals(0, cursor.getColumn());
    }

    // --- Clear screen and scrollback ---

    @Test
    void clearScreenAndScrollbackShouldRemoveScreenContent() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.writeText("AAAAA");

        buffer.clearScreenAndScrollback();

        assertTrue(buffer.getLine(0).isBlank());
        assertTrue(buffer.getLine(1).isBlank());
        assertTrue(buffer.getLine(2).isBlank());
    }

    @Test
    void clearScreenAndScrollbackShouldRemoveScrollbackHistory() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.writeText("FGHIJ");
        buffer.writeText("KLMNO");

        assertEquals("ABCDE", buffer.getLine(0));

        buffer.clearScreenAndScrollback();

        assertTrue(buffer.getLine(0).isBlank());
    }

    @Test
    void clearScreenAndScrollbackShouldResetCursorPosition() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);
        buffer.setCursorPosition(new Position(2, 4));

        buffer.clearScreenAndScrollback();

        Position cursorPosition = buffer.getCursorPosition();
        assertEquals(0, cursorPosition.getRow());
        assertEquals(0, cursorPosition.getColumn());
    }

    // --- Retrieve character at position ---

    @Test
    void getCharacterShouldReadFromScreen() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        buffer.writeText("ABCDE");

        assertEquals('A', buffer.getCharacterAt(new Position(0, 0)));
        assertEquals('E', buffer.getCharacterAt(new Position(0, 4)));
    }

    @Test
    void getCharacterShouldReadFromScrollback() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.writeText("FGHIJ");
        buffer.writeText("KLMNO");

        assertEquals('A', buffer.getCharacterAt(new Position(0, 0))); // scrollback line
    }

    @Test
    void getCharacterShouldReturnSpaceForEmptyCell() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        assertEquals(' ', buffer.getCharacterAt(new Position(0, 0)));
    }

    @Test
    void getCharacter_invalidPositionThrows() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> buffer.getCharacterAt(new Position(-1, 0)));
    }

    // --- Retrieve cell attributes at position ---

    @Test
    void getAttributes_returnsDefaultAttributes() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        CellAttributes attr = buffer.getAttributesAt(new Position(0, 0));

        assertNotNull(attr);
    }

    @Test
    void getAttributes_readsFromScrollback() {
        TextBuffer buffer = new TextBuffer(5, 2, 10);

        buffer.writeText("ABCDE");
        buffer.writeText("FGHIJ");
        buffer.writeText("KLMNO");

        CellAttributes attr = buffer.getAttributesAt(new Position(0, 0));

        assertNotNull(attr);
    }

    @Test
    void getAttributes_invalidPositionThrows() {
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> buffer.getAttributesAt(new Position(10, 1)));
    }
}