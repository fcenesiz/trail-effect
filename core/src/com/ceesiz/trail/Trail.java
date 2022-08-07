package com.ceesiz.trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Trail {

    private int segmentSize;
    private float segmentLength;
    private float segmentWidth;
    private float segmentLerp;
    private float positionLerp;
    private boolean gradient;
    private Vector2 position;
    private Vector2 tmpVec = new Vector2();
    private List<Segment> segmentList = new ArrayList<>();


    private Color colorStart;
    private Color colorEnd;

    public Trail(int segmentSize, float segmentLength, float segmentWidth, float segmentLerp, Vector2 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = 0.25f;
    }

    public Trail(int segmentSize, float segmentLength, float segmentWidth, float segmentLerp, float positionLerp, Vector2 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
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
                        Vector2.X,
                        Color.WHITE
                );
            } else {
                Segment pSegment = segmentList.get(i - 1);
                segment = new Segment(
                        this,
                        i,
                        pSegment.d.x, pSegment.d.y,
                        pSegment.c.x, pSegment.c.y, segmentLerp, pSegment.length, pSegment.width * 0.75f, pSegment.direction, Color.WHITE);
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
            if (i == 0) {
                segmentList.get(i).lerp(tmpVec, true);
            } else {
                Segment pSegment = segmentList.get(i - 1);
                segmentList.get(i).lerp(segmentList.get(i - 1).direction, pSegment.c, pSegment.d, false);
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
        setGradient(true);
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public boolean isGradient() {
        return gradient;
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
}
