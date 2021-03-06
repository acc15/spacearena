package ru.spacearena.game.ship;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.Texture;
import ru.spacearena.engine.graphics.shaders.DefaultShaders;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.integration.box2d.Box2dWorld;
import ru.spacearena.engine.random.QRand;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.game.Bullet;
import ru.spacearena.game.GameBody;
import ru.spacearena.game.GameFactory;
import ru.spacearena.game.ObjectType;

import static ru.spacearena.engine.geometry.primitives.Point2F.p;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends GameBody {

    private static final float MAX_SPEED = 30f;
    private static final float ACCELERATION = 30f;
    private static final float ANGULAR_VELOCITY = FloatMathUtils.TWO_PI * 2;


    private static final Point2F[] LOCAL_GUN_POS = new Point2F[]{p(3f, 1.4f), p(3f, -1.4f)};
    private static final Point2F LOCAL_ENGINE_POS = p(-1.25f, 0f);
    private static final Vec2[] LOCAL_SHAPE = new Vec2[]{new Vec2(-2, -2), new Vec2(-2, 2), new Vec2(4, 0.3f), new Vec2(4, -0.3f)};
    public static final float DAMAGE_TIME = 0.2f;
    public static final float SPAWN_TIME = 5f;

    private final EngineContainer<? super EngineEntity> fxContainer;
    private final Box2dWorld world;

    private boolean canFire = true;
    private float damageTime = 0f;
    private float health = 1f;
    private boolean active = false;
    private float reviveTimeout = SPAWN_TIME;

    private float acceleration = ACCELERATION, maxSpeed = MAX_SPEED, angularVelocity = ANGULAR_VELOCITY;

    public static final Texture.Definition TEXTURE = new Texture.Definition().url(GameFactory.class, "ship.png");

    public Point2F[] getGuns() {
        return LOCAL_GUN_POS;
    }

    public Ship(Box2dWorld world, EngineContainer<? super EngineEntity> low, EngineContainer<? super EngineEntity> high) {
        this.fxContainer = low;
        this.world = world;
        low.add(new EngineFlame(this));
        high.add(new HealthBar(this));
    }

    @Override
    public ObjectType getType() {
        return ObjectType.SHIP;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.2f;
        bodyDef.angularDamping = 0.2f;

        this.health = 1f;
        this.reviveTimeout = SPAWN_TIME;
        this.damageTime = 0f;
        setVisible(true);
    }


    public void onPostCreate(Body body) {
        final PolygonShape shape = new PolygonShape();
        shape.set(LOCAL_SHAPE, LOCAL_SHAPE.length);

        final FixtureDef fd = new FixtureDef();
        fd.restitution = 0.1f;
        fd.density = 10f;
        fd.shape = shape;
        body.createFixture(fd);
    }

    @Override
    public void onDestroyBody(Body body) {
        fxContainer.add(new Explosion(getPositionX(), getPositionY(), getVelocityX(), getVelocityY()));
        setVisible(false);
    }


    public boolean isEngineActive() {
        return active;
    }

    public Point2F getEnginePosition(Point2F pt) {
        return mapPoint(pt.set(LOCAL_ENGINE_POS));
    }

    public float getHealth() {
        return health;
    }


    public void flyTo(float dx, float dy, float seconds) {
        if (isDead() || FloatMathUtils.isZero(dx, dy)) {
            active = false;
            return;
        }

        active = true;

        final float vl = FloatMathUtils.scaledLength(maxSpeed, dx, dy);
        final float vx = dx * vl, vy = dy * vl;
        accelerateTo(vx, vy, acceleration * seconds);
        rotateTo(FloatMathUtils.atan2(vy, vx), angularVelocity * seconds);
    }

    public void fire(boolean pressed) {
        if (isDead() || !pressed) {
            canFire = true;
            return;
        }
        if (!canFire) {
            return;
        }
        for (Point2F gun: getGuns()) {
            final Point2F worldGun = mapPoint(Point2F.PT.set(gun));
            final Bullet bullet = new Bullet(this, worldGun.x, worldGun.y, getAngle());
            world.add(bullet);
        }
        canFire = false;
    }

    @Override
    public void onCollision(Box2dBody object, boolean isReference, Contact contact, ContactImpulse impulse) {
        final float imp = impulse.normalImpulses[0];
        final float d = GameBody.getObjectType(object) == ObjectType.BULLET ? 0.05f : imp/40000f;
        if (d < 0.025f) {
            return;
        }

        health -= d;
        if (health > 0) {
            damageTime = DAMAGE_TIME;
            return;
        }
        kill();
    }

    @Override
    public void onUpdate(float seconds) {
        if (isDead()) {
            reviveTimeout -= seconds;
            if (reviveTimeout < 0) {
                revive();
                setVisible(true);
                setInitialPosition(QRand.RAND.nextFloatBetween(-80, 80), QRand.RAND.nextFloatBetween(-80,80));
                onCreate(world);
            }

        } else if (damageTime > 0) {
            damageTime = FloatMathUtils.max(0f, damageTime - seconds);
        }
        super.onUpdate(seconds);
    }

    @Override
    protected void onDrawTransformed(DrawContext2f context) {
        super.onDrawTransformed(context);

        final VertexBuffer vb = context.getSharedBuffer();
        final Texture texture = context.obtain(TEXTURE);

        final float l = -3.7f, t = -2.3f, r = 5.4f, b = 2.3f;
        vb.reset(DefaultShaders.LAYOUT_P2T2).
                put(l, t).put(texture.getLeft(), texture.getTop()).
                put(l, b).put(texture.getLeft(), texture.getBottom()).
                put(r, b).put(texture.getRight(), texture.getBottom()).
                put(r, t).put(texture.getRight(), texture.getTop());
        context.use(SHADER).
                attrs(vb).
                uniform(context.getActiveMatrix()).
                uniform(TEXTURE).
                //glUniform1i(Color.RED, false).
                uniform(damageTime / DAMAGE_TIME).
                draw(OpenGL.GL_TRIANGLE_FAN);
        //context.drawImage(-3.7f, -2.3f, 5.4f, 2.3f, TEXTURE);
    }

    public static final ShaderProgram.Definition SHADER = new ShaderProgram.Definition().
            shader(DefaultShaders.class, "tex.vert").
            shader(Ship.class, "ship.frag").
            attribute("a_Position").
            attribute("a_TexCoord").
            uniform("u_MVPMatrix").
            uniform("u_Texture").
            uniform("u_ShadeAmount");

}
