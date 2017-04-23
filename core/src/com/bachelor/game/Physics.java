package com.bachelor.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Physics {

  public static Side getSelectedSide(Block block, Ray ray) {
    Side selectedSide = Side.Front;

    float distance = Float.MAX_VALUE;

    Vector3 blockPos = new Vector3(block.getPosition().getPositionVector());

    BoundingBox sideBoundingBox = new BoundingBox(blockPos, blockPos.cpy().add(0f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      distance = ray.origin.dst(blockPos.cpy().add(0f, 0.5f, 0.5f));
      selectedSide = Side.Front;
    }

    sideBoundingBox = new BoundingBox(blockPos, blockPos.cpy().add(1f, 1f, 0f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      if (ray.origin.dst(blockPos.cpy().add(0.5f, 0.5f, 0f)) < distance) {
        distance = ray.origin.dst(blockPos.cpy().add(0.5f, 0.5f, 0f));
        selectedSide = Side.Left;
      }
    }

    sideBoundingBox = new BoundingBox(blockPos, blockPos.cpy().add(1f, 0f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      if (ray.origin.dst(blockPos.cpy().add(0.5f, 0f, 0.5f)) < distance) {
        distance = ray.origin.dst(blockPos.cpy().add(0.5f, 0f, 0.5f));
        selectedSide = Side.Bottom;
      }
    }

    sideBoundingBox = new BoundingBox(blockPos.cpy().add(0f, 1f, 0f), blockPos.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      if (ray.origin.dst(blockPos.cpy().add(0.5f, 1f, 0.5f)) < distance) {
        distance = ray.origin.dst(blockPos.cpy().add(0.5f, 1f, 0.5f));
        selectedSide = Side.Top;
      }
    }

    sideBoundingBox = new BoundingBox(blockPos.cpy().add(0f, 0f, 1f), blockPos.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      if (ray.origin.dst(blockPos.cpy().add(0.5f, 0.5f, 1f)) < distance) {
        distance = ray.origin.dst(blockPos.cpy().add(0.5f, 0.5f, 1f));
        selectedSide = Side.Right;
      }
    }

    sideBoundingBox = new BoundingBox(blockPos.cpy().add(1f, 0f, 0f), blockPos.cpy().add(1f, 1f, 1f));

    if (Intersector.intersectRayBoundsFast(ray, sideBoundingBox)) {
      if (ray.origin.dst(blockPos.cpy().add(1f, 0.5f, 0.5f)) < distance) {
        selectedSide = Side.Back;
      }
    }

    return selectedSide;
  }


}
