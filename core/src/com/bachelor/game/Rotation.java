package com.bachelor.game;

import com.badlogic.gdx.math.MathUtils;

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
//    this.yaw = yaw;
//    this.pitch = pitch;

    this.pitch = MathUtils.clamp((this.pitch + pitch) % 360, -90, 90);
    this.yaw = (this.yaw + yaw) % 360;

    System.out.println(pitch + " " + yaw);
  }
}
