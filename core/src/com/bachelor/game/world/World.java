package com.bachelor.game.world;

import com.bachelor.game.graphics.BlockRenderer;
import com.bachelor.game.utils.IntegerPosition;
import com.bachelor.game.graphics.Renderer;
import com.bachelor.game.entities.Block;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

public class World {

  private static World instance;

  private List<Chunk> chunkList;

  private Map<IntegerPosition, Chunk> chunkMap;

  private static ModelBuilder modelBuilder;

  private static Vector3 localVector = new Vector3();

  private ModelInstance modelInstance;

  public World() {
    instance = this;
    modelBuilder = new ModelBuilder();
    chunkList = new ArrayList<>();
    BlockRenderer.prepareTextures();
    createTestMap();
    initWorld();
  }

  private void createTestMap() {
    chunkList = WorldGenerator.generateMap();
  }

  public void initWorld() {
    short chunkNumber = 1;

    for (Chunk chunk : chunkList) {
      chunk.initializeVisibleBlocks();
      Gdx.graphics.setTitle("Initializing world: " + chunkNumber++ + "/" + chunkList.size());
    }

    for (Chunk chunk : chunkList) {
      chunk.loadVisibleBlocks();
    }
  }

  public void renderWorld(ModelBatch modelBatch) {
    modelBuilder.begin();

    for (Chunk chunk : chunkList) {
      if (chunk.isVisible()) {
        for (Mesh mesh : chunk.getMeshes()) {
          modelBuilder.part("Part", mesh, GL30.GL_TRIANGLES, Renderer.getMaterial());
        }
      }
    }

    modelInstance = new ModelInstance(modelBuilder.end());

    modelBatch.render(modelInstance);
  }

  public static Chunk getChunk(Vector3 position) {
    float x = position.x;
    float z = position.z;

    IntegerPosition chunkPosition;
    IntegerPosition chunkEndPosition;

    for (Chunk chunk : instance.chunkList) {
      chunkPosition = chunk.getStartPosition();
      chunkEndPosition = chunk.getEndPosition();

      if (x >= chunkPosition.getX() && x <= chunkEndPosition.getX()) {
        if (z >= chunkPosition.getZ() && z <= chunkEndPosition.getZ()) {
          return chunk;
        }
      }
    }

    return null;
  }

  public static Chunk getChunk(IntegerPosition position) {
    localVector.set(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f);
    return getChunk(localVector);
  }

  public static List<Block> getBlocksInRange(Vector3 position, float range) {
    List<Block> blocks = new ArrayList<>();

    //TODO

    return blocks;
  }

  public static Set<Chunk> getChunksInRange(Vector3 position, float range) {
    Set<Chunk> chunks = new HashSet<>();

    chunks.add(getChunk(position));

    localVector.set(position.x + range, position.y, position.z);
    chunks.add(getChunk(localVector));
    localVector.set(position.x + range, position.y, position.z + range);
    chunks.add(getChunk(localVector));
    localVector.set(position.x, position.y, position.z + range);
    chunks.add(getChunk(localVector));
    localVector.set(position.x - range, position.y, position.z);
    chunks.add(getChunk(localVector));
    localVector.set(position.x - range, position.y, position.z - range);
    chunks.add(getChunk(localVector));
    localVector.set(position.x, position.y, position.z - range);
    chunks.add(getChunk(localVector));
    localVector.set(position.x + range, position.y, position.z - range);
    chunks.add(getChunk(localVector));
    localVector.set(position.x - range, position.y, position.z + range);
    chunks.add(getChunk(localVector));

    chunks.remove(null);

    return chunks;
  }
}
