package com.ceesiz.trail.trail.segment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.ceesiz.trail.trail.Trail;

public class Segment {

    private Trail trail;
    private int index;
    public Vector2 position = new Vector2();
    public Vector2 a = new Vector2();
    public Vector2 b = new Vector2();
    public Vector2 c = new Vector2();
    public Vector2 d = new Vector2();
    public float length;
    public Vector2 direction;
    public Vector2 crs = new Vector2();
    public float width;
    public float lerp = 0.25f;
    public Color colorStart;
    public Color colorEnd;

    public Segment(Trail trail, int index, float x, float y, float lerp, float length, float width, Vector2 direction) {
        this.trail = trail;
        this.index = index;
        this.position.set(position);
        this.length = length;
        this.direction = direction;
        this.width = width;
        this.lerp = lerp;
        lerpColors();
    }

    public Segment(Trail trail, int index, float ax, float ay, float bx, float by, float lerp, float length, float width, Vector2 direction) {
        this.trail = trail;
        this.index = index;
        this.a.set(ax, ay);
        this.b.set(bx, by);
        this.length = length;
        this.width = width;
        this.lerp = lerp;
        this.direction = new Vector2(direction);
        lerpColors(trail.getSegmentList().get(index - 1));
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

    public void lerp(Vector2 targetDirection) {
        this.direction.lerp(targetDirection, lerp);
        this.crs.set(direction.nor()).rotateDeg(90);
        this.direction.setLength(length);
        this.crs.setLength(width * 0.5f);

        a.set(position);
        a.add(crs);

        b.set(position);
        b.sub(crs);

        c.set(position);
        c.add(direction);
        c.sub(crs);

        d.set(position);
        d.add(direction);
        d.add(crs);

    }

    public void lerp(Vector2 targetDirection, Vector2 pC, Vector2 pD) {
        this.direction.lerp(targetDirection, lerp);
        this.crs.set(direction.nor()).rotateDeg(90);
        this.direction.setLength(length);

        a.set(pD);
        b.set(pC);

        float distAB = a.dst(b);
        float crsWidth = (distAB - width) * 0.5f;
        this.crs.setLength(crsWidth);

        c.set(b.x, b.y);
        c.add(direction);
        c.add(crs);

        d.set(a.x, a.y);
        d.add(direction);
        d.sub(crs);

        if (distAB < c.dst(d)){
            c.sub(crs);
            d.add(crs);
        }

    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void lerpColors(Segment previousSegment) {
        if (colorStart == null)
            colorStart = new Color();
        if (colorEnd == null)
            colorEnd = new Color();
        colorStart.set(previousSegment.colorEnd);
        colorEnd.set(colorStart);
        if (trail.getColorEnd().a == 0)
            colorEnd.lerp(trail.getColorEnd(), 2f / (trail.getSegmentSize() - 1));
        else
            colorEnd.lerp(trail.getColorEnd(), 0.5f / (trail.getSegmentSize() - 1));
    }

    public void lerpColors() {
        if (colorStart == null)
            colorStart = new Color();
        if (colorEnd == null)
            colorEnd = new Color();
        colorStart.set(trail.getColorStart());
        colorEnd.set(colorStart);
        if (trail.getColorEnd().a == 0)
            colorEnd.lerp(trail.getColorEnd(), 2f / trail.getSegmentSize());
        else
            colorEnd.lerp(trail.getColorEnd(), 0.5f / trail.getSegmentSize());
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
