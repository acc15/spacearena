package ru.spacearena.engine.graphics.font;

import java.io.Serializable;

/**
* @author Vyacheslav Mayorov
* @since 2014-07-04
*/
public final class CharGlyph implements Serializable {
    int x, y, offset, advance, width;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOffset() {
        return offset;
    }

    public int getWidth() {
        return width;
    }

    public int getAdvance() {
        return advance;
    }
}
