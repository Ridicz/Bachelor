package com.bachelor.game;

import com.badlogic.gdx.math.Vector3;

public class IntegerPosition {

  private int x;

  private int y;

  private int z;

  public IntegerPosition(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getZ() {
    return this.z;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setZ(int z) {
    this.z = z;
  }

  public IntegerPosition add(IntegerPosition position) {
    return new IntegerPosition(this.x + position.getX(), this.y + position.getY(), this.z + position.getZ());
  }

  public IntegerPosition sub(IntegerPosition position) {
    return new IntegerPosition(this.x - position.getX(), this.y - position.getY(), this.z - position.getZ());
  }

  public Vector3 getPositionVector() {
    return new Vector3(x, y, z);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    IntegerPosition otherPosition = (IntegerPosition) other;

    return this.x == otherPosition.x && this.y == otherPosition.y && this.z == otherPosition.z;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    result = 31 * result + z;
    return result;
  }

  @Override
  public String toString() {
    return "IntegerPosition{" +
      "x=" + x +
      ", y=" + y +
      ", z=" + z +
      '}';
  }
}
