package com.bachelor.game;

import com.bachelor.game.entities.Tool;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

  private Map<Tool, Integer> inventory;

  private short capacity = 128;

  public Inventory() {
    inventory = new HashMap<>();
  }

  public boolean addToInventory(Tool tool) {
//    if (inventory)
    return false;
  }
}
