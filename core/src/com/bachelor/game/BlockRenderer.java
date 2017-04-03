package com.bachelor.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockRenderer {

  private static final short SIZE = 1;

  private static MeshBuilder meshBuilder;

  private static Map<BlockType, UVHolder> textureMap = new HashMap<BlockType, UVHolder>();

  private static float x, y, z;

  private UVHolder uvHolder;

  private List<Mesh> meshes;

  public static void setMeshBuilder(MeshBuilder meshBuilder) {
    BlockRenderer.meshBuilder = meshBuilder;

    prepareTextures();
  }

  private static void prepareTextures() {
    textureMap.put(BlockType.Gravel, new UVHolder(2, 0));
    textureMap.put(BlockType.Stone, new UVHolder(2, 0));
//    textureMap.put(BlockType.Stone, new UVHolder(13, 15));
//    textureMap.put(BlockType.Gravel, new UVHolder(13, 15));
  }

  public List<Mesh> renderChunk(List<Block> list) {
    if (meshes != null) {
      meshes.clear();
    }

    meshes = new LinkedList<Mesh>();

    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);

    for (Block block : list) {
      if (meshBuilder.getNumVertices() > Short.MAX_VALUE - 64) {
        meshes.add(meshBuilder.end());
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
      }

      renderBlock(block);
    }

    meshes.add(meshBuilder.end());

    list.clear();

    return meshes;
  }

  private void renderBlock(Block block) {
    x = block.getPosition().getX();
    y = block.getPosition().getY();
    z = block.getPosition().getZ();

    uvHolder = textureMap.get(block.getBlockType());

    meshBuilder.setUVRange(uvHolder.getU1(), uvHolder.getV1(), uvHolder.getU2(), uvHolder.getV2());

    meshBuilder.rect(x, y + SIZE, z, x + SIZE, y + SIZE, z, x + SIZE, y, z, x, y, z,0f, 0f, 0f);
    meshBuilder.rect(x, y, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x, y + SIZE, z + SIZE, 0f, 0f, 0f);
    meshBuilder.rect(x, y, z, x, y, z + SIZE, x, y + SIZE, z + SIZE, x, y + SIZE, z, 0f, 0f, 0f);
    meshBuilder.rect(x + SIZE, y + SIZE, z, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y, z, 0f, 0f, 0f);
    meshBuilder.rect(x, y + SIZE, z, x, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z, 0f, 0f, 0f);
    meshBuilder.rect(x + SIZE, y, z, x + SIZE, y, z + SIZE, x, y, z + SIZE, x, y, z, 0f, 0f, 0f);
  }

  private void renderTop() {
    meshBuilder.rect(x, y + SIZE, z, x, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y + SIZE, z, 0f, 0f, 0f);
  }

  private void renderBottom() {
    meshBuilder.rect(x + SIZE, y, z, x + SIZE, y, z + SIZE, x, y, z + SIZE, x, y, z, 0f, 0f, 0f);
  }

  private void renderNorth() {
    meshBuilder.rect(x + SIZE, y + SIZE, z, x + SIZE, y + SIZE, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y, z, 0f, 0f, 0f);
  }

  private void renderSouth() {
    meshBuilder.rect(x, y, z, x, y, z + SIZE, x, y + SIZE, z + SIZE, x, y + SIZE, z, 0f, 0f, 0f);
  }

  private void renderEast() {
    meshBuilder.rect(x, y, z + SIZE, x + SIZE, y, z + SIZE, x + SIZE, y + SIZE, z + SIZE, x, y + SIZE, z + SIZE, 0f, 0f, 0f);
  }

  private void renderWest() {
    meshBuilder.rect(x, y + SIZE, z, x + SIZE, y + SIZE, z, x + SIZE, y, z, x, y, z,0f, 0f, 0f);
  }
}
