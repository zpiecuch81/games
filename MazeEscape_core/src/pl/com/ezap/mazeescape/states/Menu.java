package pl.com.ezap.mazeescape.states;

import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Menu extends GameState {

	private MenuButton play;
	private MenuButton exit;
	private Texture title;
	private Vector2 titlePosition;
	private Vector2 titleSize;

	public Menu(GameStateManager gsm) {
		super(gsm);

		title = Game.res.getTexture("title");
		float titleZoom = (float)(hudCam.viewportWidth*0.8)/(float)title.getWidth();
		titleSize = new Vector2();
		titleSize.x = title.getWidth() * titleZoom;
		titleSize.y = title.getHeight() * titleZoom;
		float partHeight = (hudCam.viewportHeight-titleSize.y)/8;
		titlePosition = new Vector2();
		titlePosition.x = (hudCam.viewportWidth-title.getWidth()*titleZoom)/2;
		titlePosition.y = partHeight*7;
		play = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(5*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		play.setText(Game.texts.get("play"));
		exit = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(2*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		exit.setText(Game.texts.get("exit"));
	}

	public void handleInput() {
		if(exit.isClicked()){
			Gdx.app.exit();
		}
		if(play.isClicked()){
			gsm.setState(GameStateManager.LEVEL_SELECT);
		}
	}
	public void update(float dt) {
		play.update(dt);
		exit.update(dt);
		handleInput();
	}
	
	public void render() {
		sb.setProjectionMatrix(hudCam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		sb.begin();
		sb.draw(title, titlePosition.x, titlePosition.y, titleSize.x, titleSize.y);
		sb.end();
		play.render(sb);
		exit.render(sb);
	}
	
	public void dispose() {
		play.dispose();
		exit.dispose();
	}

	@Override
	public void back() {
		Gdx.app.exit();
	}
	
}
