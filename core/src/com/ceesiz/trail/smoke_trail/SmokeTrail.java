package com.ceesiz.trail.smoke_trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SmokeTrail {

    private List<SmokeSegment> segments = new ArrayList<>();
    private int segmentSize;
    private Vector2 position;
    private Vector2 direction;
    private float length;
    private float width;
    private Color color;

    public SmokeTrail(Vector2 position, Vector2 direction, float length, float width, int segmentSize, Color color) {
        this.position = position;
        this.direction = direction;
        this.length = length;
        this.width = width;
        this.segmentSize = segmentSize;
        this.color = color;

        initSegments();
    }

    private void initSegments() {
        float maxLength = length / segmentSize;
        for (int i = 0; i < segmentSize; i++) {
            SmokeSegment segment;
            if (i == 0)
                segment = new SmokeSegment(position.cpy(), direction.cpy(), width, maxLength, color.cpy(), color.cpy());
            else {
                segment = new SmokeSegment(new Vector2(), new Vector2(), width, maxLength, new Color(0,0,0,0), new Color(0,0,0,0));
            }
            segments.add(segment);
        }
    }

    public void update(float dt){
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).update(dt);
        }
    }

    public void render(ImmediateModeRenderer20 renderer){
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).render(renderer);
        }
    }

    public void setPosition(float x, float y){
        this.position.set(x, y);
    }

}
