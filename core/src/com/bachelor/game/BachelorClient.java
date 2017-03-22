package com.bachelor.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BachelorClient extends ApplicationAdapter {

	public static final short FIELD_OF_VIEW = 70;

	private PerspectiveCamera camera;

	private Renderer renderer;

	private InputHandler inputHandler;

	private Player player;

	@Override
	public void create () {
		initCamera();
		renderer = new Renderer(this);
		player = new Player(camera);
		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);
		Gdx.input.setCursorCatched(true);
	}

	private void initCamera() {
		camera = new PerspectiveCamera(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, 1.6f, 0f);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();
	}

	@Override
	public void render () {
		inputHandler.update();
		player.update();
		renderer.render();
	}
	
	@Override
	public void dispose () {

	}

	public PerspectiveCamera getCamera() {
		return camera;
	}
}