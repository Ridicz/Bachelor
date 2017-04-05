package com.bachelor.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {

  private static final int CHUNK_SIZE = 16;

  private static final int WORLD_HEIGHT = 256;

  private static final int SEA_LEVEL = 64;

  private static final float GRAVITY = 32f;

  private static World instance;

  private List<Chunk> chunkList;

  private Map<Chunk, List<Mesh>> map;

  private static ModelBuilder modelBuilder;

  private Material material;

  private BlockRenderer blockRenderer;

  private ModelInstance skydome;

  private World() {
    modelBuilder = new ModelBuilder();
    chunkList = new ArrayList<Chunk>();
    map = new HashMap<Chunk, List<Mesh>>();
    createTestMap();
    material = new Material(TextureAttribute.createDiffuse(new Texture("assetshd.jpg")));
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

  private void createTestMap() {
//    for (int i = -1; i < 1; i++) {
//      for (int j = -1; j < 1; j++) {
//        chunkList.add(new Chunk(i, j));
//      }
//    }
//
//    for (int i = 0; i < 128; i++) {
//      for (Chunk chunk : chunkList) {
//        chunk.setBlock(0, i, 15, BlockType.Gravel);
//        chunk.setBlock(15, i, 0, BlockType.Stone);
//        chunk.setBlock(0, i, 0, BlockType.Gravel);
//        chunk.setBlock(15, i, 15, BlockType.Stone);
//
//        if (i < 16) {
//          chunk.setBlock(0, 15, i, BlockType.Gravel);
//          chunk.setBlock(i, 15, 0, BlockType.Stone);
//          chunk.setBlock(i, 15, 15, BlockType.Gravel);
//          chunk.setBlock(15, 15, i, BlockType.Stone);
//
//          chunk.setBlock(0, 31, i, BlockType.Gravel);
//          chunk.setBlock(i, 31, 0, BlockType.Stone);
//          chunk.setBlock(i, 31, 15, BlockType.Gravel);
//          chunk.setBlock(15, 31, i, BlockType.Stone);
//
//          chunk.setBlock(0, 47, i, BlockType.Gravel);
//          chunk.setBlock(i, 47, 0, BlockType.Stone);
//          chunk.setBlock(i, 47, 15, BlockType.Gravel);
//          chunk.setBlock(15, 47, i, BlockType.Stone);
//
//          chunk.setBlock(0, 63, i, BlockType.Gravel);
//          chunk.setBlock(i, 63, 0, BlockType.Stone);
//          chunk.setBlock(i, 63, 15, BlockType.Gravel);
//          chunk.setBlock(15, 63, i, BlockType.Stone);
//        }
//      }
//    }
//
//    for (int i = -5; i < 5; i++) {
//      for (int j = -5; j < 5; j++) {
//        chunkList.add(new Chunk(i, j));
//      }
//    }

    Chunk chunk = new Chunk(0f, 0f);
    Chunk chunk2 = new Chunk(0f, 1f);
    Chunk chunk3 = new Chunk(-1f, 0f);
    Chunk chunk4 = new Chunk(1f, 0f);
    Chunk chunk5 = new Chunk(0f, -1f);

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++) {
        chunk.setBlock(i, 0, j, BlockType.Stone);

        for (int k = 0; k < 256; k++) {
          chunk2.setBlock(i, k, j, BlockType.Stone);
          chunk3.setBlock(i, k, j, BlockType.Stone);
          chunk4.setBlock(i, k, j, BlockType.Stone);
          chunk5.setBlock(i, k, j, BlockType.Stone);
        }
      }
    }

    chunkList.add(chunk);
    chunkList.add(chunk2);
    chunkList.add(chunk3);
    chunkList.add(chunk4);
    chunkList.add(chunk5);
  }

  public Model render() {
    for (Chunk chunk : chunkList) {
      if (chunk.isChanged()) {
        if (map.get(chunk) != null) {
          List list = map.remove(chunk);
          list.clear();
        }

        map.put(chunk, blockRenderer.renderChunk(chunk.getBlocks()));

        chunk.setChanged(false);

        System.out.println(map.values().size());
      }
    }

    modelBuilder.begin();

    for (List<Mesh> meshes : map.values()) {
      for (Mesh mesh : meshes) {
        modelBuilder.part("World", mesh, GL20.GL_TRIANGLES, material);
      }
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
