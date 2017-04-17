package com.bachelor.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.util.Arrays;
import java.util.TreeMap;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float WIDTH = 0.6f;

  public static final float CAMERA_HEIGHT = 1.6f;

  private static final float WALK_SPEED = 0.2f;

  private static final float ACCELERATION = 0.02f;

  private Vector3 position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float horizontalVelocity = 0f;

  private float verticalVelocity = 0f;

  private Vector2 jumpDirection = new Vector2();

  public Player(PerspectiveCamera camera) {
    this(camera, new Vector3(8f, 10f, 8f));
  }

  public Player(PerspectiveCamera camera, Vector3 position) {
    this.camera = camera;
    this.position = position;
    this.rotation = new Rotation();
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
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

    position.add(velocity.x, 0f, velocity.y);
  }

  private boolean isOnGround() {
    if (verticalVelocity > 0f || getCurrentChunk().getBlock(position) == null) {
      return false;
    }

    verticalVelocity = 0f;
    position.y = getCurrentChunk().getBlock(position).getPosition().getY() + 0.99f;

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
    if (isOnGround()) {
      if (horizontalVelocity > 0f) {
        horizontalVelocity -= ACCELERATION / 2;
      }

      jumpDirection.setZero();
    } else {
      if (!checkCollision(new Vector3(jumpDirection.x, 0.1f, jumpDirection.y))) {
        position.add(jumpDirection.x, 0.2f * verticalVelocity, jumpDirection.y);
      } else {
        position.add(0f, 0.2f * verticalVelocity, 0f);
      }

      verticalVelocity = Math.max(-2f, verticalVelocity - 0.04f);
    }

    camera.position.set(position.x, position.y + CAMERA_HEIGHT, position.z);
  }

  private boolean checkCollision(Vector3 direction) {
    Vector3 positionLatter = direction.add(position);

    Chunk currentChunk = World.getChunk(positionLatter);

//    Block lowerBlock = currentChunk.getBlock(positionLatter);
    Block upperBlock = currentChunk.getBlock(positionLatter.add(0f, 1f, 0f));

    return /*lowerBlock != null ||*/ upperBlock != null;
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

  public Block getTargetBlock(int screenX, int screenY) {
    Block result = null;
    float distance = -1;

    Ray ray = camera.getPickRay(screenX, screenY);
    Vector3 pos = new Vector3(camera.position);

    float dst = Float.MAX_VALUE;

    for (Block block : getCurrentChunk().getBlocks()) {

      float dist2 = ray.origin.dst2(pos);

      if (distance >= 0f && dist2 > distance) {
        continue;
      }

      if (Intersector.intersectRayBoundsFast(ray, pos, new Vector3(1, 1, 1))) {
        distance = dist2;

        Vector3 v = new Vector3();
        if (Intersector.intersectRayBounds(ray, new BoundingBox(new Vector3(block.getPosition().getX(),
          block.getPosition().getY(), block.getPosition().getZ()), new Vector3(block.getPosition().getX() + 1,
          block.getPosition().getY() + 1, block.getPosition().getZ() + 1)), v)) {

          float curDst = v.dst(position);

          if (curDst < dst) {
            dst = curDst;
            result = block;
          }
        }
      }
    }

    return result;
  }
}
