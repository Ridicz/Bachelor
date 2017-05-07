package com.bachelor.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.util.HashSet;
import java.util.Set;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float SIZE = 0.2f;

  public static final float CAMERA_HEIGHT = 1.6f;

  private static final float WALK_SPEED = 0.16f;

  private static final float ACCELERATION = 0.008f;

  private static final float REACH_RANGE = 5.5f;

  private Vector3 position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float horizontalVelocity = 0f;

  private float verticalVelocity = 0f;

  private Vector2 jumpDirection = new Vector2();

  private Set<InputKeys> pressedKeys = new HashSet<>();

  private Vector3 localVector = new Vector3();

  private BoundingBox boundingBox = new BoundingBox();

  public Player(PerspectiveCamera camera) {
    this(camera, new Vector3(3f, 200f, 70f));
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

  public Vector2 move(InputKeys direction) {
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

    Vector2 rotation = new Vector2(camera.direction.x, camera.direction.z);

    moveVector.rotate(rotation.angle());

    jumpDirection.set(moveVector);

    return moveVector;
  }

  public void rotate(float yaw, float pitch) {
    localVector.set(camera.direction);

    Quaternion q = new Quaternion();
    camera.view.getRotation(q);
    q.setEulerAngles(-yaw, 0f, 0f);
    Matrix4 mat = new Matrix4();
    mat.set(q);

    camera.direction.prj(mat);

    if (camera.direction.y >= -0.995f && pitch > 0) {
      camera.direction.rotate(localVector.crs(camera.up), -pitch);
    }

    if (camera.direction.y <= 0.995f && pitch < 0) {
      camera.direction.rotate(localVector.crs(camera.up), -pitch);
    }
  }

  public void isOnGround() {
    if (MathUtils.isZero(verticalVelocity) && checkCollision(localVector.set(0f, -0.1f, 0f))) {
      verticalVelocity = 0.16f;
    }
  }

  public void update() {
    if (pressedKeys.contains(InputKeys.Jump)) {
      isOnGround();
    }

    boolean moving = false;

    Vector2 horizontalShift = new Vector2();

    if (pressedKeys.contains(InputKeys.Forward)) {
      horizontalShift.set(move(InputKeys.Forward));
      moving = true;
    } else if (pressedKeys.contains(InputKeys.Backward)) {
      horizontalShift.set(move(InputKeys.Backward));
      moving = true;
    }

    if (pressedKeys.contains(InputKeys.Left)) {
      horizontalShift.add(move(InputKeys.Left));
      moving = true;
    } else if (pressedKeys.contains(InputKeys.Right)) {
      horizontalShift.add(move(InputKeys.Right));
      moving = true;
    }

    if (! moving) {
      horizontalVelocity = 0;
    }

    horizontalShift.limit(WALK_SPEED);

    Vector3 shift = new Vector3(horizontalShift.x, 0f, 0f);

    if (! checkCollision(shift)) {
      position.add(shift);
    }

    shift.set(0f, verticalVelocity, 0f);

    if (! checkCollision(shift)) {
      position.add(shift);
      verticalVelocity -= 0.01f;
    } else {
      verticalVelocity = 0f;
    }

    shift.set(0f, 0f, horizontalShift.y);

    if (! checkCollision(shift)) {
      position.add(shift);
    }

    camera.position.set(position.x, position.y + CAMERA_HEIGHT, position.z);
  }

  private boolean checkCollision(Vector3 direction) {
    boundingBox.set(position.cpy().sub(SIZE, 0f, SIZE).add(direction), position.cpy().add(SIZE, HEIGHT, SIZE).add(direction));

    for (Chunk chunk : World.getChunksInRange(position, 1f)) {
      for (Block block : chunk.getBlocks()) {
        if (boundingBox.intersects(block.getBoundingBox())) {
          return true;
        }
      }
    }

    return false;
  }

  private Chunk getCurrentChunk() {
    return World.getChunk(position);
  }

  public void action(int screenX, int screenY) {
    Block block = getTargetBlock(screenX, screenY);

    if (block != null) {
      World.getChunk(block.getPosition()).destroyBlock(block);
    }
  }

  public void setBlock(int screenX, int screenY) {
    Block block = getTargetBlock(screenX, screenY);

    if (block == null) {
      return;
    }

    Ray ray = camera.getPickRay(960, 536);

    Side selectedSide = Physics.getSelectedSide(block, ray);

    Chunk chunk = World.getChunk(block.getPosition());

    switch (selectedSide) {
      case Top:
        chunk.setBlockPlayer(block, Side.Top, BlockType.Gravel);
        break;

      case Bottom:
        chunk.setBlockPlayer(block, Side.Bottom, BlockType.Gravel);
        break;

      case Front:
        chunk.setBlockPlayer(block, Side.Front, BlockType.Gravel);
        break;

      case Back:
        chunk.setBlockPlayer(block, Side.Back, BlockType.Gravel);
        break;

      case Left:
        chunk.setBlockPlayer(block, Side.Left, BlockType.Gravel);
        break;

      case Right:
        chunk.setBlockPlayer(block, Side.Right, BlockType.Gravel);
        break;
    }
  }

  public Block getTargetBlock(int screenX, int screenY) {
    Block result = null;
    float distance = -1;

    Ray ray = camera.getPickRay(screenX, screenY);
    Vector3 pos = new Vector3(camera.position);

    float dst = Float.MAX_VALUE;

    for (Chunk chunk : World.getChunksInRange(position, REACH_RANGE)) {
      for (Block block : chunk.getBlocks()) {
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
    }

    if (dst > 5) {
      return null;
    }

    return result;
  }

  public void addKeyPressed(InputKeys key) {
    pressedKeys.add(key);
  }

  public void removeKeyPressed(InputKeys key) {
    pressedKeys.remove(key);
  }
}
