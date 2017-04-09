package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.*;

public class Chunk {

  private static final short WIDTH = 16;

  private static final short HEIGHT = 256;

  private static final short LENGTH = 16;

  private Position startPosition;

  private Block[][][] storage;

  private Map<Position, Block> blocks;

  private Map<Block, Mesh> visibleBlocks;

  private Mesh mesh;

  public Chunk(float x, float y) {
    startPosition = new Position(x * WIDTH, 0, y * LENGTH);
    storage = new Block[WIDTH][HEIGHT][LENGTH];
    blocks = new HashMap<Position, Block>(4096);
    visibleBlocks = new HashMap<Block, Mesh>();
  }

  public void initializeVisibleBlocks() {
    for (int i = 1; i < WIDTH - 1; i++) {
      for (int j = 1; j < HEIGHT - 1; j++) {
        for (int k = 1; k < LENGTH - 1; k++) {
          if (storage[i][j][k] != null) {
            if (storage[i + 1][j][k] == null || storage[i - 1][j][k] == null || storage[i][j + 1][k] == null ||
                storage[i][j - 1][k] == null || storage[i][j][k + 1] == null || storage[i][j][k - 1] == null) {

              visibleBlocks.put(storage[i][j][k], BlockRenderer.renderBlock(storage[i][j][k]));
            }
          }
        }
      }
    }

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 256; j++) {
        if (storage[i][j][0] != null) {
          visibleBlocks.put(storage[i][j][0], BlockRenderer.renderBlock(storage[i][j][0]));
        }
        if (storage[i][j][15] != null) {
          visibleBlocks.put(storage[i][j][15], BlockRenderer.renderBlock(storage[i][j][15]));
        }
        if (storage[0][j][i] != null) {
          visibleBlocks.put(storage[0][j][i], BlockRenderer.renderBlock(storage[0][j][i]));
        }
        if (storage[15][j][i] != null) {
          visibleBlocks.put(storage[15][j][i], BlockRenderer.renderBlock(storage[15][j][i]));
        }
      }
    }

    rebuildModel();
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

  public List<Block> getBlocks() {
    return new LinkedList<Block>(blocks.values());
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    Block newBlock = new Block(new Position(startPosition.getX() + x, y, startPosition.getZ() + z), type);

    storage[x % WIDTH][y % HEIGHT][z % LENGTH] = newBlock;

    blocks.put(newBlock.getPosition(), newBlock);
  }

  public void destroyBlock(Block block) {
    Position position = block.getPosition();

    Gdx.app.log("Chunk.destroyBlock", position.getPosition().toString());

    int localX = Math.abs((int) position.getX() % WIDTH);
    int localY = (int) position.getY();
    int localZ = Math.abs((int) position.getZ() % LENGTH);

    storage[localX][localY][localZ] = null;
    visibleBlocks.remove(block);
    makeNeighbourBlocksVisible(localX, localY, localZ);

    rebuildModel();
  }

  private void makeNeighbourBlocksVisible(int x, int y, int z) {
    System.out.println(new Vector3(x, y, z));

    if (x != 15 && storage[x + 1][y][z] != null) {
      visibleBlocks.put(storage[x + 1][y][z], BlockRenderer.renderBlock(storage[x + 1][y][z]));
    }

    if (x != 0 && storage[x - 1][y][z] != null) {
      visibleBlocks.put(storage[x - 1][y][z], BlockRenderer.renderBlock(storage[x - 1][y][z]));
    }

    if (y != 15 && storage[x][y + 1][z] != null) {
      visibleBlocks.put(storage[x][y + 1][z], BlockRenderer.renderBlock(storage[x][y + 1][z]));
    }

    if (y != 0 && storage[x][y - 1][z] != null) {
      visibleBlocks.put(storage[x][y - 1][z], BlockRenderer.renderBlock(storage[x][y - 1][z]));
    }

    if (z != 15 && storage[x][y][z + 1] != null) {
      visibleBlocks.put(storage[x][y][z + 1], BlockRenderer.renderBlock(storage[x][y][z + 1]));
    }

    if (z != 0 && storage[x][y][z - 1] != null) {
      visibleBlocks.put(storage[x][y][z - 1], BlockRenderer.renderBlock(storage[x][y][z - 1]));
    }
  }

  public boolean isVisible() {
    BoundingBox boundingBox = new BoundingBox(getStartPosition().getPosition(), getEndPosition().getPosition());

    return BachelorClient.getInstance().getCamera().frustum.boundsInFrustum(boundingBox);
  }

  public void rebuildModel() {
    MeshBuilder meshBuilder = new MeshBuilder();
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);

    for (Mesh mesh1 : visibleBlocks.values()) {
      System.out.println(visibleBlocks.size());
//      if (meshBuilder.getNumVertices() < Short.MAX_VALUE)
        meshBuilder.addMesh(mesh1);
    }

    mesh = meshBuilder.end();
  }

  public Mesh getMesh() {
//    Gdx.app.log("Visible blocks", String.valueOf(visibleBlocks.size()));
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
