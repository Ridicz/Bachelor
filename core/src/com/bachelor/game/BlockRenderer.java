package com.bachelor.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.HashMap;
import java.util.Map;

public class BlockRenderer {

  private static final short SIZE = 1;

  private static MeshBuilder meshBuilder;

  private static ModelBuilder modelBuilder;

  private static Map<BlockType, UVHolder> textureMap = new HashMap<BlockType, UVHolder>();

  private static float x, y, z;

  public static void setMeshBuilder(MeshBuilder meshBuilder) {
    BlockRenderer.meshBuilder = meshBuilder;

    prepareTextures();
  }

  public static void setModelBuilder(ModelBuilder modelBuilder) {
    BlockRenderer.modelBuilder = modelBuilder;
  }

  private static void prepareTextures() {
    textureMap.put(BlockType.Gravel, new UVHolder(2, 0));
    textureMap.put(BlockType.Stone, new UVHolder(0, 1));
  }

  public static Mesh renderChunk(Chunk chunk) {
    meshBuilder.clear();
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);

    for (Block[][] blocks2D : chunk.getStorage()) {
      for (Block[] blocks : blocks2D) {
        for (Block block : blocks) {
          if (block != null) {
//            renderBlock(block1);

            x = block.getPosition().getX();
            y = block.getPosition().getY();
            z = block.getPosition().getZ();
          }
        }
      }
    }

    return meshBuilder.end();
  }

  private static void renderBlock(Block block) {
//    x = block.getPosition().getX();
//    y = block.getPosition().getY();
//    z = block.getPosition().getZ();
//
//    UVHolder uvHolder = textureMap.get(block.getBlockType());
//
//    meshBuilder.setUVRange(uvHolder.getU1(), uvHolder.getV1(), uvHolder.getU2(), uvHolder.getV2());
//    meshBuilder.setUVRange(0, 0, 0, 0);
//
//    meshBuilder.rect(x, y + SIZE, z, x + SIZE, y + SIZE, z, x + SIZE, y, z, x, y, z,0f, 0f, 0f);
//    meshBuilder.rect(x, y, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x, y + SIZE, z + SIZE, 0f, 0f, 0f);
//    meshBuilder.rect(x, y, z, x, y, z + SIZE, x, y + SIZE, z + SIZE, x, y + SIZE, z, 0f, 0f, 0f);
//    meshBuilder.rect(x + SIZE, y + SIZE, z, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y, z, 0f, 0f, 0f);
//    meshBuilder.rect(x, y + SIZE, z, x, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z, 0f, 0f, 0f);
//    meshBuilder.rect(x + SIZE, y, z, x + SIZE, y, z + SIZE, x, y, z + SIZE, x, y, z, 0f, 0f, 0f);
  }
}
