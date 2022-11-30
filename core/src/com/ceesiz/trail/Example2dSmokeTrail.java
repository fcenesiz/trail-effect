package com.ceesiz.trail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ceesiz.trail.smoke_trail.SmokeTrail;
import com.ceesiz.trail.trail.Trail;

public class Example2dSmokeTrail extends ApplicationAdapter {

    ImmediateModeRenderer20 renderer;
    private Matrix4 proj = new Matrix4();
    private SmokeTrail trail;


    @Override
    public void create() {
        renderer = new ImmediateModeRenderer20(false, true, 0);

        this.createTrail();

    }

    public void update(float dt){

        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() -  Gdx.input.getY();

        trail.setPosition(x, y);
        trail.update(dt);


    }

    @Override
    public void render() {

        this.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |
                GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        proj.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        renderer.begin(proj, GL20.GL_TRIANGLES);
        trail.render(renderer);

        renderer.end();
    }


    private void createTrail(){
        trail = new Trail(
                20, // segment size
                5, // segment length
                5, // max segment length
                15, // segment width
                0.4f, // segment rotation lerp
                0.1f, // trail position lerp
                new Vector2(250, 250) // begin position
        );

        // set start and end colors
        Color colorStart = Color.WHITE;
        Color colorEnd = new Color(1,1,1,0);
        trail.setGradientColors(colorStart, colorEnd);

        // create trail
        trail.create();
    }

}
