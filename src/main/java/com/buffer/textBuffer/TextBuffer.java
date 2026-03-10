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
}
