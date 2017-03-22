package com.bachelor.game;

import java.util.LinkedList;
import java.util.List;

public class World {

  private static final int CHUNK_SIZE = 16;
  private static final int WORLD_HEIGHT = 256;
  private static final int SEA_LEVEL = 64;
  private static final float GRAVITY = 32f;

  private List<Chunk> chunkList;

  private World() {
    chunkList = new LinkedList<Chunk>();
  }

  public World createWorld() {
    World world = new World();

    return world;
  }
}
