package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext2f;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-04
 */
public class HealthBar extends EngineObject {

    private final Ship ship;

    public HealthBar(Ship ship) {
        this.ship = ship;
    }

    @Override
    public void onUpdate(float seconds) {
        setHidden(ship.isDead());
        if (ship.isDeleted()) {
            delete();
        }
    }


    @Override
    public void onDraw(DrawContext2f context) {
        final float left = ship.getPositionX() - 2f,
                top = ship.getPositionY() - 5f,
                right = ship.getPositionX() + 2f,
                bottom = ship.getPositionY() - 4.5f;
        context.color(Color.GREEN).fillRect(left, top, left + (right - left) * ship.getHealth(), bottom);
        context.color(Color.WHITE).drawRect(left, top, right, bottom);
    }
}
