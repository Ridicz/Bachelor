package com.bachelor.game.utils;

import com.bachelor.game.entities.Block;
import com.bachelor.game.enums.Side;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Physics {

  private static BoundingBox localBoundingBox = new BoundingBox();

  private static Vector3 localVector = new Vector3();

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


}
