package com.bachelor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {

  private static final float MOUSE_SENSITIVITY = 0.18f;

  private Player player;

  public InputHandler(Player player) {
    this.player = player;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.ESCAPE) {
      Gdx.app.exit();
    }

    if (keycode == Input.Keys.W) {
      player.addKeyPressed(InputKeys.Forward);
    }

    if (keycode == Input.Keys.S) {
      player.addKeyPressed(InputKeys.Backward);
    }

    if (keycode == Input.Keys.A) {
      player.addKeyPressed(InputKeys.Left);
    }

    if (keycode == Input.Keys.D) {
      player.addKeyPressed(InputKeys.Right);
    }

    if (keycode == Input.Keys.SPACE) {
      player.addKeyPressed(InputKeys.Jump);
    }

    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (keycode == Input.Keys.W) {
      player.removeKeyPressed(InputKeys.Forward);
    }

    if (keycode == Input.Keys.S) {
      player.removeKeyPressed(InputKeys.Backward);
    }

    if (keycode == Input.Keys.A) {
      player.removeKeyPressed(InputKeys.Left);
    }

    if (keycode == Input.Keys.D) {
      player.removeKeyPressed(InputKeys.Right);
    }

    if (keycode == Input.Keys.SPACE) {
      player.removeKeyPressed(InputKeys.Jump);
    }

    return true;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == 0) {
      player.action(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    if (button == 1) {
      player.setBlock(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    updateRotation();

    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    updateRotation();

    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }

  private void updateRotation() {
    float yaw = Gdx.input.getDeltaX() * MOUSE_SENSITIVITY;
    float pitch = Gdx.input.getDeltaY() * MOUSE_SENSITIVITY;

    player.rotate(yaw, pitch);
  }
}
