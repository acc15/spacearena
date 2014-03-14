package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.collisions.CollisionObject;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.geometry.shapes.Shape2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends CollisionObject {

    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 2000f;
    public static final float ANGULAR_VELOCITY = 720f;

    public Ship() {
        add(new Sprite());
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {90,10,90,85};
        getWorldSpace().mapPoints(positions);
        return positions;
    }

    @Override
    public void onInit(Engine engine) {
        getSprite().setImage(engine.loadImage("ship.png"));
        setPivot(getSprite().getWidth()/3, getSprite().getHeight()/2);
        super.onInit(engine);
    }

    private Sprite getSprite() {
        return getChild(0);
    }

    public boolean onCollision(CollisionObject entity, float seconds, boolean reference, Contact contact) {

        return true;
    }

    public boolean canCollide(CollisionObject entity) {
        return true;
    }

    private static final Shape2F[] SHIP_SHAPES = new Shape2F[] {/*
        new Rect2FPP(0,0,164,94)*/
    };

    @Override
    public Shape2F getWorldShape(int n) {
        return SHIP_SHAPES[n];
    }

    @Override
    public int getShapeCount() {
        return SHIP_SHAPES.length;
    }

    /*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
