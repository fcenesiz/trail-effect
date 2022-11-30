package com.ceesiz.trail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.ceesiz.trail.trail.TrailModel;

import java.util.Arrays;

public class Example3dWithModelBatch extends ApplicationAdapter {

    ModelBatch batch;

    TrailModel trailModel;
    ModelInstance trailInstance;
    ModelInstance instanceXYZ;
    ModelInstance instancePlane;
    Camera camera;
    CameraInputController cameraInputController;

    @Override
    public void create() {
        batch = new ModelBatch();
        setupCamera();
        setupXYZ(new ModelBuilder());
        setupPlane(new ModelBuilder());
        trailInstance = new ModelInstance(
                this.setupTrail(),
                0, 0, 0
        );
    }

    public void update(float dt){
        float x = Gdx.input.getX() - Gdx.graphics.getWidth() * 0.5f;
        float z = Gdx.graphics.getHeight() -  Gdx.input.getY() - Gdx.graphics.getHeight() * 0.5f;

        trailModel.lerpPosition(x, 0, -z, 0.5f);
        trailModel.up.set(Vector3.Y);
        trailModel.direction.set(new Vector3(x, 0 , -z).add(trailModel.getPosition()));
        trailModel.update(dt);

        camera.update();
    }

    @Override
    public void render() {
        this.update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |
                GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin(camera);
        batch.render(Arrays.asList(
                instanceXYZ,
                instancePlane,
                trailInstance
        ));
        batch.end();
    }

    private Model setupTrail(){
        trailModel = new TrailModel(
                35, // segment size
                1f, // segment length
                5f, // max segment length
                1f, // segment width
                0.4f, // segment rotation lerp
                1f, // trail position lerp
                new Vector3(0, 0, 0) // begin position
        );

        trailModel.setGradientColors(Color.WHITE, new Color(1,1,1,0));
        trailModel.create();

        return trailModel.getModel();
    }

    private void setupCamera() {
        camera = new PerspectiveCamera(
                55f, // görüş açısı
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        camera.position.set(-10, 50, 55);
        camera.lookAt(0f, 0f, 0); // odak noktası
        camera.up.set(Vector3.Y);
        camera.near = 0.1f; // kameranın gördüğü en yakın mesafe
        camera.far = 10000f; // kameranın gördüğü en uzak görüntülediği mesafe

        cameraInputController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);
    }

    private void setupXYZ(ModelBuilder modelBuilder) {
        MeshPartBuilder meshPartBuilder; // mesh, noktalardan tanımlanmış 3d nesneyi ifade eder

        modelBuilder.begin();

        meshPartBuilder = modelBuilder.part(
                "Line",
                GL20.GL_LINES,
                3,
                new Material()
        );

        meshPartBuilder.setColor(Color.RED);
        meshPartBuilder.line(
                -100, 0, 0,
                100, 0, 0
        );
        meshPartBuilder.setColor(Color.GREEN);
        meshPartBuilder.line(
                0, -100, 0,
                0, 100, 0
        );
        meshPartBuilder.setColor(Color.CYAN);
        meshPartBuilder.line(
                0, 0, 100,
                0, 0, -100
        );
        Model model = modelBuilder.end();

        instanceXYZ = new ModelInstance(
                model,
                0f,
                0f,
                0f
        );
    }

    private void setupPlane(ModelBuilder modelBuilder) {
        MeshPartBuilder meshPartBuilder; // mesh, noktalardan tanımlanmış 3d nesneyi ifade eder

        modelBuilder.begin();

        meshPartBuilder = modelBuilder.part(
                "Line",
                GL20.GL_TRIANGLES,
                3,
                new Material(
                        new IntAttribute(IntAttribute.CullFace, 0),
                        new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                )
        );

        meshPartBuilder.setColor(Color.RED);
        meshPartBuilder.triangle(
                new Vector3(50, -5, 50),
                new Vector3(-50, -5, 50),
                new Vector3(-50, -5, -50)
        );

        Model model = modelBuilder.end();

        instancePlane = new ModelInstance(
                model,
                0f,
                0f,
                0f
        );
    }

}
