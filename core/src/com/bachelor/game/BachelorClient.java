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

	private World world;

	@Override
	public void create () {
		world = World.getInstance();
		initCamera();
		renderer = new Renderer(this);
		player = new Player(camera);
		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);
		Gdx.input.setCursorCatched(true);
	}

	private void initCamera() {
		camera = new PerspectiveCamera(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, Player.CAMERA_HEIGHT, 0f);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();
	}

	@Override
	public void render () {
		inputHandler.update();
		player.update();
		renderer.render();
//		renderer.cleanup();
	}
	
	@Override
	public void dispose () {

	}

	public PerspectiveCamera getCamera() {
		return camera;
	}

	public World getWorld() {
		return world;
	}
}
