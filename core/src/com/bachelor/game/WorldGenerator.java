package com.bachelor.game;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldGenerator {

  public static List<Chunk> generateMap() {
//    List<Chunk> world = new ArrayList<>();
//
//    world.add(loadFromJson().get(0));

//    int mapWidth = 3;
//    int mapLength = 3;
//
//    int baseHeight = 60;
//
//    for (int i = 0; i < mapWidth; i++) {
//      for (int j = 0; j < mapLength; j++) {
//        Chunk chunk = new Chunk(i, j);
//        generate(chunk);
//        world.add(chunk);
//
//        loadFromJson();
//      }
//    }

    return loadFromJson();
  }

  private static void generate(Chunk chunk) {
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int y = 0; y < 10; y++) {
        for (int z = 0; z < Chunk.LENGTH; z++) {
          if (y % 2 == 0) {
            if (x % 2 == 0) {
              if (z % 2 == 0) {
                chunk.setBlock(x, y, z, BlockType.Grass);
              }
            } else {
              if (z % 2 == 1) {
                chunk.setBlock(x, y, z, BlockType.Grass);
              }
            }
          } else {
            if (x % 2 == 1) {
              if (z % 2 == 0) {
                chunk.setBlock(x, y, z, BlockType.Grass);
              }
            } else {
              if (z % 2 == 1) {
                chunk.setBlock(x, y, z, BlockType.Grass);
              }
            }
          }
        }
      }
    }
  }

  private static List<Chunk> loadFromJson() {
    JsonReader reader;

    List<Chunk> world = new ArrayList<>();


    try {
      reader = new JsonReader(new FileReader("world.json"));

      reader.beginArray();

      Chunk chunk;

      int x;
      int y;
      int z;
      int type;

      for (int i = 0; i < 386; i++) {
        reader.beginArray();
        reader.beginObject();

        reader.nextName();
        int startX = Integer.parseInt(reader.nextString()) / 16 + 13;
        reader.nextName();
        int startZ = Integer.parseInt(reader.nextString()) / 16;
        reader.nextName();
        int startY = Integer.parseInt(reader.nextString());

        reader.nextName();
        int blocks = Integer.parseInt(reader.nextString());

        chunk = new Chunk(startX, startZ);

        System.out.println(startX + " " + startZ);

        reader.endObject();
        reader.beginArray();

        for (int j = 0; j < blocks; j++) {
          reader.beginObject();
          reader.nextName();
          x = Integer.parseInt(reader.nextString());
          reader.nextName();
          y = Integer.parseInt(reader.nextString());
          reader.nextName();
          z = Integer.parseInt(reader.nextString());

          reader.nextName();
          type = Integer.parseInt(reader.nextString());

          if (type == 0) {
            chunk.setBlock(x, y, z, BlockType.Cobblestone);
          } else if (type == 1) {
            chunk.setBlock(x, y, z, BlockType.Grass);
          } else if (type == 4) {
            chunk.setBlock(x, y, z, BlockType.Dirt);
          } else if (type == 7) {
            chunk.setBlock(x, y, z, BlockType.Bedrock);
          } else if (type == 8 || type == 9) {
            chunk.setBlock(x, y, z, BlockType.Water);
          } else if (type == 12) {
            chunk.setBlock(x, y, z, BlockType.Sand);
          } else if (type == 13) {
            chunk.setBlock(x, y, z, BlockType.Gravel);
          } else if (type == 14) {
            chunk.setBlock(x, y, z, BlockType.Gold);
          } else if (type == 15) {
            chunk.setBlock(x, y, z, BlockType.Iron);
          } else if (type == 16) {
            chunk.setBlock(x, y, z, BlockType.Coal);
          } else if (type == 17) {
            chunk.setBlock(x, y, z, BlockType.Wood);
          } else if (type == 18) {
            chunk.setBlock(x, y, z, BlockType.Leafs);
          } else {
            chunk.setBlock(x, y, z, BlockType.Checker);
          }

          reader.endObject();
        }

        reader.endArray();
        reader.endArray();

        world.add(chunk);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return world;
  }
}
