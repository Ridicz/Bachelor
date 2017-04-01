package com.bachelor.game;

public class UVHolder {

  private static final short imageWidth = 1280;

  private static final short imageHeight = 1280;

  private static final short regionSize = 80;

  private float u1;

  private float u2;

  private float v1;

  private float v2;

  public UVHolder(int x, int y) {
    u1 = (x * 80f) / imageWidth;
    u2 = u1 + 80f / imageWidth;
    v1 = (y * 80f) / imageHeight;
    v2 = v1 + 80f /imageHeight;
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
