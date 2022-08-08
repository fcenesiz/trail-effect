package com.ceesiz.trail;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Trail3d {

    private int segmentSize;
    private float segmentLength;
    private float maxSegmentLength;
    private float segmentWidth;
    private float segmentLerp;
    private float positionLerp;

    private Vector3 position;
    private Vector3 tmpVec = new Vector3();
    private List<Segment3d> segmentList = new ArrayList<>();

    private Color colorStart;
    private Color colorEnd;

    public Trail3d(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, Vector3 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.maxSegmentLength = maxSegmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = 0.25f;
    }

    public Trail3d(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, float positionLerp, Vector3 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.maxSegmentLength = maxSegmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = positionLerp;
    }

    public void create() {
        this.segmentList = new ArrayList<>();
        for (int i = 0; i < segmentSize; i++) {
            Segment3d segment;
            if (i == 0) {
                segment = new Segment3d(
                        this,
                        new Vector3(position),
                        segmentLerp,
                        segmentLength,
                        segmentWidth,
                        new Vector3(0, 0, 1f)
                );
            } else {
                Segment3d pSegment = segmentList.get(i - 1);
                float width = (1f - (float) i / segmentSize) * segmentWidth;
                segment = new Segment3d(
                        this,
                        i,
                        pSegment.d.x, pSegment.d.y, pSegment.d.z,
                        pSegment.c.x, pSegment.c.y, pSegment.c.z,  segmentLerp, pSegment.length, width, pSegment.direction);
            }
            segmentList.add(segment);
        }
    }

    public void update(float dt) {

        segmentList.get(0).position.lerp(position, positionLerp);

        // direction
        tmpVec.set(position).sub(segmentList.get(0).position);
        tmpVec.scl(-1);

        segmentList.get(0).position.set(position);

        for (int i = 0; i < segmentList.size(); i++) {
            Segment3d segment = segmentList.get(i);
            if (i == 0) {
                segment.lerp(tmpVec);
            } else {
                Segment3d pSegment = segmentList.get(i - 1);
                segment.lerp(segmentList.get(i - 1).direction, pSegment.c, pSegment.d);
            }
        }

    }

    public void render(ImmediateModeRenderer20 renderer20) {
        for (int i = 0; i < segmentSize; i++) {
            segmentList.get(i).render(renderer20);
        }
    }

    public void setGradientColors(Color colorStart, Color colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    public void setColor(Color color) {
        this.colorStart = color;
        this.colorEnd = color;
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public Color getColorEnd() {
        return colorEnd;
    }

    public Color getColorStart() {
        return colorStart;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public List<Segment3d> getSegmentList() {
        return segmentList;
    }

    @Override
    public String toString() {
        String string = "Trail{";
        for (int i = 0; i < segmentList.size(); i++) {
            string += "\n" + segmentList.get(i).toString();
        }
        string += "\n}";
        return string;
    }

}
