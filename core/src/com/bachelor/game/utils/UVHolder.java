package com.bachelor.game.utils;

public class UVHolder {

  private static final short imageWidth = 4096;

  private static final short imageHeight = 4096;

  private static final short regionSize = 80;

  private float u1;

  private float u2;

  private float v1;

  private float v2;

  public UVHolder(int x, int y) {
    u1 = (x * 256f) / imageWidth;
    u2 = u1 + 256f / imageWidth;
    v1 = (y * 256f) / imageHeight;
    v2 = v1 + 256f /imageHeight;
  }

  public float getU1() {
    return u1;
  }

  public float getU2() {
    return u2;
  }

  public float getV1() {
    return v1;
  }

  public float getV2() {
    return v2;
  }
}
