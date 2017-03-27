package com.bachelor.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.*;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float WIDTH = 0.6f;

  public static final float CAMERA_HEIGHT = 1.6f;

  private static final float WALK_SPEED = 0.05f;

  private Position position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float velocity;

  private float verticalVelocity = 0f;

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

  public void move(Direction direction) {
    if (!isOnGround()) {
      return;
    }

    Vector2 vec =  new Vector2();

    switch (direction) {
      case Left:
        vec.set(0, -WALK_SPEED);
        break;

      case Right:
        vec.set(0, WALK_SPEED);
        break;

      case Forward:
        vec.set(WALK_SPEED, 0f);
        break;

      case Backward:
        vec.set(-WALK_SPEED, 0f);
        break;
    }

    Vector2 velocity = new Vector2();
    velocity.set(vec.x, vec.y);

    Vector2 rot = new Vector2(camera.direction.x, camera.direction.z);

    velocity.rotate(rot.angle());

    position.move(velocity.x, 0f, velocity.y);
  }

  private boolean isOnGround() {
    return MathUtils.isEqual(position.getY(), 0f);
  }

  public void rotate(float yaw, float pitch) {
    Vector3 vec = new Vector3();
    vec.set(camera.direction);

    Quaternion q = new Quaternion();
    camera.view.getRotation(q);
    q.setEulerAngles(-yaw, 0f, 0f);
    Matrix4 mat = new Matrix4();
    mat.set(q);

    camera.direction.prj(mat);
    camera.direction.rotate(vec.crs(camera.up), -pitch);
  }

  public void jump() {
    if (isOnGround()) {
      verticalVelocity = 1f;
      onGround = false;
    }
  }

  public void update() {
    if (!isOnGround()) {
      position.move(0f, 0.2f * verticalVelocity, 0f);
      verticalVelocity -= 0.1f;
    }

    if (position.getY() <= 0) {
      onGround = true;
    }

    camera.position.set(position.getX(), position.getY() + CAMERA_HEIGHT, position.getZ());
  }
}
