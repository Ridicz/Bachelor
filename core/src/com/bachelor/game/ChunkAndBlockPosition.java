package com.bachelor.game;

public class ChunkAndBlockPosition {

  private Chunk chunk;

  private IntegerPosition position;

  public ChunkAndBlockPosition() {

  }

  public ChunkAndBlockPosition(Chunk chunk, IntegerPosition position) {
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
    return chunk.getBlock(position);
  }
}
