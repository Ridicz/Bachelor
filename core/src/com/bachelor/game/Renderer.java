package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.Gdx.gl;

public class Renderer {

  private ModelBatch modelBatch;

  private ModelInstance ground;

  private BachelorClient game;

  private MeshBuilder meshBuilder;

  private Environment environment;

  public Renderer(BachelorClient game) {
    this.game = game;
    init();
  }

  private void init() {
    modelBatch = new ModelBatch(new DefaultShaderProvider());
    meshBuilder = new MeshBuilder();

    Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
    Material texMat = new Material(TextureAttribute.createDiffuse(new Texture("gravel.jpg")));

    ModelBuilder modelBuilder = new ModelBuilder();

    modelBuilder.begin();
    MeshPartBuilder meshPartBuilder = modelBuilder.part("planeGround", GL20.GL_TRIANGLES, VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, texMat);
    meshPartBuilder.rect(-2f, 0f, -2f, -2f, 0f, 2f, 2f, 0f, 2f,  2f, 0f, -2f,0f, 1f, 0f);
    Node planeGround = modelBuilder.node();
    planeGround.id = "planeGround";
    planeGround.translation.set(0, 0, 0);
    ground = new ModelInstance(modelBuilder.end());

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
  }

  public void render() {
    Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond()));

    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    game.getCamera().update();
    modelBatch.begin(game.getCamera());
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
    game.getWorld().render(meshBuilder);

    ModelBuilder builder = new ModelBuilder();
    builder.begin();
//    builder.part("Box", meshBuilder.end(), GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BROWN)));
    builder.part("Box", meshBuilder.end(), GL20.GL_TRIANGLES, new Material(TextureAttribute.createDiffuse(new Texture("gravel.jpg"))));
    Model model = builder.end();
    ModelInstance instance = new ModelInstance(model);

//    modelBatch.render(instance);
    modelBatch.render(ground);
    modelBatch.end();
  }
}
