package com.bachelor.game.graphics;

import com.bachelor.game.utils.UVHolder;
import com.bachelor.game.entities.Block;
import com.bachelor.game.enums.BlockType;
import com.bachelor.game.enums.Side;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import java.util.HashMap;
import java.util.Map;

public class BlockRenderer {

  private static final short SIZE = 1;

  private static Map<BlockType, UVHolder> textureMap;

  public static void prepareTextures() {
    textureMap = new HashMap<>();
    textureMap.put(BlockType.Dirt, new UVHolder(2, 0));
    textureMap.put(BlockType.Bedrock, new UVHolder(1, 1));
    textureMap.put(BlockType.Grass, new UVHolder(2, 9));
    textureMap.put(BlockType.Cobblestone, new UVHolder(0, 1));
    textureMap.put(BlockType.Coal, new UVHolder(2, 2));
    textureMap.put(BlockType.Wood, new UVHolder(4, 1));
    textureMap.put(BlockType.Leafs, new UVHolder(5, 4));
    textureMap.put(BlockType.Gravel, new UVHolder(3, 1));
    textureMap.put(BlockType.Water, new UVHolder(15, 13));
    textureMap.put(BlockType.Sand, new UVHolder(2, 1));
    textureMap.put(BlockType.Gold, new UVHolder(0, 2));
    textureMap.put(BlockType.Iron, new UVHolder(1, 2));
    textureMap.put(BlockType.Checker, new UVHolder(13, 15));
  }

  public static void renderBlockVisibleSides(Block block, MeshBuilder meshBuilder) {
    float x = block.getPosition().getX();
    float y = block.getPosition().getY();
    float z = block.getPosition().getZ();

    UVHolder uvHolder = textureMap.get(block.getBlockType());
    meshBuilder.setUVRange(uvHolder.getU1(), uvHolder.getV1(), uvHolder.getU2(), uvHolder.getV2());

    for (Side side : block.getVisibleSides()) {
      switch (side) {
        case Right:
          meshBuilder.rect(x, y, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x, y + SIZE, z + SIZE, 0f, 0f, 0f);
          break;

        case Left:
          meshBuilder.rect(x, y + SIZE, z, x + SIZE, y + SIZE, z, x + SIZE, y, z, x, y, z,0f, 0f, 0f);
          break;

        case Top:
          meshBuilder.rect(x, y + SIZE, z, x, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z, 0f, 0f, 0f);
          break;

        case Bottom:
          meshBuilder.rect(x + SIZE, y, z, x + SIZE, y, z + SIZE, x, y, z + SIZE, x, y, z, 0f, 0f, 0f);
          break;

        case Front:
          meshBuilder.rect(x + SIZE, y + SIZE, z, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y, z, 0f, 0f, 0f);
          break;

        case Back:
          meshBuilder.rect(x, y, z, x, y, z + SIZE, x, y + SIZE, z + SIZE, x, y + SIZE, z, 0f, 0f, 0f);
          break;
      }
    }
  }
}
