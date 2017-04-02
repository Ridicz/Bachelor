package com.bachelor.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BachelorClient extends ApplicationAdapter {

	public static final short FIELD_OF_VIEW = 70;

	private PerspectiveCamera camera;

	private OrthographicCamera hudCamera;

	private SpriteBatch hudBatch;

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
		Gdx.graphics.setWindowedMode(1800, 900);
	}

	private void initCamera() {
		camera = new PerspectiveCamera(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, Player.CAMERA_HEIGHT, 0f);
		camera.near = 0.1f;
		camera.far = 250f;
		camera.update();

		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch = new SpriteBatch();
		hudBatch.setProjectionMatrix(hudCamera.combined);
	}

	@Override
	public void render () {
		inputHandler.update();
		player.update();
		renderer.render();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudCamera.update();
		hudBatch.begin();
		hudBatch.draw(new Texture("rock.jpg"), 900, 450, 100, 100);
		hudBatch.end();
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
