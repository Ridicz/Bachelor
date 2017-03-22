package com.bachelor.game;

public class Rotation {

  private float yaw;

  private float pitch;

  public Rotation() {
    this(0, 0);
  }

  public Rotation(float yaw, float pitch) {
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public void setRotation(float yaw, float pitch) {
    this.yaw = yaw;
    this.pitch = pitch;
  }
}
