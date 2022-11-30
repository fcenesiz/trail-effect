package com.ceesiz.trail.trail.segment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.ceesiz.trail.trail.TrailModel;

public class MeshSegment {

    private TrailModel trail;
    private int index;

    // Settings
    float length;
    float width;
    float lerp;
    Color colorStart;
    Color colorEnd;

    // Coordinates
    public Vector3 position;
    public Vector3 direction;
    public Vector3 up = new Vector3();
    public Vector3 cross = new Vector3();

    public Vector3 a = new Vector3();
    public Vector3 b = new Vector3();
    public Vector3 c = new Vector3();
    public Vector3 d = new Vector3();

    public float[] vertices = new float[42];

    public MeshSegment(
            TrailModel trail,
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
        this.cross.set(Vector3.Z);
        lerpColors();
    }

    public MeshSegment(
            TrailModel trail, int index,
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
        this.cross.set(Vector3.Z);
        lerpColors(trail.getSegmentList().get(index - 1));

    }



    public void create(){
       trail.getMeshPartBuilder().triangle(
            a, colorStart,
               b, colorStart,
               c, colorEnd
       );

        trail.getMeshPartBuilder().triangle(
               c, colorEnd,
               d, colorEnd,
               a, colorStart
       );
    }

    public void set(Vector3 direction, Vector3 up) {
        this.direction = direction;
        this.up = up;
        normalizeUp();
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

        this.updateVertices();
    }

    public void lerp(Vector3 targetDirection, Vector3 pC, Vector3 pD) {
        this.direction.lerp(targetDirection, lerp);
        normalizeUp();

        this.direction.setLength(length);
        a.set(pD);
        b.set(pC);

        float distAB = a.dst(b);
        float crossWidth = (distAB - width) * 0.5f;
        this.cross.setLength(crossWidth);

        c.set(b);
        c.add(direction);
        //c.add(cross);

        d.set(a);
        d.add(direction);
        //d.sub(cross);

       //if (distAB < c.dst(d)){
       //    c.sub(cross);
       //    d.add(cross);
       //}
        this.updateVertices();
    }

    public void lerpColors(MeshSegment previousSegment) {
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

        System.out.println(direction);
        System.out.println(up);
        System.out.println(cross);
        System.out.println();
    }

    private void updateVertices(){

        vertices[0] = a.x;
        vertices[1] = a.y;
        vertices[2] = a.z;

        vertices[3] = colorStart.r;
        vertices[4] = colorStart.g;
        vertices[5] = colorStart.b;
        vertices[6] = colorStart.a;

        vertices[7] = b.x;
        vertices[8] = b.y;
        vertices[9] = b.z;

        vertices[10] = colorStart.r;
        vertices[11] = colorStart.g;
        vertices[12] = colorStart.b;
        vertices[13] = colorStart.a;

        vertices[14] = c.x;
        vertices[15] = c.y;
        vertices[16] = c.z;

        vertices[17] = colorEnd.r;
        vertices[18] = colorEnd.g;
        vertices[19] = colorEnd.b;
        vertices[20] = colorEnd.a;

        vertices[21] = c.x;
        vertices[22] = c.y;
        vertices[23] = c.z;

        vertices[24] = colorEnd.r;
        vertices[25] = colorEnd.g;
        vertices[26] = colorEnd.b;
        vertices[27] = colorEnd.a;

        vertices[28] = d.x;
        vertices[29] = d.y;
        vertices[30] = d.z;

        vertices[31] = colorEnd.r;
        vertices[32] = colorEnd.g;
        vertices[33] = colorEnd.b;
        vertices[34] = colorEnd.a;

        vertices[35] = a.x;
        vertices[36] = a.y;
        vertices[37] = a.z;

        vertices[38] = colorStart.r;
        vertices[39] = colorStart.g;
        vertices[40] = colorStart.b;
        vertices[41] = colorStart.a;

    }

}
