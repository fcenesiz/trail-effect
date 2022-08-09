package com.ceesiz.trail.segment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
import com.ceesiz.trail.trail.Trail3d;

public class Segment3d {

    private Trail3d trail;
    private int index;

    // Settings
    public float length;
    public float width;
    public float lerp;
    public Color colorStart;
    public Color colorEnd;

    // Coordinates
    public Vector3 position;
    public Vector3 direction;
    public Vector3 up = new Vector3();
    public Vector3 cross = new Vector3();

    public Vector3 a = new Vector3();
    public Vector3 b = new Vector3();
    public Vector3 c = new Vector3();
    public Vector3 d = new Vector3();

    public Segment3d(
            Trail3d trail,
            Vector3 position,
            float lerp,
            float length, float width,
            Vector3 direction){
        this.trail = trail;
        this.index = 0;
        this.position = position;
        this.lerp = lerp;
        this.length = length;
        this.width = width;
        this.direction = direction;
        this.up.set(Vector3.Y);
        lerpColors();
    }

    public Segment3d(
            Trail3d trail, int index,
            float ax, float ay, float az,
            float bx, float by, float bz,
            float lerp,
            float length, float width,
            Vector3 direction){
        this.trail = trail;
        this.index = index;
        this.a.set(ax, ay, az);
        this.b.set(bx, by, bz);
        this.lerp = lerp;
        this.length = length;
        this.width = width;
        this.direction = direction;
        this.up.set(Vector3.Y);
        lerpColors(trail.getSegmentList().get(index - 1));
    }

    public void render(ImmediateModeRenderer20 renderer) {
        // TRIANGLE 1
        renderer.color(colorStart);
        renderer.vertex(a.x, a.y, a.z);
        renderer.color(colorStart);
        renderer.vertex(b.x, b.y, b.z);
        renderer.color(colorEnd);
        renderer.vertex(c.x, c.y, c.z);


        // TRIANGLE 2
        renderer.color(colorEnd);
        renderer.vertex(c.x, c.y, c.z);
        renderer.color(colorEnd);
        renderer.vertex(d.x, d.y, d.z);
        renderer.color(colorStart);
        renderer.vertex(a.x, a.y, a.z);
    }

    public void lerp(Vector3 targetDirection) {
        normalizeUp();
        this.direction.lerp(targetDirection, lerp);

        this.direction.setLength(length);
        this.cross.setLength(width * 0.5f);

        a.set(position);
        a.add(cross);

        b.set(position);
        b.sub(cross);

        c.set(position);
        c.add(direction);
        c.sub(cross);

        d.set(position);
        d.add(direction);
        d.add(cross);

    }

    public void lerp(Vector3 targetDirection, Vector3 pC, Vector3 pD) {
        normalizeUp();
        this.direction.lerp(targetDirection, lerp);
        a.set(pD);
        b.set(pC);

        float distAB = a.dst(b);
        float crossWidth = (distAB - width) * 0.5f;
        this.cross.setLength(crossWidth);

        c.slerp(b, lerp);
        c.add(direction);
        //c.add(cross);

        d.slerp(a, lerp);
        d.add(direction);
        d.sub(cross);

        if (distAB < c.dst(d)){
            c.sub(cross);
            d.add(cross);
        }

    }

    public void lerpColors(Segment3d previousSegment) {
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

    public void normalizeUp() {
        cross.set(direction).crs(up);
        up.set(cross).crs(direction).nor();
    }

}
