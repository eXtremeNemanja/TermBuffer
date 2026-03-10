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

    private List<Cell> createEmptyLine() {
        List<Cell> line = new ArrayList<>();
        for (int i = 0; i < width; i++)
            line.add(new Cell(' ', new CellAttributes()));
        return line;
    }
}
