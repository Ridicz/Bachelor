package com.bachelor.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

public class World {

  private static World instance;

  private List<Chunk> chunkList;

  private Map<IntegerPosition, Chunk> chunkMap;

  private static ModelBuilder modelBuilder;

  private ModelInstance skydome;

  public World() {
    instance = this;
    modelBuilder = new ModelBuilder();
    chunkList = new ArrayList<>();
    AssetManager assetManager = new AssetManager();
    assetManager.load("skydome.g3db", Model.class);
    assetManager.finishLoading();
    skydome = new ModelInstance(assetManager.get("skydome.g3db", Model.class));
    BlockRenderer.prepareTextures();
    createTestMap();
  }

  private void createTestMap() {
//    chunkList.add(new Chunk(0, 0));
//    chunkList.add(new Chunk(0, 1));
//    chunkList.add(new Chunk(1, 0));
//    chunkList.add(new Chunk(1, 1));
//    chunkList.add(new Chunk(1, 2));
//    chunkList.add(new Chunk(0, 2));
//    chunkList.add(new Chunk(2, 0));
//    chunkList.add(new Chunk(2, 1));
//    chunkList.add(new Chunk(2, 2));

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        chunkList.add(new Chunk(i, j));
      }
    }

    Random random = new Random();

    for (Chunk chunk : chunkList) {
      int top = random.nextInt(4) * 10;

      for (int k = 0; k < 40; k++) {
        for (int i = 0; i < 16; i++) {
          for (int j = 0; j < 16; j++) {
            chunk.setBlock(i, k, j, BlockType.Stone);
          }
        }
      }
    }


//    for (int i = 0; i < 16; i++) {
//      for (int j = 0; j < 16; j++) {
//
//        chunkList.get(1).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(2).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(4).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(6).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(7).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(8).setBlock(i, 1, j, BlockType.Stone);
//        chunkList.get(0).setBlock(i, 1, j, BlockType.Stone);
//
//        for (int k = 0; k < 100; k++) {
//          chunkList.get(3).setBlock(i, k, j, BlockType.Gravel);
//          chunkList.get(5).setBlock(i, k, j, BlockType.Stone);
//        }
//      }
//    }
//------------------------------------------------------------------
//
//    for (int i = -5; i < 5; i++) {
//      for (int j = -5; j < 5; j++) {
//        chunkList.add(new Chunk(i, j));
//      }
//    }
//
//    int counter = 0;
//
//    for (int l = -5; l < 5; l++) {
//      for (int m = -5; m < 5; m++) {
//        for (int i = 0; i < 16; i++) {
//          for (int j = 0; j < 16; j++) {
//            for (int k = 0; k < 120 + l * 10; k++) {
//              chunkList.get(counter).setBlock(i, k, j, BlockType.Stone);
//            }
//          }
//        }
//
//        ++counter;
//      }
//    }

//------------------------------------------------
//    for (int i = 0; i < 5; i++) {
//      for (int j = 0; j < 5; j++) {
//        chunkList.add(new Chunk(i, j));
//      }
//    }
//
//    int counter = 0;
//
//    for (Chunk chunk : chunkList) {
//      for (int i = 0; i < 16; i++) {
//        for (int j = 0; j < i; j++) {
//          for (int k = 0; k < 16; k++) {
//            chunk.setBlock(i, j, k, BlockType.Stone);
//          }
//        }
//      }
//
//      Gdx.app.log("World", "Created " + ++counter + "/" + chunkList.size() + " chunk.");
//    }
//--------------------------------------------------
//    chunkList.add(new Chunk(0, 0));
//
//    for (int i = 0; i < 5; i++) {
//      for (int j = 0; j < 5; j++) {
//        chunkList.get(0).setBlock(i, 1, j, BlockType.Stone);
//      }
//    }

    short initialized = 0;

    for (Chunk chunk : chunkList) {
      chunk.initializeVisibleBlocks();
      System.out.println("Initialized " + ++initialized + "/" + chunkList.size() + " chunk.");
    }
  }

  public void renderWorld(ModelBatch modelBatch, Environment environment) {
    modelBuilder.begin();

    for (Chunk chunk : chunkList) {
      if (chunk.isVisible()) {
        modelBuilder.part("XXX", chunk.getMesh(), GL30.GL_TRIANGLES, Renderer.getMaterial());
      }
    }

    modelBatch.render(new ModelInstance(modelBuilder.end()));
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
    return getChunk(new Vector3(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f));
  }

  public static List<Block> getBlocksInRange(Vector3 position, float range) {
    List<Block> blocks = new ArrayList<>();

    //TODO

    return blocks;
  }

  public static Set<Chunk> getChunksInRange(Vector3 position, float range) {
    Set<Chunk> chunks = new HashSet<>();

    chunks.add(getChunk(position));

    Vector3 targetPosition = new Vector3();

    targetPosition.set(position.x + range, position.y, position.z);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x + range, position.y, position.z + range);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x, position.y, position.z + range);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x - range, position.y, position.z);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x - range, position.y, position.z - range);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x, position.y, position.z - range);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x + range, position.y, position.z - range);
    chunks.add(getChunk(targetPosition));
    targetPosition.set(position.x - range, position.y, position.z + range);
    chunks.add(getChunk(targetPosition));

    return chunks;
  }

  public ModelInstance getSkydome() {
    return skydome;
  }
}
