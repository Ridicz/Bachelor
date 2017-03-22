package com.bachelor.game;

import com.badlogic.gdx.math.Vector3;

public class Position {

  private Vector3 position;

  public Position() {
    this(0, 0, 0);
  }

  public Position(float x, float y, float z) {
    this.position = new Vector3(x, y, z);
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public float getZ() {
    return position.z;
  }

  public void move(float x, float y, float z) {
    position.add(x, y, z);
  }

  public Vector3 getPosition() {
    return new Vector3(position.x, position.y, position.z);
  }
}
