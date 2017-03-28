package com.bachelor.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.HashMap;
import java.util.Map;

public class BlockRenderer {

  private static final short SIZE = 1;

  private static MeshBuilder meshBuilder;

  private static ModelBuilder modelBuilder;

  private static MeshPartBuilder.VertexInfo corner1;
  private static MeshPartBuilder.VertexInfo corner2;
  private static MeshPartBuilder.VertexInfo corner3;
  private static MeshPartBuilder.VertexInfo corner4;

  private static Map<String, Texture> textureMap = new HashMap<String, Texture>();

  public static void setMeshBuilder(MeshBuilder meshBuilder) {
    BlockRenderer.meshBuilder = meshBuilder;
  }

  public static void setModelBuilder(ModelBuilder modelBuilder) {
    BlockRenderer.modelBuilder = modelBuilder;
  }

  private static void prepareTextures() {
    textureMap.put("Gravel", new Texture("gravel.jpg"));
    textureMap.put("rock", new Texture("rock.jpg"));
  }

  public static Model renderChunk(Chunk chunk) {
    modelBuilder.begin();

    for (Block[][] blocks : chunk.getStorage()) {
      for (Block[] block : blocks) {
        for (Block block1 : block) {
          renderBlock(block1);
          modelBuilder.part("Box", meshBuilder.end(), GL20.GL_TRIANGLES, new Material(TextureAttribute.createDiffuse(textureMap.get("Gravel"))));
        }
      }
    }

    return modelBuilder.end();
  }

  public static void renderBlock(Block block) {
    meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal, GL20.GL_TRIANGLES);

    float x = block.getPosition().getX();
    float y = block.getPosition().getY();
    float z = block.getPosition().getZ();

    corner1.setPos(x, y, z);
    corner2.setPos(x + SIZE, y, z);
    corner3.setPos(x + SIZE, y + SIZE, z);
    corner4.setPos(x, y + SIZE, z);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner4, corner3, corner2, corner1);

    corner1.setPos(x, y, z + SIZE);
    corner2.setPos(x + SIZE, y, z + SIZE);
    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
    corner4.setPos(x, y + SIZE, z + SIZE);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner1, corner2, corner3, corner4);

    corner1.setPos(x, y, z);
    corner2.setPos(x, y, z + SIZE);
    corner3.setPos(x, y + SIZE, z + SIZE);
    corner4.setPos(x, y + SIZE, z);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner1, corner2, corner3, corner4);

    corner1.setPos(x + SIZE, y, z);
    corner2.setPos(x + SIZE, y, z + SIZE);
    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
    corner4.setPos(x + SIZE, y + SIZE, z);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner4, corner3, corner2, corner1);

    corner1.setPos(x, y, z);
    corner2.setPos(x, y, z + SIZE);
    corner3.setPos(x + SIZE, y, z + SIZE);
    corner4.setPos(x + SIZE, y, z);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner4, corner3, corner2, corner1);

    corner1.setPos(x, y + SIZE, z);
    corner2.setPos(x, y + SIZE, z + SIZE);
    corner3.setPos(x + SIZE, y + SIZE, z + SIZE);
    corner4.setPos(x + SIZE, y + SIZE, z);
    meshBuilder.setColor(Color.BROWN);
    meshBuilder.rect(corner1, corner2, corner3, corner4);
  }
}
