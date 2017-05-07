package com.bachelor.game;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chunk {

  public static final short WIDTH = 16;

  public static final short HEIGHT = 256;

  public static final short LENGTH = 16;

  private IntegerPosition startPosition;

  private IntegerPosition endPosition;

  private Block[][][] storage;

  private Set<Block> blocks;

  private List<Mesh> meshes;

  private boolean rebuildModel = true;

  private BoundingBox boundingBox;

  private ChunkAndBlockPosition localChunkAndBlockPosition = new ChunkAndBlockPosition();

  public Chunk(int x, int y) {
    startPosition = new IntegerPosition(x * WIDTH, 0, y * LENGTH);
    storage = new Block[WIDTH][HEIGHT][LENGTH];
    blocks = new HashSet<>(1024);
    meshes = new ArrayList<>();
    endPosition = startPosition.add(WIDTH, HEIGHT, LENGTH);
    boundingBox = new BoundingBox(startPosition.getPositionVector(), endPosition.getPositionVector());
  }

  public void initializeVisibleBlocks() {
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        for (int k = 0; k < LENGTH; k++) {
          if (storage[i][j][k] != null) {
            addVisibleSides(storage[i][j][k]);
          }
        }
      }
    }
  }

  public void loadVisibleBlocks() {
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        for (int k = 0; k < LENGTH; k++) {
          if (storage[i][j][k] != null) {
            Block block = storage[i][j][k];

            if (block.getVisibleSides().size() > 0) {
              blocks.add(block);
            }
          }
        }
      }
    }
  }

  private void addVisibleSides(Block block) {
    IntegerPosition position = block.getPosition();

    getNeighbour(position, Side.Front);
    Block neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Front);
    }

    getNeighbour(position, Side.Back);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Back);
    }

    getNeighbour(position, Side.Left);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Left);
    }

    getNeighbour(position, Side.Right);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Right);
    }

    getNeighbour(position, Side.Top);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Top);
    }

    getNeighbour(position, Side.Bottom);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour == null) {
      block.addVisibleSide(Side.Bottom);
    }
  }

  public IntegerPosition getStartPosition() {
    return startPosition;
  }

  public IntegerPosition getEndPosition() {
    return endPosition;
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

  public Block setBlock(int x, int y, int z, BlockType type) {
    if (storage[x][y][z] != null) {
      return null;
    }

    Block newBlock = new Block(new IntegerPosition(startPosition.getX() + x, y, startPosition.getZ() + z), type);

    storage[x][y][z] = newBlock;

    return newBlock;
  }

  public void setBlockPlayer(Block selectedBlock, Side side, BlockType type) {
    getNeighbour(selectedBlock.getPosition(), side);

    Block block = localChunkAndBlockPosition.getChunk().setBlock(localChunkAndBlockPosition.getPosition().getX() % WIDTH, localChunkAndBlockPosition.getPosition().getY(), localChunkAndBlockPosition.getPosition().getZ() % LENGTH, type);

    if (block == null) {
      return;
    }

    block.addVisibleSide(Side.Front);
    block.addVisibleSide(Side.Back);
    block.addVisibleSide(Side.Top);
    block.addVisibleSide(Side.Bottom);
    block.addVisibleSide(Side.Left);
    block.addVisibleSide(Side.Right);

    localChunkAndBlockPosition.getChunk().getBlocks().add(block);
    localChunkAndBlockPosition.getChunk().setRebuildModel();
  }

  public void destroyBlock(Block block) {
    IntegerPosition position = block.getPosition();

    int localX = position.getX() % WIDTH;
    int localY = position.getY();
    int localZ = position.getZ() % LENGTH;

    storage[localX][localY][localZ] = null;
    makeNeighbourBlocksVisible(position);
    blocks.remove(block);

    rebuildModel = true;
  }

  private void makeNeighbourBlocksVisible(IntegerPosition position) {
    getNeighbour(position, Side.Front);
    Block neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Back);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }

    getNeighbour(position, Side.Back);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Front);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }

    getNeighbour(position, Side.Left);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Right);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }

    getNeighbour(position, Side.Right);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Left);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }

    getNeighbour(position, Side.Top);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Bottom);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }

    getNeighbour(position, Side.Bottom);
    neighbour = localChunkAndBlockPosition.getBlock();

    if (neighbour != null) {
      neighbour.addVisibleSide(Side.Top);
      localChunkAndBlockPosition.getChunk().addVisibleBlock(neighbour.getPosition());
    }
  }

  public void addVisibleBlock(IntegerPosition position) {
    int x = position.getX() % WIDTH;
    int z = position.getZ() % LENGTH;

    blocks.add(storage[x][position.getY()][z]);
    rebuildModel = true;
  }

  public boolean isVisible() {
    return BachelorClient.getInstance().getCamera().frustum.boundsInFrustum(boundingBox);
  }

  public void setRebuildModel() {
    rebuildModel = true;
  }

  private void rebuildModel() {
    if (! meshes.isEmpty()) {
      meshes.clear();
    }

    MeshBuilder meshBuilder = new MeshBuilder();
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL30.GL_TRIANGLES);
    meshBuilder.ensureCapacity(Short.MAX_VALUE, Short.MAX_VALUE);

    for (Block block : blocks) {
      if (meshBuilder.getNumVertices() > Short.MAX_VALUE - 64) {
        meshes.add(meshBuilder.end());
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.Normal, GL30.GL_TRIANGLES);
        meshBuilder.ensureCapacity(Short.MAX_VALUE, Short.MAX_VALUE);
      }

      BlockRenderer.renderBlockVisibleSides(block, meshBuilder);
    }

    meshes.add(meshBuilder.end());
  }

  public List<Mesh> getMeshes() {
    if (rebuildModel) {
      rebuildModel();
      rebuildModel = false;
    }

    return meshes;
  }

  private void getNeighbour(IntegerPosition position, Side side) {
    IntegerPosition localIntegerPosition;

    switch (side) {
      case Right:
        if ((position.getZ() + 1) % LENGTH != 0) {
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY(), position.getZ() + 1);
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        } else {
          Chunk chunk = World.getChunk(position.add(0, 0, LENGTH));
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY(), position.getZ() + 1);
          localChunkAndBlockPosition.set(chunk, localIntegerPosition);
        }

        break;

      case Left:
        if (position.getZ() % LENGTH != 0) {
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY(), position.getZ() - 1);
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        } else {
          Chunk chunk = World.getChunk(position.sub(0, 0, LENGTH));
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY(), position.getZ() - 1);
          localChunkAndBlockPosition.set(chunk, localIntegerPosition);
        }

        break;

      case Top:
        if (position.getY() != HEIGHT - 1) {
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY() + 1, position.getZ());
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        }

        break;

      case Bottom:
        if (position.getY() != 0) {
          localIntegerPosition = new IntegerPosition(position.getX(), position.getY() - 1, position.getZ());
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        }

        break;

      case Front:
        if ((position.getX() + 1) % WIDTH != 0) {
          localIntegerPosition = new IntegerPosition(position.getX() + 1, position.getY(), position.getZ());
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        } else {
          Chunk chunk = World.getChunk(position.add(WIDTH, 0, 0));
          localIntegerPosition = new IntegerPosition(position.getX() + 1, position.getY(), position.getZ());
          localChunkAndBlockPosition.set(chunk, localIntegerPosition);
        }

        break;

      case Back:
        if (position.getX() % WIDTH != 0) {
          localIntegerPosition = new IntegerPosition(position.getX() - 1, position.getY(), position.getZ());
          localChunkAndBlockPosition.set(this, localIntegerPosition);
        } else {
          Chunk chunk = World.getChunk(position.sub(WIDTH, 0, 0));
          localIntegerPosition = new IntegerPosition(position.getX() - 1, position.getY(), position.getZ());
          localChunkAndBlockPosition.set(chunk, localIntegerPosition);
        }

        break;
    }
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
