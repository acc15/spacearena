package ru.spacearena.android;

import android.graphics.Typeface;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class MainActivity extends BaseGameActivity {

    public EngineOptions onCreateEngineOptions() {
        final float w = 1280, h = 720;
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(w, h), new BoundCamera(0, 0, w, h));
    }

    private IFont font;
    private ITextureRegion shipImage;

    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        this.font = FontFactory.create(getFontManager(), getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 30, Color.WHITE.getARGBPackedInt());
        this.font.load();

        final BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(),512, 512);
        this.shipImage = BitmapTextureAtlasTextureRegionFactory.createFromResource(
                atlas, this, R.drawable.ship, 0, 0);
        atlas.load();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        final Camera camera = getEngine().getCamera();

        final Scene scene = new Scene();

        final Sprite sprite = new Sprite(0,0, shipImage, getVertexBufferObjectManager());
        final Ship ship = new Ship(sprite);
        camera.setChaseEntity(ship);

        final RectangularShape rect = new Rectangle(100, 100, 30, 30, getVertexBufferObjectManager());
        rect.setColor(Color.WHITE);
        scene.attachChild(rect);
        scene.attachChild(ship);

        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionUp()) {
                    ship.getPhysicsHandler().setVelocity(0, 0);
                } else {
                    final float x = pSceneTouchEvent.getX() - ship.getX();
                    final float y = pSceneTouchEvent.getY() - ship.getY();
                    final float len = MathUtils.length(x, y);
                    ship.setRotation(MathUtils.radToDeg(MathUtils.atan2(-x, y))+180);
                    ship.getPhysicsHandler().setVelocity(x/len*500f, y/len*500f);
                }
                return false;
            }
        });

        final HUD hud = new HUD();
        final Text text = new Text(0, 0, this.font, "FPS: XXXXXXXX", getVertexBufferObjectManager());
        final Text positionText = new Text(0, 50, this.font, "Position: XXXXXXXXXXXXXXXXX", getVertexBufferObjectManager());
        hud.attachChild(text);
        hud.attachChild(positionText);
        camera.setHUD(hud);

        this.mEngine.registerUpdateHandler(new IUpdateHandler() {
            public void onUpdate(float pSecondsElapsed) {
                text.setText(String.format("FPS: %.2f", 1/pSecondsElapsed));
                positionText.setText(String.format("Position: %.2f; %.2f", ship.getX(), ship.getY()));
            }
            public void reset() {
            }
        });


        pOnCreateSceneCallback.onCreateSceneFinished(scene);
    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pScene.setBackground(new Background(0, 0, 0));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
}
