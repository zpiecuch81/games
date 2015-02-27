package pl.com.ezap.mazeescape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.utils.FontFactory;

public class Instructions extends GameState {

	private MenuButton back;
	private BitmapFont font;
	public static boolean initialInfo = false;

	public Instructions(GameStateManager gsm) {
		super(gsm);
		font = FontFactory.getFont(40);
		back = new MenuButton(
				(int)cam.viewportWidth/2,
				(int)(cam.viewportHeight/5),
				(int)(cam.viewportWidth*0.8),
				(int)(cam.viewportHeight/10),
				cam);
		if( initialInfo ){
			back.setText(Game.texts.get("play"));
			initialInfo = false;
		} else {
			back.setText(Game.texts.get("back"));
		}
	}

	@Override
	public void handleInput() {
		if(back.isClicked()){
			gsm.popState();
		}
	}

	@Override
	public void update(float dt) {
		back.update(dt);
		handleInput();
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		sb.begin();
		font.drawWrapped(
				sb,
				Game.texts.get("instructions"),
				cam.viewportWidth*0.1f,
				cam.viewportHeight*0.9f,
				cam.viewportWidth*0.8f,
				HAlignment.CENTER);
		sb.end();
		back.render(sb);
	}

	@Override
	public void dispose() {
		font.dispose();
		back.dispose();
	}

	@Override
	public void back() {
		gsm.popState();
	}

}
