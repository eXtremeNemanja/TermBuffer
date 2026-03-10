package com.buffer.textBuffer;

import java.util.ArrayList;
import java.util.List;

public class TextBuffer {

    private int width;
    private int height;
    private int maxScrollback;

    private final List<List<Cell>> screen;
    private final List<List<Cell>> scrollback;

    private final Cursor cursor;
    private CellAttributes currentAttributes;

    public TextBuffer(int width, int height, int maxScrollback) {

        if (width <= 0)
            throw new IllegalArgumentException("Width must be greater than 0");

        if (height <= 0)
            throw new IllegalArgumentException("Height must be greater than 0");

        if (maxScrollback < 0)
            throw new IllegalArgumentException("Scrollback cannot be negative");

        this.width = width;
        this.height = height;
        this.maxScrollback = maxScrollback;

        this.cursor = new Cursor();
        this.currentAttributes = new CellAttributes();

        this.screen = new ArrayList<>();
        this.scrollback = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            this.screen.add(createEmptyLine());
        }
    }

    public void writeText(String text) {
        for (char c : text.toCharArray()) {

            if (cursor.getColumn() > width - 1) {
                cursor.setPosition(cursor.getRow(), 0);
                moveCursorDown();
            }

            List<Cell> line = screen.get(cursor.getRow());
            line.set(cursor.getColumn(), new Cell(c, currentAttributes));

            cursor.setPosition(cursor.getRow(), cursor.getColumn() + 1);
        }
    }

    public String getScreenLine(int lineNumber) {
        if (lineNumber >= height)
            throw new IllegalArgumentException("Line number must be less than screen height");

        return getText(screen.get(lineNumber));
    }

    public String getScrollbackLine(int lineNumber) {
        if (lineNumber < 0) {
            lineNumber += scrollback.size();
        }

        if (lineNumber >= maxScrollback)
            throw new IllegalArgumentException("Line number must be less than maximum scrollback size");

        if (lineNumber >= scrollback.size())
            throw new IllegalArgumentException("Scrollback is currently not that big");

        return getText(scrollback.get(lineNumber));
    }

    public Position getCursorPosition() {
        return new Position(cursor.getRow(), cursor.getColumn());
    }

    public void setCursorPosition(Position cursorPosition) {
        int row = Math.min(height - 1, Math.max(0, cursorPosition.getRow()));
        int column = Math.min(width - 1, Math.max(0, cursorPosition.getColumn()));

        cursor.setPosition(row, column);
    }

    public void moveCursorUp(int n) {
        cursor.setPosition(Math.max(0, cursor.getRow() - n), cursor.getColumn());
    }

    public void moveCursorDown(int n) {
        cursor.setPosition(Math.min(height - 1, cursor.getRow() + n), cursor.getColumn());
    }

    public void moveCursorLeft(int n) {
        cursor.setPosition(cursor.getRow(), Math.max(0, cursor.getColumn() - n));
    }

    public void moveCursorRight(int n) {
        cursor.setPosition(cursor.getRow(), Math.min(width - 1, cursor.getColumn() + n));
    }

    private List<Cell> createEmptyLine() {
        List<Cell> line = new ArrayList<>();
        for (int i = 0; i < width; i++)
            line.add(new Cell(' ', new CellAttributes()));
        return line;
    }

    private String getText(List<Cell> cells) {
        StringBuilder text = new StringBuilder();
        for (Cell c : cells) {
            text.append(c.getCharacter());
        }

        return text.toString();
    }


    private void moveCursorDown() {

        if (cursor.getRow() == height - 1) {
            scroll();
        } else {
            cursor.setPosition(cursor.getRow() + 1, cursor.getColumn());
        }
    }

    private void scroll() {
        List<Cell> removedLine = screen.removeFirst();
        scrollback.addLast(removedLine);

        if (scrollback.size() > maxScrollback) {
            scrollback.removeFirst();
        }

        screen.add(createEmptyLine());
    }
}
