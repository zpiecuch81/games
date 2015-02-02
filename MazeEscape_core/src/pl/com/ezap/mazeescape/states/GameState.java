package pl.com.ezap.mazeescape.states;

import pl.com.ezap.mazeescape.handlers.BoundedCamera;
import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class GameState {

	protected GameStateManager gsm;
	protected Game game;

	protected SpriteBatch sb;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;

	private Texture background;
	private TextureRegion reg;

	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		cam.zoom = 1;
		cam.update();
		hudCam = game.getHUDCamera();

		background = Game.res.getTexture("background");
		reg = new TextureRegion(background, 0, 0,
				(int)Math.min(cam.viewportWidth, background.getWidth()),
				(int)Math.min(cam.viewportHeight, background.getHeight()));
	}

	public abstract void handleInput();
	public abstract void update(float dt);

	public void render()
	{
		sb.begin();
		sb.draw(reg,0,0,cam.viewportWidth,cam.viewportHeight);
		sb.end();
	}

	public abstract void dispose();

}
