package com.bachelor.game;

import java.util.HashSet;
import java.util.Set;

public class Block extends Item {

  private static int currentId = 0;

  private final int blockId;

  private BlockType blockType;

  private Position position;

  private Set<Side> visibleSides;

  public Block(Position position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
    this.blockId = currentId++;
    this.visibleSides = new HashSet<Side>();
  }

  public Position getPosition() {
    return position;
  }

  public BlockType getBlockType() {
    return blockType;
  }

  public int getBlockId() {
    return blockId;
  }

  public Set<Side> getVisibleSides() {
    return visibleSides;
  }

  public void addVisibleSide(Side side) {
    visibleSides.add(side);
  }

  public void deleteVisibleSide(Side side) {
    visibleSides.remove(side);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Block block = (Block) o;

    return blockId == block.blockId;
  }

  @Override
  public int hashCode() {
    return blockId;
  }
}
