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

  public static final float SIZE = 0.2f;

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

  private Vector3 movement = new Vector3();

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

//    position.add(velocity.x, 0f, velocity.y);

    movement.add(velocity.x, 0f, velocity.y);
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
      movement.y = 1f;
    }
  }

  public void update() {
    Vector3 shift = new Vector3(movement.x, 0f, 0f);

    if (! checkCollision(shift)) {
      position.add(shift);

    } else {

    }

    shift.set(0f, movement.y, 0f);

    if (! checkCollision(shift)) {
      movement.add(0f, -0.01f, 0f);
      position.add(shift);
    } else {
      movement.y = 0f;
    }

    shift.set(0f, 0f, movement.z);

    if (! checkCollision(shift)) {
      position.add(shift);
    } else {

    }

    camera.position.set(position.x, position.y + CAMERA_HEIGHT, position.z);

    Vector2 v = new Vector2(movement.x, movement.z);

    v.scl(-0.1f);

    movement.x = v.x;
    movement.z = v.y;
  }

  private boolean checkCollision(Vector3 direction) {
    Block currentBlock = getCurrentChunk().getBlock(position);

    BoundingBox playerBoundingBox = new BoundingBox(position.cpy().sub(SIZE / 2f, 0f, SIZE / 2).add(direction), position.cpy().add(SIZE / 2f, HEIGHT, SIZE / 2).add(direction));

    for (Block block : getCurrentChunk().getBlocks()) {
      if (playerBoundingBox.intersects(block.getBoundingBox())) {

        System.out.println("---");
        System.out.println(position);
        System.out.println(block.getBoundingBox().getMin(new Vector3()));
        System.out.println(block.getBoundingBox().getMax(new Vector3()));
        System.out.println(playerBoundingBox.getMin(new Vector3()));
        System.out.println(playerBoundingBox.getMax(new Vector3()));
        System.out.println("---");

        return true;
      }
    }

    return false;
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
