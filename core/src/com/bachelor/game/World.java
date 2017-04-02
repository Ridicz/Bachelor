package com.bachelor.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {

  private static final int CHUNK_SIZE = 16;

  private static final int WORLD_HEIGHT = 256;

  private static final int SEA_LEVEL = 64;

  private static final float GRAVITY = 32f;

  private static World instance;

  private List<Chunk> chunkList;

  private List<Mesh> meshList;

  private static ModelBuilder modelBuilder;

  private static MeshBuilder meshBuilder;

  private Material material;

  private BlockRenderer blockRenderer;

  private ModelInstance skydome;

  private World() {
    chunkList = new ArrayList<Chunk>();
    meshList = new ArrayList<Mesh>();
    createTestMap();
    material = new Material(TextureAttribute.createDiffuse(new Texture("atlas.jpg")));
    blockRenderer = new BlockRenderer();
    AssetManager a = new AssetManager();
    a.load("skydome.g3db", Model.class);
    a.finishLoading();

    skydome = new ModelInstance(a.get("skydome.g3db", Model.class));
  }

  public static World getInstance() {
    if (instance == null) {
      instance = new World();
      return instance;
    }

    return instance;
  }

  public static void setModelBuilder(ModelBuilder modelBuilder) {
    World.modelBuilder = modelBuilder;
  }

  public static void setMeshBuilder(MeshBuilder meshBuilder) {
    World.meshBuilder = meshBuilder;
  }

  private void createTestMap() {
    for (int i = -16; i < 16; i++) {
      for (int j = -16; j < 16; j++) {
        chunkList.add(new Chunk(i, j));
      }
    }

    for (int i = 0; i < 128; i++) {
      for (Chunk chunk : chunkList) {
        chunk.setBlock(0, i, 15, BlockType.Gravel);
        chunk.setBlock(15, i, 0, BlockType.Stone);
        chunk.setBlock(0, i, 0, BlockType.Gravel);
        chunk.setBlock(15, i, 15, BlockType.Stone);

        if (i < 16) {
          chunk.setBlock(0, 15, i, BlockType.Gravel);
          chunk.setBlock(i, 15, 0, BlockType.Stone);
          chunk.setBlock(i, 15, 15, BlockType.Gravel);
          chunk.setBlock(15, 15, i, BlockType.Stone);

          chunk.setBlock(0, 31, i, BlockType.Gravel);
          chunk.setBlock(i, 31, 0, BlockType.Stone);
          chunk.setBlock(i, 31, 15, BlockType.Gravel);
          chunk.setBlock(15, 31, i, BlockType.Stone);

          chunk.setBlock(0, 47, i, BlockType.Gravel);
          chunk.setBlock(i, 47, 0, BlockType.Stone);
          chunk.setBlock(i, 47, 15, BlockType.Gravel);
          chunk.setBlock(15, 47, i, BlockType.Stone);

          chunk.setBlock(0, 63, i, BlockType.Gravel);
          chunk.setBlock(i, 63, 0, BlockType.Stone);
          chunk.setBlock(i, 63, 15, BlockType.Gravel);
          chunk.setBlock(15, 63, i, BlockType.Stone);
        }
      }
    }

    for (int i = -5; i < 5; i++) {
      for (int j = -5; j < 5; j++) {
        chunkList.add(new Chunk(i, j));
      }
    }

//    chunkList.add(new Chunk(0, 0));


//    Chunk chunk = new Chunk(0f, 0f);
//
//    chunk.setBlock(1, 0, 1, BlockType.Stone);
//    chunk.setBlock(1, 1, 0, BlockType.Stone);
//    chunk.setBlock(2, 2, 0, BlockType.Stone);
//    chunk.setBlock(3, 3, 0, BlockType.Stone);
//    chunk.setBlock(4, 4, 0, BlockType.Stone);
//
//    chunkList.add(chunk);


//    for (int i = 0; i < 3; i++) {
//      for (int j = 1; j < 245; j++) {
//        for (int k = 0; k < 3; k++) {
////          chunkList.get(0).setBlock(i, j, k, BlockType.Gravel);
////          chunkList.get(1).setBlock(i, j, k, BlockType.Gravel);
////          chunkList.get(2).setBlock(i, j, k, BlockType.Gravel);
////          chunkList.get(3).setBlock(i, j, k, BlockType.Gravel);
//
//          for (Chunk chunk : chunkList) {
////            chunk.setBlock(0, j, 0, BlockType.Stone);
////            chunk.setBlock(0, j, 15, BlockType.Stone);
////            chunk.setBlock(15, j, 0, BlockType.Stone);
////            chunk.setBlock(15, j, 15, BlockType.Stone);
//
//            chunk.setBlock(i, j, k, BlockType.Stone);
//            chunk.setBlock(i + 3, 1, k + 3, BlockType.Stone);
//          }
//        }
//      }
//    }
  }

  public Model render() {
    for (Chunk chunk : chunkList) {
      if (chunk.isChanged()) {
        meshList.addAll(blockRenderer.renderChunk(chunk.getBlocks()));
      }

      chunk.setChanged(false);
    }

    modelBuilder.begin();

    for (Mesh mesh : meshList) {
      modelBuilder.part("World", mesh, GL20.GL_TRIANGLES, material);
    }

    return modelBuilder.end();
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
