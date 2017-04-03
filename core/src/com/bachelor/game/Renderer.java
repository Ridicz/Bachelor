package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.LinkedList;
import java.util.List;

public class Renderer {

  private ModelBatch modelBatch;

  private BachelorClient game;

  private ModelBuilder modelBuilder;

  private MeshBuilder meshBuilder;

  private Environment environment;

  private List<Model> models;

  public Renderer(BachelorClient game) {
    this.game = game;
    init();
  }

  private void init() {
    models = new LinkedList<Model>();
    modelBatch = new ModelBatch(new DefaultShaderProvider());
    meshBuilder = new MeshBuilder();
    modelBuilder = new ModelBuilder();

    BlockRenderer.setMeshBuilder(meshBuilder);

    World.setModelBuilder(modelBuilder);

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  public void render() {
    modelBatch.dispose();

    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
//    gl.glClearColor(0, 0, 0, 1);

    game.getCamera().update();
    modelBatch.begin(game.getCamera());

    modelBatch.render(game.getWorld().getSkydome());
    modelBatch.render(new ModelInstance(game.getWorld().render()));

    modelBatch.end();
    modelBatch.dispose();
  }
}