package pl.com.ezap.mazeescape.main;

import pl.com.ezap.mazeescape.handlers.BBInput;
import pl.com.ezap.mazeescape.handlers.BBInputProcessor;
import pl.com.ezap.mazeescape.handlers.BoundedCamera;
import pl.com.ezap.mazeescape.handlers.Content;
import pl.com.ezap.mazeescape.handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.I18NBundle;

public class Game implements ApplicationListener {

	public static final String TITLE = "Block Bunny";
	public static int V_WIDTH = 400;
	public static int V_HEIGHT = 400;
	public static final float STEP = 1 / 60f;

	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;

	public static Content res;
	public static I18NBundle texts;

	public void create()
	{
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		BBInputProcessor bbInputProcessor = new BBInputProcessor();
		inputMultiplexer.addProcessor(bbInputProcessor);
		GestureDetector gd = new GestureDetector(bbInputProcessor);
		gd.setLongPressSeconds(0.5f);
		inputMultiplexer.addProcessor(gd);
		Gdx.input.setInputProcessor(inputMultiplexer);

		res = new Content();
		res.loadTexture("images/dot.png");
		res.loadTexture("images/title.png");
		res.loadTexture("images/background.jpg");
		res.loadTexture("images/star.png");
		res.loadTexture("images/levels.png");

		res.loadSound("sfx/jump.wav");
		res.loadSound("sfx/crystal.wav");
		res.loadSound("sfx/levelselect.wav");
		res.loadSound("sfx/hit.wav");
		res.loadSound("sfx/changeblock.wav");

//		res.loadMusic("music/bbsong.ogg");
//		res.getMusic("bbsong").setLooping(true);
//		res.getMusic("bbsong").setVolume(0.5f);
//		res.getMusic("bbsong").play();

		FileHandle baseFileHandle = Gdx.files.internal("i18n/texts");
		texts = I18NBundle.createBundle(baseFileHandle);

		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();

		cam = new BoundedCamera();
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		sb = new SpriteBatch();
		gsm = new GameStateManager(this);
	}

	public void render()
	{
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		BBInput.update();
	}

	public void dispose() {
		res.removeAll();
	}

	public void resize(int w, int h) {}

	public void pause() {}

	public void resume() {}

	public SpriteBatch getSpriteBatch() { return sb; }
	public BoundedCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }

}
