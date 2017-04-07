package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class Renderer {

  private ModelBatch modelBatch;

  private BachelorClient game;

  private Environment environment;

  private Shader shader;

  private ModelInstance instance;

  DefaultShader.Config config;

  public Renderer(BachelorClient game) {
    this.game = game;
    init();
  }

  private void init() {


    config = new DefaultShader.Config();
    config.numDirectionalLights = 1;
    config.numPointLights = 1;
    config.numBones = 1;

//        modelBatch = new ModelBatch(new DefaultShaderProvider(config));

//    modelBatch = new ModelBatch(Gdx.files.internal("world.vert.glsl"), Gdx.files.internal("world.frag.glsl"));

    modelBatch = new ModelBatch();

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  public void render() {
//    Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond()));

    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

    game.getCamera().update();
    modelBatch.begin(game.getCamera());

    instance = new ModelInstance(game.getWorld().render());

//    modelBatch.render(game.getWorld().getSkydome());
    modelBatch.render(instance);

    modelBatch.end();
//    modelBatch.dispose();
  }
}