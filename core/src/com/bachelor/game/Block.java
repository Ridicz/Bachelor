package com.bachelor.game;

public class Block extends Item {

  private static int currentId = 0;

  private final int blockId;

  private BlockType blockType;

  private Position position;

  public Block(Position position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
    this.blockId = currentId++;
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
