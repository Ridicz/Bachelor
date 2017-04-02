package com.bachelor.game;

import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

  private Position startPosition;

  private Block[][][] storage;

  private Map<Position, Block> blocks;

  private boolean changed;

  public Chunk(float x, float y) {
    startPosition = new Position(x * WIDTH, 0, y * LENGTH);
    storage = new Block[WIDTH][HEIGHT][LENGTH];
    blocks = new HashMap<Position, Block>(4096);
    changed = true;
  }

//  public Block getBlock(int x, int y, int z) {
//    int indexX = x / 16;
//    int indexY = y / 16;
//    int indexZ = z / 16;
//
//    return storage[indexX][indexY][indexZ];
//  }

  public Position getStartPosition() {
    return startPosition;
  }

  public Position getEndPosition() {
    float x = startPosition.getX() + WIDTH;
    float y = 0;
    float z = startPosition.getZ() + WIDTH;

    return new Position(x, y, z);
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    storage[x % WIDTH][y % HEIGHT][z % LENGTH] = new Block(new Position(startPosition.getX() + x, y, startPosition.getZ() + z), type);

    Position position = new Position(startPosition.getX() + x, y, startPosition.getZ() + z);

    blocks.put(position, new Block(position, type));
  }

  public Block getBlock(float x, float y, float z) {
    int localX = Math.abs((int) x % WIDTH);
    int localY = (int) y;
    int localZ = Math.abs((int) z % LENGTH);

    return storage[localX][localY][localZ];
  }

  public Block getBlock(Position position) {
    return getBlock(position.getX(), position.getY(), position.getZ());
  }

  public List<Block> getBlocks() {
    return new LinkedList<Block>(blocks.values());
  }

  public boolean isChanged() {
    return changed;
  }

  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  public void destroyBlock(Block block) {
    Position position = new Position(block.getPosition().getX(), block.getPosition().getY(), block.getPosition().getZ());

    int localX = Math.abs((int) position.getX() % WIDTH);
    int localY = (int) position.getY();
    int localZ = Math.abs((int) position.getZ() % LENGTH);

    storage[localX][localY][localZ] = null;

    System.out.println(blocks.get(position));
    Block temp = blocks.remove(position);

    System.out.println(blocks.get(position));

    changed = true;

    System.out.println("Destroyed");
  }
}
