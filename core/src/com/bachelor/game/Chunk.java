package com.bachelor.game;

import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector2;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

//  private MeshBuilder meshBuilder;

  private int positionX;

  private int positionY;

  private Vector2 startPosition;

  private Block[][][] storage;

  public Chunk(int positionX, int positionY) {
    this.positionX = positionX;
    this.positionY = positionY;
    storage = new Block[WIDTH][HEIGHT][LENGTH];
//    meshBuilder = new MeshBuilder();
  }

  public Block getBlock(int x, int y, int z) {
    int indexX = x / 16;
    int indexY = y / 16;
    int indexZ = z / 16;

    return storage[indexX][indexY][indexZ];
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    storage[x][y][z] = new Block(new Position(x, y, z), type);
  }

  public Block[][][] getStorage() {
    return storage;
  }

  public void render(MeshBuilder meshBuilder) {
    for (Block[][] blocks : storage) {
      for (Block[] block : blocks) {
        for (Block block1 : block) {
          if (block1 != null) {
            block1.render(meshBuilder);
          }
        }
      }
    }
  }
}
