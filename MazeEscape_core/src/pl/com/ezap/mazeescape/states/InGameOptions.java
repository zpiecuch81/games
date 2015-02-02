package pl.com.ezap.mazeescape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.main.Game;

public class InGameOptions extends GameState {

	private MenuButton restart;
	private MenuButton menu;
	private MenuButton back;
	private MenuButton instructions;

	public InGameOptions(GameStateManager gsm) {
		super(gsm);
		final int BUTTONS_COUNT = 4;
		float partHeight = hudCam.viewportHeight / ((BUTTONS_COUNT + 2) * 3 + 1);
		restart = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(14*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		restart.setText(Game.texts.get("repeat"));
		instructions = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(11*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		instructions.setText(Game.texts.get("instruction"));
		menu = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(8*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		menu.setText(Game.texts.get("menu"));
		back = new MenuButton(
				(int)hudCam.viewportWidth/2,
				(int)(5*partHeight),
				(int)(hudCam.viewportWidth*0.8),
				(int)(2*partHeight),
				hudCam);
		back.setText(Game.texts.get("back"));
	}

	@Override
	public void handleInput() {
		if(restart.isClicked()){
			gsm.clearStates();
			gsm.setState(GameStateManager.PLAY);
		}
		if(menu.isClicked()){
			gsm.clearStates();
			gsm.setState(GameStateManager.MENU);
		}
		if(back.isClicked()){
			gsm.popState();
		}
		if(instructions.isClicked()){
			gsm.pushState(GameStateManager.INSTRUCTIONS);
		}
	}

	@Override
	public void update(float dt) {
		restart.update(dt);
		menu.update(dt);
		back.update(dt);
		instructions.update(dt);
		handleInput();
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(hudCam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		restart.render(sb);
		menu.render(sb);
		back.render(sb);
		instructions.render(sb);
	}

	@Override
	public void dispose() {
		restart.dispose();
		menu.dispose();
		back.dispose();
		instructions.dispose();
	}

}
