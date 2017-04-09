package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class World {

  private static World instance;

  private List<Chunk> chunkList;

  private static ModelBuilder modelBuilder;

  private ModelInstance skydome;

  public World() {
    modelBuilder = new ModelBuilder();
    chunkList = new ArrayList<Chunk>();
    AssetManager assetManager = new AssetManager();
    assetManager.load("skydome.g3db", Model.class);
    assetManager.finishLoading();
    skydome = new ModelInstance(assetManager.get("skydome.g3db", Model.class));
    BlockRenderer.prepareTextures();
    createTestMap();
    instance = this;
  }

  private void createTestMap() {

//    for (int i = -5; i < 5; i++) {
//      for (int j = -5; j < 5; j++) {
//        chunkList.add(new Chunk(i, j));
//      }
//    }
//
//    int counter = 0;
//
//    for (int l = -5; l < 5; l++) {
//      for (int m = -5; m < 5; m++) {
//        for (int i = 0; i < 16; i++) {
//          for (int j = 0; j < 16; j++) {
//            for (int k = 0; k < 120 + l * 10; k++) {
//              chunkList.get(counter).setBlock(i, k, j, BlockType.Stone);
//            }
//          }
//        }
//
//        ++counter;
//      }
//    }

//------------------------------------------------
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        chunkList.add(new Chunk(i, j));
      }
    }

    int counter = 0;

    for (Chunk chunk : chunkList) {
      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < i; j++) {
          for (int k = 0; k < 16; k++) {
            chunk.setBlock(i, j, k, BlockType.Stone);
          }
        }
      }

      Gdx.app.log("World", "Created " + ++counter + "/" + chunkList.size() + " chunk.");
    }
//--------------------------------------------------
//    chunkList.add(new Chunk(0, 0));
//
//    for (int i = 0; i < 5; i++) {
//      for (int j = 0; j < 5; j++) {
//        chunkList.get(0).setBlock(i, 1, j, BlockType.Stone);
//      }
//    }

    short initialized = 0;

    for (Chunk chunk : chunkList) {
      chunk.initializeVisibleBlocks();
      System.out.println("Initialized " + ++initialized + "/" + chunkList.size() + " chunk.");
    }
  }

  public void renderNew(ModelBatch modelBatch, Environment environment) {
    modelBuilder.begin();

    for (Chunk chunk : chunkList) {
//      if (chunk.isVisible()) {
        modelBuilder.part("XXX", chunk.getMesh(), GL20.GL_TRIANGLES, Renderer.getMaterial());
//      }
    }

    modelBatch.render(new ModelInstance(modelBuilder.end()));
  }

  public static Chunk getChunk(Position position) {
    float x = position.getX();
    float z = position.getZ();

    Position chunkPosition;
    Position chunkEndPosition;

    for (Chunk chunk : instance.chunkList) {
      chunkPosition = chunk.getStartPosition();
      chunkEndPosition = chunk.getEndPosition();

      if (x > chunkPosition.getX() && x < chunkEndPosition.getX()) {
        if (z > chunkPosition.getZ() && z < chunkEndPosition.getZ()) {
          return chunk;
        }
      }
    }

    return null;
  }

  public ModelInstance getSkydome() {
    return skydome;
  }
}
