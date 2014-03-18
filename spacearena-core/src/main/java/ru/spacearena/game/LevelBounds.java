package ru.spacearena.game;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.dynamics.Body;
import ru.spacearena.engine.geometry.shapes.PolyShape2F;
import ru.spacearena.engine.integration.box2d.Box2dObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class LevelBounds extends Box2dObject {

    private PolyShape2F bounds;

    public LevelBounds(PolyShape2F bounds) {
        this.bounds = bounds;
    }

    @Override
    protected void onPostCreate(Body body) {
        for (int i=0; i<bounds.getPointCount(); i++) {
            final EdgeShape edgeShape = new EdgeShape();
            edgeShape.m_vertex1.set(bounds.getPointX(i), bounds.getPointY(i));
            edgeShape.m_vertex2.set(bounds.getPointX(i+1), bounds.getPointY(i+1));
            body.createFixture(edgeShape, 0f);
        }
    }
}
