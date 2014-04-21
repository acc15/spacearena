package ru.spacearena.engine.graphics.font.gen.pack;

import ru.spacearena.engine.geometry.shapes.PackedRect2I;
import ru.spacearena.engine.geometry.shapes.Rect2I;
import ru.spacearena.engine.geometry.shapes.Rect2IP;
import ru.spacearena.engine.util.IntMathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class RectPacker {

    private final List<Rect2I> freeAreas = new ArrayList<Rect2I>();
    private int packWidth = 0, packHeight = 0;

    public static Rect2IP createInfiniteArea() {
        return new Rect2IP(Integer.MAX_VALUE,Integer.MAX_VALUE);
    }

    public RectPacker() {
        freeAreas.add(createInfiniteArea());
    }

    public RectPacker(Rect2IP freeArea) {
        freeAreas.add(freeArea);
    }

    public void reset(Rect2IP freeArea) {
        packWidth = 0;
        packHeight = 0;
        freeAreas.clear();
        freeAreas.add(freeArea);
    }

    public void reset() {
        reset(createInfiniteArea());
    }


    public static void splitRect(Rect2I free, PackedRect2I rect, List<Rect2I> list) {
        final int l = rect.getX(), t = rect.getY(), r = l + rect.getWidth(), b = t + rect.getHeight();
        if (r <= free.getLeft()
         || b <= free.getTop()
         || l >= free.getRight()
         || t >= free.getBottom()) {
            // nothing to split.. rectangles don't intersect, add "as is"
            list.add(free);
            return;
        }
        // left
        if (l > free.getLeft()) {
            list.add(new Rect2IP(free.getLeft(), free.getTop(), l, free.getBottom()));
        }
        // top
        if (t > free.getTop()) {
            list.add(new Rect2IP(free.getLeft(), free.getTop(), free.getRight(), t));
        }
        // right
        if (r < free.getRight()) {
            list.add(new Rect2IP(r, free.getTop(), free.getRight(), free.getBottom()));
        }
        // bottom
        if (b < free.getBottom()) {
            list.add(new Rect2IP(free.getLeft(), b, free.getRight(), free.getBottom()));
        }
    }

    private static Rect2I findFreeArea(List<Rect2I> areas, PackedRect2I rect) {

        Rect2I bestArea = null;
        int minDim = Integer.MAX_VALUE;
        for (final Rect2I f : areas) {

            final int fw = f.getWidth(), fh = f.getHeight();
            if (rect.getWidth() > fw || rect.getHeight() > fh) {
                continue;
            }

            final int d = IntMathUtils.max(f.getLeft(), f.getTop());
            if (d < minDim) {
                bestArea = f;
                minDim = d;
            }
        }
        return bestArea;
    }

    public boolean pack(Iterable<? extends Rect2I> rects) {
        for (Rect2I r: rects) {
            if (!pack(r)) {
                return false;
            }
        }
        return true;
    }

    public boolean pack(PackedRect2I r) {
        final Rect2I f = findFreeArea(freeAreas, r);
        if (f == null) {
            return false;
        }

        r.setPosition(f.getLeft(), f.getTop());
        packWidth = IntMathUtils.max(packWidth, f.getLeft() + r.getWidth());
        packHeight = IntMathUtils.max(packHeight, f.getTop() + r.getHeight());

        final int l = freeAreas.size();
        for (int i=0; i<l; i++) {
            final Rect2I s = freeAreas.remove(0);
            splitRect(s, r, freeAreas);
        }

        for (int i=0; i<freeAreas.size(); i++) {
            for (int j=i+1; j<freeAreas.size(); j++) {
                final Rect2I r1 = freeAreas.get(i), r2 = freeAreas.get(j);
                if (r2.contains(r1)) {
                    freeAreas.remove(i);
                    --i;
                    break;
                }
                if (r1.contains(r2)) {
                    freeAreas.remove(j);
                    --j;
                }
            }
        }
        return true;
    }

    public int getPackWidth() {
        return packWidth;
    }

    public int getPackHeight() {
        return packHeight;
    }

    public List<Rect2I> getFreeAreas() {
        return freeAreas;
    }
}
