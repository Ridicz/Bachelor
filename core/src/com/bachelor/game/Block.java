package com.bachelor.game;

public class Block extends Item {

  private static final short SIZE = 1;

  private BlockType blockType;

  private Position position;

  public Block(Position position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
  }

  public Position getPosition() {
    return position;
  }

  public BlockType getBlockType() {
    return blockType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Block block = (Block) o;

    return blockType == block.blockType && position.equals(block.position);
  }

  @Override
  public int hashCode() {
    int result = blockType.hashCode();
    result = 31 * result + position.hashCode();
    return result;
  }
}
