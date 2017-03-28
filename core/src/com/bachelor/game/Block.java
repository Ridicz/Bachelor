package com.bachelor.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Block extends Item {

  private static final short SIZE = 1;

  private BlockType blockType;

  private Position position;

  private MeshPartBuilder.VertexInfo corner1;
  private MeshPartBuilder.VertexInfo corner2;
  private MeshPartBuilder.VertexInfo corner3;
  private MeshPartBuilder.VertexInfo corner4;

  public Block(Position position, BlockType blockType) {
    super();
    this.position = position;
    this.blockType = blockType;
    corner1 = new MeshPartBuilder.VertexInfo();
    corner2 = new MeshPartBuilder.VertexInfo();
    corner3 = new MeshPartBuilder.VertexInfo();
    corner4 = new MeshPartBuilder.VertexInfo();
  }

  public Position getPosition() {
    return position;
  }

  public void render(MeshBuilder meshBuilder) {
    renderRectangle(position.getX(), position.getY(), position.getZ(), BlockType.Gravel, meshBuilder);
  }

  private void renderRectangle(float x, float y, float z, BlockType type, MeshBuilder meshBuilder) {
//    meshBuilder.setUVRange(1, 0, 1, 0);
//
//    corner1.setPos(x, y, z);
//    corner2.setPos(x + SIZE, y, z);
//    corner3.setPos(x + SIZE, y + SIZE, z);
//    corner4.setPos(x, y + SIZE, z);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner4, corner3, corner2, corner1);
//
//    corner1.setPos(x, y, z + SIZE);
//    corner2.setPos(x + SIZE, y, z + SIZE);
//    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
//    corner4.setPos(x, y + SIZE, z + SIZE);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner1, corner2, corner3, corner4);
//
//    corner1.setPos(x, y, z);
//    corner2.setPos(x, y, z + SIZE);
//    corner3.setPos(x, y + SIZE, z + SIZE);
//    corner4.setPos(x, y + SIZE, z);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner1, corner2, corner3, corner4);
//
//    corner1.setPos(x + SIZE, y, z);
//    corner2.setPos(x + SIZE, y, z + SIZE);
//    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
//    corner4.setPos(x + SIZE, y + SIZE, z);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner4, corner3, corner2, corner1);
//
//    corner1.setPos(x, y, z);
//    corner2.setPos(x, y, z + SIZE);
//    corner3.setPos(x + SIZE, y, z + SIZE);
//    corner4.setPos(x + SIZE, y, z);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner4, corner3, corner2, corner1);
//
//    corner1.setPos(x, y + SIZE, z);
//    corner2.setPos(x, y + SIZE, z + SIZE);
//    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
//    corner4.setPos(x + SIZE, y + SIZE, z);
//    meshBuilder.setColor(Color.BROWN);
//    meshBuilder.rect(corner1, corner2, corner3, corner4);
  }
}
