package com.bachelor.game.graphics;

import com.bachelor.game.BachelorClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class Renderer {

  private static Material material;

  private ModelBatch modelBatch;

  private BachelorClient game;

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

    modelBatch = new ModelBatch(new DefaultShaderProvider(config));

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    Texture texture = new Texture("atlas.png");
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    material = new Material(TextureAttribute.createDiffuse(texture));
  }

  public static Material getMaterial() {
    return material;
  }

  public void render() {
    Gdx.graphics.setTitle("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));

    Gdx.gl.glClear(GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_COLOR_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL30.GL_COVERAGE_BUFFER_BIT_NV:0));

    game.getCamera().update();
    modelBatch.begin(game.getCamera());

    game.getWorld().renderWorld(modelBatch);

    modelBatch.end();
  }
}