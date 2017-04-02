package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.HashSet;
import java.util.Set;

public class InputHandler implements InputProcessor {

  private static final float MOUSE_SENSITIVITY = 0.2f;

  private Set<Integer> pressedKeys;

  private Player player;

  public InputHandler(Player player) {
    pressedKeys = new HashSet<Integer>();
    this.player = player;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.ESCAPE) {
      Gdx.app.exit();
    }

    return pressedKeys.add(keycode);
  }

  @Override
  public boolean keyUp(int keycode) {
    return pressedKeys.remove(keycode);
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == 0) {
      player.action(screenX, screenY);
    }

    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    float yaw = Gdx.input.getDeltaX() * MOUSE_SENSITIVITY;
    float pitch = Gdx.input.getDeltaY() * MOUSE_SENSITIVITY;

    player.rotate(yaw, pitch);

    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }

  public void update() {
    if (pressedKeys.contains(Input.Keys.W)) {
      player.move(Direction.Forward);
    }

    if (pressedKeys.contains(Input.Keys.S)) {
      player.move(Direction.Backward);
    }

    if (pressedKeys.contains(Input.Keys.A)) {
      player.move(Direction.Left);
    }

    if (pressedKeys.contains(Input.Keys.D)) {
      player.move(Direction.Right);
    }

    if (pressedKeys.contains(Input.Keys.SPACE)) {
      player.jump();
    }
  }
}
