package com.bachelor.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float WIDTH = 0.6f;

  public static final float CAMERA_HEIGHT = 1.6f;

  private Position position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float velocity;

  private boolean onGround = true;

  public Player(PerspectiveCamera camera) {
    this(camera, new Position(), new Rotation());
  }

  public Player(PerspectiveCamera camera, Position position) {
    this(camera, position, new Rotation());
  }

  public Player(PerspectiveCamera camera, Position position, Rotation rotation) {
    this.camera = camera;
    this.position = position;
    this.rotation = rotation;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Rotation getRotation() {
    return rotation;
  }

  public void setRotation(Rotation rotation) {
    this.rotation = rotation;
  }

  public Tool getToolEquipped() {
    return toolEquipped;
  }

  public void setToolEquipped(Tool toolEquipped) {
    this.toolEquipped = toolEquipped;
  }

  public void forward() {
    if (onGround) {
      position.move(0.1f, 0f, 0f);
    }
  }

  public void update() {
    camera.position.set(position.getX(), position.getY() + CAMERA_HEIGHT, position.getZ());
  }
}
