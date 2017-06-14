package com.bachelor.game.world;

import com.bachelor.game.enums.BlockType;
import com.badlogic.gdx.Gdx;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldGenerator {

  public static List<Chunk> generateMap() {
    return loadFromJson();
  }

  private static List<Chunk> generate() {
    List<Chunk> world = new ArrayList<>();

    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        Chunk chunk = new Chunk(i, j);
        world.add(chunk);

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
    }

    return world;
  }

  private static List<Chunk> loadFromJson() {
    JsonReader reader;

    List<Chunk> world = new ArrayList<>();

    final int bytesInSize = 10240;

    try {
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream("world.zip"));
      zipInputStream.getNextEntry();
      byte[] bytesIn = new byte[bytesInSize];
      int read;

      List<Byte> worldBytes = new ArrayList<>(300000000);

      while ((read = zipInputStream.read(bytesIn)) != -1) {
        for (int i = 0; i < read; i++) {
          worldBytes.add(bytesIn[i]);
        }
      }

      byte[] array = new byte[worldBytes.size()];

      for (int i = 0; i < worldBytes.size(); i++) {
        array[i] = worldBytes.get(i);
      }

      reader = new JsonReader(new StringReader(new String(array)));
//      reader = new JsonReader(new FileReader("world.json"));

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

          switch (type) {
            case 0: chunk.setBlock(x, y, z, BlockType.Cobblestone); break;
            case 1: chunk.setBlock(x, y, z, BlockType.Grass); break;
            case 4: chunk.setBlock(x, y, z, BlockType.Dirt); break;
            case 7: chunk.setBlock(x, y, z, BlockType.Bedrock); break;
            case 8: chunk.setBlock(x, y, z, BlockType.Water); break;
            case 12: chunk.setBlock(x, y, z, BlockType.Sand); break;
            case 13: chunk.setBlock(x, y, z, BlockType.Gravel); break;
            case 14: chunk.setBlock(x, y, z, BlockType.Gold); break;
            case 15: chunk.setBlock(x, y, z, BlockType.Iron); break;
            case 16: chunk.setBlock(x, y, z, BlockType.Coal); break;
            case 17: chunk.setBlock(x, y, z, BlockType.Wood); break;
            case 18: chunk.setBlock(x, y, z, BlockType.Leafs); break;
            default: //chunk.setBlock(x, y, z, BlockType.Checker); break;
          }

          reader.endObject();
        }

        reader.endArray();
        reader.endArray();

        world.add(chunk);

        Gdx.graphics.setTitle("Loading world: " + (i + 1) + "/" + 386);
      }

      zipInputStream.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return world;
  }
}
