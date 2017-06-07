package com.bachelor.game.entities;

import com.bachelor.game.utils.IntegerPosition;
import com.bachelor.game.enums.BlockType;
import com.bachelor.game.enums.Side;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class Block extends Item {

  private static int currentId = 0;

  private final int blockId;

  private BlockType blockType;

  private IntegerPosition position;

  private Set<Side> visibleSides;

  private static BoundingBox boundingBox = new BoundingBox();

  public Block(IntegerPosition position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
    this.blockId = currentId++;
    this.visibleSides = new HashSet<>();
//    System.out.println(blockId);
  }

  public IntegerPosition getPosition() {
    return position;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox.set(position.getPositionVector(), position.getPositionVector().cpy().add(1f, 1f, 1f));
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
