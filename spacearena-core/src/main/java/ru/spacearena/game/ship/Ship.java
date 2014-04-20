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
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.shaders.TextureProgram;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureDefinition;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.util.FloatMathUtils;
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

    private final EngineContainer<? super EngineEntity> fxContainer;

    private float damageTime = 0f;
    private float health = 1f;
    private boolean active = false;

    private static final Texture.Definition SHIP_TEXTURE = new TextureDefinition().url(GameFactory.class, "ship.png");

    public Point2F[] getGuns() {
        return LOCAL_GUN_POS;
    }

    public Ship(EngineContainer<? super EngineEntity> low, EngineContainer<? super EngineEntity> high) {
        this.fxContainer = low;
        low.add(new EngineFlame(this));
        high.add(new HealthBar(this));
    }

    @Override
    public ObjectType getType() {
        return ObjectType.SHIP;
    }

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.2f;
        bodyDef.angularDamping = 0.2f;
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
        this.active = !FloatMathUtils.isZero(dx, dy);
        if (!active) {
            return;
        }

        final float vl = Ship.MAX_SPEED/FloatMathUtils.length(dx, dy);
        final float vx = dx * vl, vy = dy * vl;
        accelerateTo(vx, vy, Ship.ACCELERATION * seconds);
        rotateTo(FloatMathUtils.atan2(vy, vx), Ship.ANGULAR_VELOCITY * seconds);
    }

    @Override
    public void onCollision(Box2dBody object, boolean isReference, Contact contact, ContactImpulse impulse) {
        final float imp = impulse.normalImpulses[0];
        final float d = GameBody.getObjectType(object) == ObjectType.BULLET ? 0.05f : imp/20000f;
        if (d < 0.05f) {
            return;
        }

        health -= d;
        if (health > 0) {
            damageTime = DAMAGE_TIME;
            return;
        }

        fxContainer.add(new Explosion(getPositionX(), getPositionY(), FloatMathUtils.atan2(getVelocityY(), getVelocityX())));
        kill();
    }

    @Override
    public void onUpdate(float seconds) {
        if (damageTime > 0) {
            damageTime = FloatMathUtils.max(0f, damageTime - seconds);
        }
        super.onUpdate(seconds);
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
    protected void onDrawTransformed(DrawContext2f context) {
        super.onDrawTransformed(context);

        final VertexBuffer vb = context.getSharedBuffer();
        final Texture texture = context.get(SHIP_TEXTURE);

        final float l = -3.7f, t = -2.3f, r = 5.4f, b = 2.3f;
        vb.reset(TextureProgram.LAYOUT_PT2).
                put(l, t).put(texture.getLeft(), texture.getTop()).
                put(l, b).put(texture.getLeft(), texture.getBottom()).
                put(r, b).put(texture.getRight(), texture.getBottom()).
                put(r, t).put(texture.getRight(), texture.getTop());
        context.use(SHADER).
                attrs(vb).
                uniform(context.getActiveMatrix()).
                uniform(SHIP_TEXTURE, 0).
                //glUniform1i(Color.RED, false).
                uniform(damageTime / DAMAGE_TIME).
                draw(OpenGL.GL_TRIANGLE_FAN);
        //context.drawImage(-3.7f, -2.3f, 5.4f, 2.3f, SHIP_TEXTURE);
    }

    public static final ShaderProgram.Definition SHADER = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            final ShaderProgram p = new ShaderProgram();
            p.shader(TextureProgram.class, "tex.vert");
            p.shader(Ship.class, "ship.frag");
            p.attribute("a_Position");
            p.attribute("a_TexCoord");
            p.uniform("u_MVPMatrix");
            p.uniform("u_Texture");
            //p.glUniform1i("u_ShadeColor");
            p.uniform("u_ShadeAmount");
            return p;
        }
    };

}
