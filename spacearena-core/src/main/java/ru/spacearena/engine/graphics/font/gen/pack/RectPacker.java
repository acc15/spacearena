package ru.spacearena.engine.graphics.font.gen.pack;

import ru.spacearena.engine.geometry.shapes.Rect2IP;
import ru.spacearena.engine.util.IntMathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class RectPacker {

    private final List<Rect2IP> freeAreas = new ArrayList<Rect2IP>();
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

    public static boolean contains(Rect2IP r1, Rect2IP r2) {
        return r1.l <= r2.l && r1.r >= r2.r && r1.t <= r2.t && r1.b >= r2.b;
    }

    public static void splitRect(Rect2IP f, Rect2IP r, List<Rect2IP> l) {
        if (r.r <= f.l || r.b <= f.t || r.l >= f.r || r.t >= f.b) {
            // nothing to split.. rectangles don't intersect, add "as is"
            l.add(f);
            return;
        }
        // left
        if (r.l > f.l) {
            l.add(new Rect2IP(f.l, f.t, r.l, f.b));
        }
        // top
        if (r.t > f.t) {
            l.add(new Rect2IP(f.l, f.t, f.r, r.t));
        }
        // right
        if (r.r < f.r) {
            l.add(new Rect2IP(r.r, f.t, f.r, f.b));
        }
        // bottom
        if (r.b < f.b) {
            l.add(new Rect2IP(f.l, r.b, f.r, f.b));
        }
    }

    private static Rect2IP findFreeArea(List<Rect2IP> areas, int w, int h) {

        Rect2IP bestArea = null;
        int minDim = Integer.MAX_VALUE;
        for (final Rect2IP f : areas) {

            final int fw = f.getWidth(), fh = f.getHeight();
            if (w > fw || h > fh) {
                continue;
            }

            final int d = IntMathUtils.max(f.l, f.t);
            if (d < minDim) {
                bestArea = f;
                minDim = d;
            }
        }
        return bestArea;
    }

    public boolean pack(Iterable<? extends Rect2IP> rects) {
        for (Rect2IP r: rects) {
            if (!pack(r)) {
                return false;
            }
        }
        return true;
    }

    public boolean pack(Rect2IP r) {
        final Rect2IP f = findFreeArea(freeAreas, r.getWidth(), r.getHeight());
        if (f == null) {
            return false;
        }

        r.moveTo(f.l, f.t);
        packWidth = IntMathUtils.max(packWidth, r.r);
        packHeight = IntMathUtils.max(packHeight, r.b);

        final int l = freeAreas.size();
        for (int i=0; i<l; i++) {
            final Rect2IP s = freeAreas.remove(0);
            splitRect(s, r, freeAreas);
        }

        for (int i=0; i<freeAreas.size(); i++) {
            for (int j=i+1; j<freeAreas.size(); j++) {
                final Rect2IP r1 = freeAreas.get(i), r2 = freeAreas.get(j);
                if (contains(r2, r1)) {
                    freeAreas.remove(i);
                    --i;
                    break;
                }
                if (contains(r1, r2)) {
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

    public List<Rect2IP> getFreeAreas() {
        return freeAreas;
    }
}
