package com.bachelor.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class World {

  private static final int CHUNK_SIZE = 16;

  private static final int WORLD_HEIGHT = 256;

  private static final int SEA_LEVEL = 64;

  private static final float GRAVITY = 32f;

  private static World instance;

  private List<Chunk> chunkList;

  private List<Mesh> meshList;

  private static ModelBuilder modelBuilder;

  private static MeshBuilder meshBuilder;

  private Material material;

  private World() {
    chunkList = new ArrayList<Chunk>();
    meshList = new ArrayList<Mesh>();
    createTestMap();
    material = new Material(TextureAttribute.createDiffuse(new Texture("atlas.jpg")));
  }

  public static World getInstance() {
    if (instance == null) {
      instance = new World();
      return instance;
    }

    return instance;
  }

  public static void setModelBuilder(ModelBuilder modelBuilder) {
    World.modelBuilder = modelBuilder;
  }

  public static void setMeshBuilder(MeshBuilder meshBuilder) {
    World.meshBuilder = meshBuilder;
  }

  private void createTestMap() {
    int counter = 0;

    for (int i = -4; i < 16; i++) {
      for (int j = -4; j < 16; j++) {
        chunkList.add(new Chunk(i, j));
      }
    }

    for (int i = 0; i < 16; i++) {
      for (Chunk chunk : chunkList) {
        chunk.setBlock(0, i, 15, BlockType.Gravel);
        chunk.setBlock(15, i, 0, BlockType.Stone);
        chunk.setBlock(0, i, 0, BlockType.Gravel);
        chunk.setBlock(15, i, 15, BlockType.Stone);

        chunk.setBlock(0, 15, i, BlockType.Gravel);
        chunk.setBlock(i, 15, 0, BlockType.Stone);
        chunk.setBlock(i, 15, 15, BlockType.Gravel);
        chunk.setBlock(15, 15, i, BlockType.Stone);

        counter += 8;
      }
    }

    System.out.println(counter);
  }

  public Model render() {
    for (Mesh mesh : meshList) {
      mesh.dispose();
    }

    meshBuilder.clear();
    meshList.clear();

    for (Chunk chunk : chunkList) {
      meshList.add(BlockRenderer.renderChunk(chunk));
    }

    modelBuilder.begin();

    for (Mesh mesh : meshList) {
      modelBuilder.part("Chunk", mesh, GL20.GL_TRIANGLES, material);
    }

//    modelBuilder.part("Chunk", meshBuilder.end(), GL20.GL_TRIANGLES, material);

    return modelBuilder.end();
  }

  public static Chunk getChunk(int x, int y) {
    return null;
  }
}
