package com.ceesiz.trail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Main3d extends ApplicationAdapter {

    ImmediateModeRenderer20 renderer;
    Camera camera;
    CameraInputController cameraInputController;
    Trail3d trail;

    @Override
    public void create() {
        setupCamera();
        renderer = new ImmediateModeRenderer20(false, true, 0);
        this.createTrail();
    }

    public void update(float dt){
        float x = Gdx.input.getX() - Gdx.graphics.getWidth() * 0.5f;
        float y = Gdx.graphics.getHeight() -  Gdx.input.getY() - Gdx.graphics.getHeight() * 0.5f;

        trail.setPosition(x, y, 0);
        trail.update(dt);

        camera.update();
    }

    @Override
    public void render() {
        this.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |
                GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        renderer.begin(camera.combined, GL20.GL_TRIANGLES);
        trail.render(renderer);
        renderer.end();

    }

    private void setupCamera() {
        camera = new PerspectiveCamera(
                55f, // görüş açısı
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        camera.position.set(0f, -250f, 50);
        camera.lookAt(0f, 0f, 0); // odak noktası
        camera.up.set(Vector3.Z);
        camera.near = 0.1f; // kameranın gördüğü en yakın mesafe
        camera.far = 10000f; // kameranın gördüğü en uzak görüntülediği mesafe

        cameraInputController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);
    }

    private void createTrail(){
        trail = new Trail3d(
                10, // segment size
                10, // segment length
                5, // max segment length
                15, // segment width
                0.4f, // segment rotation lerp
                0.1f, // trail position lerp
                new Vector3(0, 0, 0) // begin position
        );

        // set start and end colors
        Color colorStart = Color.YELLOW;
        Color colorEnd = new Color(1,1,1,0);
        trail.setGradientColors(colorStart, colorEnd);

        // create trail
        trail.create();
    }
}
