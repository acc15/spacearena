package ru.spacearena.engine.graphics.font.gen.pack;

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


    public static void splitRect(Rect2I f, Rect2I r, List<Rect2I> l) {
        if (r.getRight() <= f.getLeft()
         || r.getBottom() <= f.getTop()
         || r.getLeft() >= f.getRight()
         || r.getTop() >= f.getBottom()) {
            // nothing to split.. rectangles don't intersect, add "as is"
            l.add(f);
            return;
        }
        // left
        if (r.getLeft() > f.getLeft()) {
            l.add(new Rect2IP(f.getLeft(), f.getTop(), r.getLeft(), f.getBottom()));
        }
        // top
        if (r.getTop() > f.getTop()) {
            l.add(new Rect2IP(f.getLeft(), f.getTop(), f.getRight(), r.getTop()));
        }
        // right
        if (r.getRight() < f.getRight()) {
            l.add(new Rect2IP(r.getRight(), f.getTop(), f.getRight(), f.getBottom()));
        }
        // bottom
        if (r.getBottom() < f.getBottom()) {
            l.add(new Rect2IP(f.getLeft(), r.getBottom(), f.getRight(), f.getBottom()));
        }
    }

    private static Rect2I findFreeArea(List<Rect2I> areas, int w, int h) {

        Rect2I bestArea = null;
        int minDim = Integer.MAX_VALUE;
        for (final Rect2I f : areas) {

            final int fw = f.getWidth(), fh = f.getHeight();
            if (w > fw || h > fh) {
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

    public boolean pack(Rect2I r) {
        final Rect2I f = findFreeArea(freeAreas, r.getWidth(), r.getHeight());
        if (f == null) {
            return false;
        }

        r.moveTo(f.getLeft(), f.getTop());
        packWidth = IntMathUtils.max(packWidth, r.getRight());
        packHeight = IntMathUtils.max(packHeight, r.getBottom());

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
