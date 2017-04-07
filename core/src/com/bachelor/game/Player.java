package com.bachelor.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float WIDTH = 0.6f;

  public static final float CAMERA_HEIGHT = 1.6f;

  private static final float WALK_SPEED = 0.2f;

  private static final float ACCELERATION = 0.02f;

  private Position position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float horizontalVelocity = 0f;

  private float verticalVelocity = 0f;

  private Vector2 jumpDirection = new Vector2();

  public Player(PerspectiveCamera camera) {
    this(camera, new Position(1, 14, 1), new Rotation());
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

    horizontalVelocity = Math.min(WALK_SPEED, horizontalVelocity + ACCELERATION);

    Vector2 moveVector =  new Vector2();

    switch (direction) {
      case Left:
        moveVector.set(0f, -horizontalVelocity);
        break;

      case Right:
        moveVector.set(0f, horizontalVelocity);
        break;

      case Forward:
        moveVector.set(horizontalVelocity, 0f);
        break;

      case Backward:
        moveVector.set(-horizontalVelocity, 0f);
        break;
    }

    Vector2 velocity = new Vector2();
    velocity.set(moveVector.x, moveVector.y);

    Vector2 rot = new Vector2(camera.direction.x, camera.direction.z);

    velocity.rotate(rot.angle());

    jumpDirection.set(velocity);

    position.move(velocity.x, 0f, velocity.y);
  }

  private boolean isOnGround() {
    if (verticalVelocity > 0f || getCurrentChunk().getBlock(position) == null) {
      return false;
    }

    verticalVelocity = 0f;
    position.setY(getCurrentChunk().getBlock(position).getPosition().getY() + 0.99f);

    return true;
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

    if (camera.direction.y >= -0.995f && pitch > 0) {
      camera.direction.rotate(vec.crs(camera.up), -pitch);
    }

    if (camera.direction.y <= 0.995f && pitch < 0) {
      camera.direction.rotate(vec.crs(camera.up), -pitch);
    }
  }

  public void jump() {
    if (isOnGround()) {
      verticalVelocity = 1f;
    }
  }

  public void update() {
    if (!isOnGround()) {
      position.move(jumpDirection.x, 0.2f * verticalVelocity, jumpDirection.y);
      verticalVelocity = Math.max(-2f, verticalVelocity - 0.04f);
    } else {
      if (horizontalVelocity > 0f) {
        horizontalVelocity -= ACCELERATION / 2;
      }

      jumpDirection.setZero();
    }

    camera.position.set(position.getX(), position.getY() + CAMERA_HEIGHT, position.getZ());
  }

  public Chunk getCurrentChunk() {
    return World.getChunk(position);
  }

  public void action(int screenX, int screenY) {
    Block block = getTargetBlock(screenX, screenY);

    if (block != null) {
      getCurrentChunk().destroyBlock(block);
    }
  }

  private Block getTargetBlock(int screenX, int screenY) {
    Ray ray = camera.getPickRay(screenX, screenY);

    float distance = -1f;

    Block result = null;

    for (Block block : getCurrentChunk().getBlocks()) {
      Position position = new Position(block.getPosition().getX() + 0.5f, block.getPosition().getY() + 0.5f, block.getPosition().getZ() + 0.5f);

      float len = ray.direction.dot(position.getX() - ray.origin.x, position.getY() - ray.origin.y, position.getZ() - ray.origin.z);

      if (len < 0f) {
        continue;
      }

      float dist2 = position.getPosition().dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);

      if (distance >= 0f && dist2 > distance) {
        continue;
      }

      if (dist2 <= 0.5f) {
        result = block;
        distance = dist2;
      }
    }

    return result;
  }
}
