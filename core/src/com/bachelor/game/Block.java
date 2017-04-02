package com.bachelor.game;

public class Block extends Item {

  private BlockType blockType;

  private Position position;

  public Block(Position position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
  }

  public Block(Position position) {
    super();
    this.position = position;
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

    return position.equals(block.position);
  }

  @Override
  public int hashCode() {
    return position.hashCode();
  }
}
