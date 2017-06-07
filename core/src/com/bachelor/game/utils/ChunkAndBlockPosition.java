package com.bachelor.game.utils;

import com.bachelor.game.entities.Block;
import com.bachelor.game.world.Chunk;

public class ChunkAndBlockPosition {

  private Chunk chunk;

  private IntegerPosition position;

  public ChunkAndBlockPosition() {

  }

  public ChunkAndBlockPosition(Chunk chunk, IntegerPosition position) {
    this.chunk = chunk;
    this.position = position;
  }

  public void set(Chunk chunk, IntegerPosition position) {
    this.chunk = chunk;
    this.position = position;
  }

  public void setChunk(Chunk chunk) {
    this.chunk = chunk;
  }

  public void setPosition(IntegerPosition position) {
    this.position = position;
  }

  public Chunk getChunk() {
    return chunk;
  }

  public IntegerPosition getPosition() {
    return position;
  }

  public Block getBlock() {
    if (chunk != null) {
      int x = position.getX() % Chunk.WIDTH;
      int z = position.getZ() % Chunk.LENGTH;

      return chunk.getBlock(x, position.getY(), z);
    } else {
      return null;
    }
  }

  public void reset() {
    chunk = null;
    position = null;
  }
}
