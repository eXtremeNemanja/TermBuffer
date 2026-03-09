package com.buffer.textBuffer;

public class Cursor {

    private int row;
    private int column;

    public Cursor() {
        this.row = 0;
        this.column = 0;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
