package com.ceesiz.trail.trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.ceesiz.trail.trail.segment.MeshSegment;

import java.util.ArrayList;
import java.util.List;

public class TrailModel {

    private Model model;

    private int segmentSize;
    private float segmentLength;
    private float maxSegmentLength;
    private float segmentWidth;
    private float segmentLerp;
    private float positionLerp;

    private Vector3 position;
    public Vector3 direction = new Vector3();
    public Vector3 up = new Vector3();
    private List<MeshSegment> segmentList = new ArrayList<>();

    private Color colorStart;
    private Color colorEnd;
    private MeshPartBuilder meshPartBuilder;

    private float[] vertices;

    public TrailModel(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, Vector3 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.maxSegmentLength = maxSegmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = 0.25f;
        vertices = new float[segmentSize * 42];
    }

    public TrailModel(int segmentSize, float segmentLength, float maxSegmentLength, float segmentWidth, float segmentLerp, float positionLerp, Vector3 position) {
        this.segmentSize = segmentSize;
        this.segmentLength = segmentLength;
        this.maxSegmentLength = maxSegmentLength;
        this.segmentWidth = segmentWidth;
        this.segmentLerp = segmentLerp;
        this.position = position;
        this.positionLerp = positionLerp;
        vertices = new float[segmentSize * 42];
    }


    public void create(){
        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        meshPartBuilder = modelBuilder.part(
                "TRIANGLE",
                GL20.GL_TRIANGLES,
                3,
                new Material(
                        new IntAttribute(IntAttribute.CullFace, 0),
                        new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                )
        );
        createSegments();
        model = modelBuilder.end();
    }

    public void update(float dt){
        segmentList.get(0).position.lerp(position, positionLerp);

        for (int i = 0; i < segmentList.size(); i++) {
            MeshSegment segment = segmentList.get(i);
            if (i == 0) {
                segment.set(direction, up);
            } else {
                MeshSegment pSegment = segmentList.get(i - 1);
                segment.lerp(segmentList.get(i - 1).direction, pSegment.c, pSegment.d);
            }
        }
        updateModel();
    }

    public void createSegments(){
        for (int i = 0; i < segmentSize; i++) {
            MeshSegment segment;
            if (i == 0) {
                segment = new MeshSegment(
                        this,
                        new Vector3(position),
                        segmentLerp,
                        segmentLength,
                        segmentWidth,
                        new Vector3(0, 0, -1f)
                );
                segment.set(direction, up);
                segment.create();
            } else {
                MeshSegment pSegment = segmentList.get(i - 1);
                float width = (1f - (float) i / segmentSize) * segmentWidth;
                segment = new MeshSegment(
                        this,
                        i,
                        pSegment.d.x, pSegment.d.y, pSegment.d.z,
                        pSegment.c.x, pSegment.c.y, pSegment.c.z,  segmentLerp, segmentLength, width, new Vector3(0, 0, -1f));
                segment.lerp(segmentList.get(i - 1).direction, pSegment.c, pSegment.d);
                segment.create();
            }

            segmentList.add(segment);
        }
    }

    private void updateModel(){

        int index = 0;
        for (int i = 0; i < segmentSize; i++) {
            MeshSegment meshSegment = segmentList.get(i);
            for (int j = 0; j < 42; j++) {
                    vertices[index] = meshSegment.vertices[j];
                index++;
            }
        }

            model.meshParts.get(0).mesh.setVertices(vertices);


    }

    public MeshPartBuilder getMeshPartBuilder() {
        return meshPartBuilder;
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

    public void lerpPosition(float x, float y, float z, float lerp) {
        this.position.x += lerp * (x - this.position.x);
        this.position.y += lerp * (y - this.position.y);
        this.position.z += lerp * (z - this.position.z);
    }

    public void translate(Vector3 vector) {
        this.position.add(vector);
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

    public List<MeshSegment> getSegmentList() {
        return segmentList;
    }

    public Model getModel() {
        return model;
    }

    public Vector3 getPosition() {
        return position;
    }
}
