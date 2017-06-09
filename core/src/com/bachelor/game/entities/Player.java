package com.bachelor.game.entities;

import com.bachelor.game.Inventory;
import com.bachelor.game.enums.BlockType;
import com.bachelor.game.enums.InputKeys;
import com.bachelor.game.enums.Side;
import com.bachelor.game.utils.Physics;
import com.bachelor.game.utils.Rotation;
import com.bachelor.game.world.Chunk;
import com.bachelor.game.world.World;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Player {

  public static final float HEIGHT = 1.8f;

  public static final float SIZE = 0.2f;

  public static final float CAMERA_HEIGHT = 1.6f;

  public static final float REACH_RANGE = 5.5f;

  private static final float WALK_SPEED = 0.16f;

  private static final float ACCELERATION = 0.008f;

  private Vector3 position;

  private Rotation rotation;

  private Inventory inventory;

  private Tool toolEquipped;

  private PerspectiveCamera camera;

  private float horizontalVelocity = 0f;

  private float verticalVelocity = 0f;

  private Set<InputKeys> pressedKeys = new HashSet<>();

  private Vector2 jumpDirection = new Vector2();
  private Vector2 localVector2 = new Vector2();
  private Vector2 localVector2Helper = new Vector2();
  private Vector2 moveVector = new Vector2();

  private Vector3 localVector = new Vector3();

  private BoundingBox boundingBox = new BoundingBox();

  public Player(PerspectiveCamera camera) {
    this(camera, new Vector3(3f, 120f, 70f));
  }

  public Player(PerspectiveCamera camera, Vector3 position) {
    this.camera = camera;
    this.position = position;
    this.rotation = new Rotation();
  }

  public Vector3 getPosition() {
    return position;
  }

  public String printPosition() {
    return "X: [" +
      new DecimalFormat("##.##").format(position.x) +
      "], Y: [" +
      new DecimalFormat("##.##").format(position.y) +
      "], Z: [" +
      new DecimalFormat("##.##").format(position.z) +
      "]";
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

  public void rotate(float yaw, float pitch) {
//    float oldCameraX = camera.direction.x;
//    float oldCameraZ = camera.direction.z;
//    camera.direction.rotate(camera.up, yaw);
//    localVector.set(camera.direction).crs(camera.up).nor();
//    camera.direction.rotate(localVector, pitch);
//
//    if (Math.signum(camera.direction.z) != Math.signum(oldCameraZ) && Math.signum(camera.direction.x) != Math.signum(oldCameraX)) {
//      camera.direction.rotate(localVector, -pitch);
//    }

    yaw = -yaw;
    pitch = -pitch;

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

  public boolean isOnGround() {
    return MathUtils.isZero(verticalVelocity) && checkCollision(localVector.set(0f, -0.1f, 0f));
  }

  public void update() {
    if (pressedKeys.contains(InputKeys.Jump) && isOnGround()) {
      verticalVelocity = 0.16f;
    }

    boolean moving = false;

    localVector2.setZero();

    if (pressedKeys.contains(InputKeys.Forward)) {
      localVector2.set(move(InputKeys.Forward));
      moving = true;
    } else if (pressedKeys.contains(InputKeys.Backward)) {
      localVector2.set(move(InputKeys.Backward));
      moving = true;
    }

    if (pressedKeys.contains(InputKeys.Left)) {
      localVector2.add(move(InputKeys.Left));
      moving = true;
    } else if (pressedKeys.contains(InputKeys.Right)) {
      localVector2.add(move(InputKeys.Right));
      moving = true;
    }

    if (! moving) {
      horizontalVelocity = 0;
    }

    localVector2.limit(WALK_SPEED);

    localVector.set(localVector2.x, 0f, 0f);

    if (! checkCollision(localVector)) {
      position.add(localVector);
    }

    localVector.set(0f, verticalVelocity, 0f);

    if (! checkCollision(localVector)) {
      position.add(localVector);
      verticalVelocity -= 0.01f;
    } else {
      verticalVelocity = 0f;
    }

    localVector.set(0f, 0f, localVector2.y);

    if (! checkCollision(localVector)) {
      position.add(localVector);
    }

    camera.position.set(position.x, position.y + CAMERA_HEIGHT, position.z);
  }

  public Vector2 move(InputKeys direction) {
    horizontalVelocity = Math.min(WALK_SPEED, horizontalVelocity + ACCELERATION);

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

    localVector2Helper.set(camera.direction.x, camera.direction.z);

    moveVector.rotate(localVector2Helper.angle());

    jumpDirection.set(moveVector);

    return moveVector;
  }

  private boolean checkCollision(Vector3 direction) {
    boundingBox.set(position.cpy().sub(SIZE, 0f, SIZE).add(direction), position.cpy().add(SIZE, HEIGHT, SIZE).add(direction));

    for (Chunk chunk : World.getChunksInRange(position, 1f)) {
      for (Block block : chunk.getVisibleBlocks()) {
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

    if (block != null && block.getBlockType() != BlockType.Bedrock) {
      World.getChunk(block.getPosition()).destroyBlock(block);
    }
  }

  public void setBlock(int screenX, int screenY) {
    Block block = getTargetBlock(screenX, screenY);

    if (block == null) {
      return;
    }

    Ray ray = camera.getPickRay(screenX, screenY - 4);

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
    return Physics.getTargetBlock(screenX, screenY, camera);
  }

  public void addKeyPressed(InputKeys key) {
    pressedKeys.add(key);
  }

  public void removeKeyPressed(InputKeys key) {
    pressedKeys.remove(key);
  }
}
