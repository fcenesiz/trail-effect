package com.ceesiz.trail.smoke_trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;

public class SmokeSegment {

    public Vector2 position;
    public Vector2 a = new Vector2();
    public Vector2 b = new Vector2();
    public Vector2 c = new Vector2();
    public Vector2 d = new Vector2();
    public float length;
    public Vector2 direction;
    public Vector2 crs = new Vector2();
    public float width;
    public float maxLength;
    public Color colorStart;
    public Color colorEnd;
    public float elongationRate;

    public SmokeSegment(Vector2 position, Vector2 direction, float width, float maxLength, Color colorStart, Color colorEnd) {
        this.position = position;
        this.direction = direction;
        this.width = width;
        this.maxLength = maxLength;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;

        this.length = 0;
    }

    public void update(float dt){
        if (width != maxLength){
            if (width < maxLength){
                width += dt * elongationRate;
            }
            if (width > maxLength){
                width = maxLength;
            }
        }
    }

    public void render(ImmediateModeRenderer20 renderer) {
        // TRIANGLE 1
        renderer.color(colorStart);
        renderer.vertex(a.x, a.y, 0);
        renderer.color(colorStart);
        renderer.vertex(b.x, b.y, 0);
        renderer.color(colorEnd);
        renderer.vertex(c.x, c.y, 0);


        // TRIANGLE 2
        renderer.color(colorEnd);
        renderer.vertex(c.x, c.y, 0);
        renderer.color(colorEnd);
        renderer.vertex(d.x, d.y, 0);
        renderer.color(colorStart);
        renderer.vertex(a.x, a.y, 0);
    }

    @Override
    public String toString() {
        return "Segment{" +
                "\n\ta=" + a +
                ",\n\t b=" + b +
                ",\n\t c=" + c +
                ",\n\t d=" + d +
                "\n}";
    }

}
