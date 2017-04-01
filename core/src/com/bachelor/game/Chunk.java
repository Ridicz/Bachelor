package com.bachelor.game;

import com.badlogic.gdx.math.Vector2;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

  private int positionX;

  private int positionY;

  private Vector2 startPosition;

  private Block[][][] storage;

  public Chunk(int positionX, int positionY) {
    this.positionX = positionX * WIDTH;
    this.positionY = positionY * LENGTH;
    storage = new Block[WIDTH][HEIGHT][LENGTH];
  }

  public Block getBlock(int x, int y, int z) {
    int indexX = x / 16;
    int indexY = y / 16;
    int indexZ = z / 16;

    return storage[indexX][indexY][indexZ];
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    storage[x % WIDTH][y % HEIGHT][z % LENGTH] = new Block(new Position(positionX + x, y, positionY + z), type);
  }

  public Block[][][] getStorage() {
    return storage;
  }
}
