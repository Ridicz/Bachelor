package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

  private Position startPosition;

  private Block[][][] storage;

  private Set<Block> blocks;

  private Mesh mesh;

  public Chunk(float x, float y) {
    startPosition = new Position(x * WIDTH, 0f, y * LENGTH);
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
          }
        }
      }
    }

    rebuildModel();
  }

  private void addVisibleSides(int x, int y, int z, Block block) {
    if (x == 0) {
      Chunk chunk = World.getChunk(new Position(x - WIDTH, y, z));

      if (chunk != null && chunk.getBlock(WIDTH - 1, y, z) == null) {
        block.addVisibleSide(Side.Back);
      }

      if (storage[x + 1][y][z] == null) {
        block.addVisibleSide(Side.Front);
      }
    } else if (x == WIDTH - 1) {
      Chunk chunk = World.getChunk(new Position(x + WIDTH, y, z));

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
      Chunk chunk = World.getChunk(new Position(x, y, z - LENGTH));

      if (chunk != null && chunk.getBlock(x, y, LENGTH - 1) == null) {
        block.addVisibleSide(Side.Right);
      }

      if (storage[x][y][z] == null) {
        block.addVisibleSide(Side.Left);
      }
    } else if (z == LENGTH - 1) {
      Chunk chunk = World.getChunk(new Position(x, y, z + LENGTH));

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

  public Position getStartPosition() {
    return startPosition;
  }

  public Position getEndPosition() {
    float x = startPosition.getX() + WIDTH;
    float y = 0;
    float z = startPosition.getZ() + WIDTH;

    return new Position(x, y, z);
  }

  public Block getBlock(float x, float y, float z) {
    int localX = Math.abs((int) x % WIDTH);
    int localY = (int) y;
    int localZ = Math.abs((int) z % LENGTH);

    return storage[localX][localY][localZ];
  }

  public Block getBlock(Position position) {
    return getBlock(position.getX(), position.getY(), position.getZ());
  }

  public Set<Block> getBlocks() {
    return blocks;
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    if (storage[x][y][z] != null) {
      return;
    }

    Block newBlock = new Block(new Position(startPosition.getX() + x, y, startPosition.getZ() + z), type);

    storage[x][y][z] = newBlock;

    blocks.add(newBlock);
  }

  public void destroyBlock(Block block) {
    Position position = block.getPosition();

    Gdx.app.log("Chunk.destroyBlock", position.getPosition().toString());

    int localX = (int) position.getX() % WIDTH;
    int localY = (int) position.getY();
    int localZ = (int) position.getZ() % LENGTH;

    storage[localX][localY][localZ] = null;
    makeNeighbourBlocksVisible(localX, localY, localZ);
    System.out.println(block.getBlockId());
    System.out.println(blocks.remove(block));

    rebuildModel();
  }

  private void makeNeighbourBlocksVisible(int x, int y, int z) {
    if (x != 15 && storage[x + 1][y][z] != null) {
      storage[x + 1][y][z].addVisibleSide(Side.Back);
    }

    if (x != 0 && storage[x - 1][y][z] != null) {
      storage[x - 1][y][z].addVisibleSide(Side.Front);
    }

    if (y != 15 && storage[x][y + 1][z] != null) {
      storage[x][y + 1][z].addVisibleSide(Side.Bottom);
    }

    if (y != 0 && storage[x][y - 1][z] != null) {
      storage[x][y - 1][z].addVisibleSide(Side.Top);
    }

    if (z != 15 && storage[x][y][z + 1] != null) {
      storage[x][y][z + 1].addVisibleSide(Side.Right);
    }

    if (z != 0 && storage[x][y][z - 1] != null) {
      storage[x][y][z - 1].addVisibleSide(Side.Left);
    }
  }

  public boolean isVisible() {
    BoundingBox boundingBox = new BoundingBox(getStartPosition().getPosition(), getEndPosition().getPosition());

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
