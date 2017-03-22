package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import static com.badlogic.gdx.Gdx.gl;

public class Renderer {

  private ModelBatch modelBatch;

  private ModelInstance ground;

  private BachelorClient game;

  private Environment environment;

  public Renderer(BachelorClient game) {
    this.game = game;
    init();
  }

  private void init() {
    modelBatch = new ModelBatch(new DefaultShaderProvider());

    Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));

    ModelBuilder modelBuilder = new ModelBuilder();

    modelBuilder.begin();
    MeshPartBuilder meshPartBuilder = modelBuilder.part("planeGround", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, matWhite);
    meshPartBuilder.rect(-50f, 0f, -50f, -50f, 0f, 50f, 50f, 0f, 50f,  50f, 0f, -50f,0f, 1f, 0f);
    Node planeGround = modelBuilder.node();
    planeGround.id = "planeGround";
    planeGround.translation.set(0, 0, 0);
    ground = new ModelInstance(modelBuilder.end());

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
  }

  public void render() {
    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    game.getCamera().update();

    modelBatch.begin(game.getCamera());
    modelBatch.render(ground);
    modelBatch.end();
  }

  private void updateCamera() {

  }
}
