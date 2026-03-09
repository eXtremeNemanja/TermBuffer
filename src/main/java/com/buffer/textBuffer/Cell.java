package com.buffer.textBuffer;

public class Cell {

    private char character;
    private CellAttributes attributes;

    public Cell(char character, CellAttributes attributes) {
        this.character = character;
        this.attributes = attributes;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public CellAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(CellAttributes attributes) {
        this.attributes = attributes;
    }
}
