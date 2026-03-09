package com.buffer.textBuffer;

import java.util.EnumSet;

public class CellAttributes {

    private CellColor foregroundColor;
    private CellColor backgroundColor;

    private EnumSet<CellStyle> styles;

    public CellAttributes(CellColor foregroundColor, CellColor backgroundColor, EnumSet<CellStyle> styles) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.styles = styles;
    }

    public CellAttributes() {
        this.foregroundColor = CellColor.DEFAULT;
        this.backgroundColor = CellColor.DEFAULT;
        this.styles = EnumSet.noneOf(CellStyle.class);
    }

    public CellColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(CellColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public CellColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(CellColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public EnumSet<CellStyle> getStyles() {
        return styles;
    }

    public void setStyles(EnumSet<CellStyle> styles) {
        this.styles = styles;
    }
}
