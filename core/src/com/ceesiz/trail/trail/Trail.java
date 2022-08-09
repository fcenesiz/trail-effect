package com.ceesiz.trail.trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.ceesiz.trail.segment.Segment;

import java.util.ArrayList;
import java.util.List;

public class Trail {

    private int segmentSize;
    private float segmentLength;
    private float maxSegmentLength;
    private float segmentWidth;
    private float segmentLerp;
    private float positionLerp;
    private Vector2 position;
    private Vector2 tmpVec = new Vector2();
    private List<Segment> segmentList = new ArrayList<>();


    private Color colorStart;
    private Color colorEnd;

    public Trail(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, Vector2 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.maxSegmentLength = maxSegmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = 0.25f;
    }

    public Trail(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, float positionLerp, Vector2 position) {
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
            Segment segment;
            if (i == 0) {
                segment = new Segment(
                        this,
                        i,
                        20, 350,
                        segmentLerp,
                        segmentLength,
                        segmentWidth,
                        Vector2.X
                );
            } else {
                Segment pSegment = segmentList.get(i - 1);
                float width = (1f - (float) i / segmentSize) * segmentWidth;
                segment = new Segment(
                        this,
                        i,
                        pSegment.d.x, pSegment.d.y,
                        pSegment.c.x, pSegment.c.y, segmentLerp, pSegment.length, width, pSegment.direction);
            }
            segmentList.add(segment);
        }
    }

    public void update(float dt) {

        segmentList.get(0).position.lerp(position, positionLerp);

        // direction
        tmpVec.set(position).sub(segmentList.get(0).position);
        tmpVec.scl(-1);


        for (int i = 0; i < segmentList.size(); i++) {
            Segment segment = segmentList.get(i);
            if (i == 0) {
                segment.lerp(tmpVec);
            } else {
                Segment pSegment = segmentList.get(i - 1);
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

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
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

    public List<Segment> getSegmentList() {
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
