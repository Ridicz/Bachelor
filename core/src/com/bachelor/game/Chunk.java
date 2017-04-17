package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

  private IntegerPosition startPosition;

  private Block[][][] storage;

  private Set<Block> blocks;

  private Mesh mesh;

  public Chunk(int x, int y) {
    startPosition = new IntegerPosition(x * WIDTH, 0, y * LENGTH);
    storage = new Block[WIDTH][HEIGHT][LENGTH];
    blocks = new HashSet<>(1024);
  }

  public void initializeVisibleBlocks() {
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        for (int k = 0; k < LENGTH; k++) {
          if (storage[i][j][k] != null) {
            Block block = storage[i][j][k];
            addVisibleSides(i, j, k, block);

            if (block.getVisibleSides().size() > 0) {
              blocks.add(block);
            }
          }
        }
      }
    }

    rebuildModel();
  }

  private void addVisibleSides(int x, int y, int z, Block block) {
    if (x == 0) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).sub(new IntegerPosition(WIDTH, 0, 0)));

      if (chunk != null && chunk.getBlock(WIDTH - 1, y, z) == null) {
        block.addVisibleSide(Side.Back);
      }

      if (storage[x + 1][y][z] == null) {
        block.addVisibleSide(Side.Front);
      }
    } else if (x == WIDTH - 1) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).add(new IntegerPosition(WIDTH, 0, 0)));

      if (chunk != null && chunk.getBlock(0, y, z) == null) {
        block.addVisibleSide(Side.Front);
      }

      if (storage[x - 1][y][z] == null) {
        block.addVisibleSide(Side.Back);
      }
    } else {
      if (storage[x + 1][y][z] == null) {
        block.addVisibleSide(Side.Front);
      }

      if (storage[x - 1][y][z] == null) {
        block.addVisibleSide(Side.Back);
      }
    }

    if (y > 0 && y < HEIGHT - 1) {
      if (storage[x][y + 1][z] == null) {
        block.addVisibleSide(Side.Top);
      }

      if (storage[x][y - 1][z] == null) {
        block.addVisibleSide(Side.Bottom);
      }
    } else {
      block.addVisibleSide(Side.Top);
      block.addVisibleSide(Side.Bottom);
    }

    if (z == 0) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).sub(new IntegerPosition(0, 0, LENGTH)));

      if (chunk != null && chunk.getBlock(x, y, LENGTH - 1) == null) {
        block.addVisibleSide(Side.Right);
      }

      if (storage[x][y][z] == null) {
        block.addVisibleSide(Side.Left);
      }
    } else if (z == LENGTH - 1) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).add(new IntegerPosition(0, 0, LENGTH)));

      if (chunk != null && chunk.getBlock(x, y, 0) == null) {
        block.addVisibleSide(Side.Left);
      }

      if (storage[x][y][z - 1] == null) {
        block.addVisibleSide(Side.Right);
      }
    } else {
      if (storage[x][y][z - 1] == null) {
        block.addVisibleSide(Side.Right);
      }

      if (storage[x][y][z] == null) {
        block.addVisibleSide(Side.Left);
      }
    }
  }

  public IntegerPosition getStartPosition() {
    return startPosition;
  }

  public IntegerPosition getEndPosition() {
    int x = startPosition.getX() + WIDTH;
    int z = startPosition.getZ() + LENGTH;

    return new IntegerPosition(x, HEIGHT, z);
  }

  public Block getBlock(Vector3 position) {
    return getBlock((int) position.x, (int) position.y, (int) position.z);
  }

  public Block getBlock(int x, int y, int z) {
    int localX = Math.abs(x % WIDTH);
    int localZ = Math.abs(z % LENGTH);

    return storage[localX][y][localZ];
  }

  public Block getBlock(IntegerPosition position) {
    return storage[position.getX()][position.getY()][position.getZ()];
  }

  public Set<Block> getBlocks() {
    return blocks;
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    if (storage[x][y][z] != null) {
      return;
    }

    Block newBlock = new Block(new IntegerPosition(startPosition.getX() + x, y, startPosition.getZ() + z), type);

    storage[x][y][z] = newBlock;

//    blocks.add(newBlock);
  }

  public void destroyBlock(Block block) {
    IntegerPosition position = block.getPosition();

    Gdx.app.log("Chunk.destroyBlock", position.toString());

    int localX = position.getX() % WIDTH;
    int localY = position.getY();
    int localZ = position.getZ() % LENGTH;

    storage[localX][localY][localZ] = null;
    makeNeighbourBlocksVisible(localX, localY, localZ);
    System.out.println(block.getBlockId());
    System.out.println(blocks.remove(block));

    rebuildModel();
  }

  private void makeNeighbourBlocksVisible(int x, int y, int z) {
    Block block = null;

    if (x == 0) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).sub(new IntegerPosition(WIDTH, 0, 0)));

      if (chunk != null) {
        block = chunk.getBlock(WIDTH - 1, y, z);
      }

      if (block != null) {
        block.addVisibleSide(Side.Front);
        chunk.rebuildModel();
      }

      if (storage[x + 1][y][z] != null) {
        storage[x + 1][y][z].addVisibleSide(Side.Back);
      }
    } else if (x == WIDTH - 1) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).add(new IntegerPosition(WIDTH, 0, 0)));

      if (chunk != null) {
        block = chunk.getBlock(0, y, z);
      }

      if (block != null) {
        block.addVisibleSide(Side.Back);
        chunk.rebuildModel();
      }

      if (storage[x - 1][y][z] != null) {
        storage[x - 1][y][z].addVisibleSide(Side.Front);
      }
    } else {
      if (storage[x + 1][y][z] != null) {
        storage[x + 1][y][z].addVisibleSide(Side.Back);
      }

      if (storage[x - 1][y][z] != null) {
        storage[x - 1][y][z].addVisibleSide(Side.Front);
      }
    }

    if (storage[x][y + 1][z] != null) {
      storage[x][y + 1][z].addVisibleSide(Side.Bottom);
    }

    if (y != 0 && storage[x][y - 1][z] != null) {
      storage[x][y - 1][z].addVisibleSide(Side.Top);
    }

    block = null;

    if (z == 0) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).sub(new IntegerPosition(0, 0, LENGTH)));

      if (chunk != null) {
        block = chunk.getBlock(x, y, LENGTH - 1);
      }

      if (block != null) {
        block.addVisibleSide(Side.Left);
        chunk.rebuildModel();
      }

      if (storage[x][y][z + 1] != null) {
        storage[x][y][z + 1].addVisibleSide(Side.Right);
      }
    } else if (z == LENGTH - 1) {
      Chunk chunk = World.getChunk(startPosition.add(new IntegerPosition(x, y, z)).add(new IntegerPosition(0, 0, LENGTH)));

      if (chunk != null) {
        block = chunk.getBlock(x, y, 0);
      }

      if (block != null) {
        block.addVisibleSide(Side.Right);
        chunk.rebuildModel();
      }

      if (storage[x][y][z - 1] != null) {
        storage[x][y][z - 1].addVisibleSide(Side.Left);
      }
    } else {
      if (storage[x][y][z + 1] != null) {
        storage[x][y][z + 1].addVisibleSide(Side.Right);
      }

      if (storage[x][y][z - 1] != null) {
        storage[x][y][z - 1].addVisibleSide(Side.Left);
      }
    }
  }

  public boolean isVisible() {
    IntegerPosition endPosition = getEndPosition();

    BoundingBox boundingBox = new BoundingBox(new Vector3(startPosition.getX(), startPosition.getY(), startPosition.getZ()),
      new Vector3(endPosition.getX(), endPosition.getY(), endPosition.getZ()));

    return BachelorClient.getInstance().getCamera().frustum.boundsInFrustum(boundingBox);
  }

  public void rebuildModel() {
    MeshBuilder meshBuilder = new MeshBuilder();
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);
    meshBuilder.ensureCapacity(Short.MAX_VALUE, Short.MAX_VALUE);

    for (Block block : blocks) {
      BlockRenderer.renderBlockVisibleSides(block, meshBuilder);
    }

    mesh = meshBuilder.end();
  }

  public Mesh getMesh() {
    return mesh;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Chunk chunk = (Chunk) o;

    return startPosition.equals(chunk.startPosition);
  }

  @Override
  public int hashCode() {
    return startPosition.hashCode();
  }
}
