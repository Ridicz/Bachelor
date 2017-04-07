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

  public void setX(float x) {
    position.x = x;
  }

  public void setY(float y) {
    position.y = y;
  }

  public void setZ(float z) {
    position.z = z;
  }

  public void move(float x, float y, float z) {
    position.add(x, y, z);
  }

  public Vector3 getPosition() {
    return new Vector3(position.x, position.y, position.z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Position position1 = (Position) o;

    return position.equals(position1.position);
  }

  @Override
  public int hashCode() {
    return position.hashCode();
  }
}
