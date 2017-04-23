package com.bachelor.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BachelorClient extends ApplicationAdapter {

	public static final short FIELD_OF_VIEW = 70;

	private static BachelorClient gameInstance;

	private PerspectiveCamera camera;

	private OrthographicCamera hudCamera;

	private SpriteBatch hudBatch;

	private Renderer renderer;

	private Player player;

	private World world;

	private BitmapFont font;

	public static BachelorClient getInstance() {
		return gameInstance;
	}

	@Override
	public void create () {
		initCamera();
		renderer = new Renderer(this);
		player = new Player(camera);
		Gdx.input.setInputProcessor(new InputHandler(player));
		Gdx.input.setCursorCatched(true);
		world = new World();
		gameInstance = this;
	}

	private void initCamera() {
		camera = new PerspectiveCamera(FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, Player.CAMERA_HEIGHT, 0f);
		camera.near = 0.1f;
		camera.far = 350f;
		camera.update();

		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().scale(2f);

		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch = new SpriteBatch();
		hudBatch.setProjectionMatrix(hudCamera.combined);
	}

	@Override
	public void render () {
		player.update();
		renderer.render();

		hudCamera.update();
		hudBatch.begin();
		Sprite sprite = new Sprite(new Texture("crosshair.png"));
		sprite.setPosition(-20, -20);
		sprite.draw(hudBatch);
		font.draw(hudBatch, player.getPosition().toString(), 0, 400f);
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
