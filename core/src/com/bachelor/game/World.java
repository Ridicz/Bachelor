package com.bachelor.game;

import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import java.util.LinkedList;
import java.util.List;

public class World {

  private static final int CHUNK_SIZE = 16;

  private static final int WORLD_HEIGHT = 256;

  private static final int SEA_LEVEL = 64;

  private static final float GRAVITY = 32f;

  private static World instance;

  private List<Chunk> chunkList;

  private World() {
    chunkList = new LinkedList<Chunk>();
    createTestMap();
  }

  public static World getInstance() {
    if (instance == null) {
      instance = new World();
      return instance;
    }

    return instance;
  }

  private void createTestMap() {
    Chunk chunk = new Chunk(0, 0);
    chunk.setBlock(1, 0, 0, BlockType.Gravel);
    chunk.setBlock(2, 0, 0, BlockType.Gravel);
    chunk.setBlock(3, 0, 0, BlockType.Gravel);
    chunk.setBlock(4, 0, 0, BlockType.Gravel);
    chunk.setBlock(0, 0, 1, BlockType.Gravel);
    chunk.setBlock(0, 0, 2, BlockType.Gravel);
    chunk.setBlock(0, 0, 3, BlockType.Gravel);
    chunk.setBlock(0, 1, 0, BlockType.Gravel);
    chunkList.add(chunk);
  }

  public void render(MeshBuilder meshBuilder) {
    for (Chunk chunk : chunkList) {
      chunk.render(meshBuilder);
    }
  }
}
