package ru.spacearena.engine.collisions;

/**
* @author Vyacheslav Mayorov
* @since 2014-03-03
*/
public class Contact {
    float time;
    float overlapX;
    float overlapY;

    public float getTime() {
        return time;
    }

    public float getOverlapX() {
        return overlapX;
    }

    public float getOverlapY() {
        return overlapY;
    }

    void setContact(float time, float overlapX, float overlapY) {
        this.time = time;
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }

}
