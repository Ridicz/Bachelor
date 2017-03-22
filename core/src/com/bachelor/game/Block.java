package com.bachelor.game;

public class Block extends Item {

  private BlockType blockType;

  public Block(Position position, BlockType blockType) {
    super();
    this.blockType = blockType;
  }
}
