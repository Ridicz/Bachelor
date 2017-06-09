package com.bachelor.game.utils;

import com.bachelor.game.entities.Block;
import com.bachelor.game.entities.Player;
import com.bachelor.game.enums.Side;
import com.bachelor.game.world.Chunk;
import com.bachelor.game.world.World;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Physics {

  private static BoundingBox localBoundingBox = new BoundingBox();

  private static Vector3 localVector = new Vector3();
  private static Vector3 localVectorHelper = new Vector3();
  private static Vector3 pos = new Vector3();

  public static Side getSelectedSide(Block block, Ray ray) {
    Side selectedSide = Side.Front;

    float distance = Float.MAX_VALUE;

    localVector.set(block.getPosition().getPositionVector());

    localBoundingBox.set(localVector, localVector.cpy().add(0f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      distance = ray.origin.dst(localVector.cpy().add(0f, 0.5f, 0.5f));
      selectedSide = Side.Back;
    }

    localBoundingBox.set(localVector, localVector.cpy().add(1f, 1f, 0f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      if (ray.origin.dst(localVector.cpy().add(0.5f, 0.5f, 0f)) < distance) {
        distance = ray.origin.dst(localVector.cpy().add(0.5f, 0.5f, 0f));
        selectedSide = Side.Left;
      }
    }

    localBoundingBox.set(localVector, localVector.cpy().add(1f, 0f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      if (ray.origin.dst(localVector.cpy().add(0.5f, 0f, 0.5f)) < distance) {
        distance = ray.origin.dst(localVector.cpy().add(0.5f, 0f, 0.5f));
        selectedSide = Side.Bottom;
      }
    }

    localBoundingBox.set(localVector.cpy().add(0f, 1f, 0f), localVector.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      if (ray.origin.dst(localVector.cpy().add(0.5f, 1f, 0.5f)) < distance) {
        distance = ray.origin.dst(localVector.cpy().add(0.5f, 1f, 0.5f));
        selectedSide = Side.Top;
      }
    }

    localBoundingBox.set(localVector.cpy().add(0f, 0f, 1f), localVector.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      if (ray.origin.dst(localVector.cpy().add(0.5f, 0.5f, 1f)) < distance) {
        distance = ray.origin.dst(localVector.cpy().add(0.5f, 0.5f, 1f));
        selectedSide = Side.Right;
      }
    }

    localBoundingBox.set(localVector.cpy().add(1f, 0f, 0f), localVector.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, localBoundingBox)) {
      if (ray.origin.dst(localVector.cpy().add(1f, 0.5f, 0.5f)) < distance) {
        selectedSide = Side.Front;
      }
    }

    return selectedSide;
  }

  public static Block getTargetBlock(int screenX, int screenY, PerspectiveCamera camera) {
    Block result = null;
    float distance = -1;

    Ray ray = camera.getPickRay(screenX, screenY);
    pos.set(camera.position);

    float dst = Float.MAX_VALUE;
    float dist2 = ray.origin.dst2(pos);

    for (Chunk chunk : World.getChunksInRange(camera.position, Player.REACH_RANGE)) {
      for (Block block : chunk.getVisibleBlocks()) {

        if (distance >= 0f && dist2 > distance) {
          continue;
        }

        if (Intersector.intersectRayBoundsFast(ray, pos, localVector.set(1f, 1f, 1f))) {
          distance = dist2;

          localBoundingBox.set(localVector.set(block.getPosition().getX(), block.getPosition().getY(),
            block.getPosition().getZ()), localVectorHelper.set(block.getPosition().getX() + 1,
            block.getPosition().getY() + 1, block.getPosition().getZ() + 1));

          if (Intersector.intersectRayBounds(ray, localBoundingBox, localVector)) {
            float curDst = localVector.dst(camera.position);

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
}
