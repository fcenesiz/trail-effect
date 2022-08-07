package com.ceesiz.trail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Main extends ApplicationAdapter {

    ImmediateModeRenderer20 renderer;
    private Matrix4 proj = new Matrix4();
    private Trail trail;
    private Trail trail2;

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

        trail2.setPosition(x + 100, y + 100);
        trail2.update(dt);

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
        trail2.render(renderer);
        renderer.end();
    }


    private void createTrail(){
        trail = new Trail(
                35,
                10,
                5,
                0.4f,
                0.1f,
                new Vector2(250, 250)
        );


        Color colorStart = Color.WHITE;
        Color colorEnd = new Color(1,1,1,0);
        trail.setGradientColors(colorStart, colorEnd);

        trail.create();

        trail2 = new Trail(
                35,
                10,
                7.5f,
                0.1f,
                0.1f,
                new Vector2(350, 450)
        );


        Color colorStart2 = Color.YELLOW;
        Color colorEnd2 = Color.GREEN;
        trail2.setGradientColors(colorStart2, colorEnd2);

        trail2.create();
    }

}
