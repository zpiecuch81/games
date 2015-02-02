package pl.com.ezap.mazeescape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.LevelButton;
import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.utils.Scores;

public class LevelSelect extends GameState {

	public final static int LEVELS_COUNT = 5;
	private final int LEVELS_PER_ROW = 5;

	private LevelButton[] buttons;

	public LevelSelect(GameStateManager gsm) {
		super(gsm);

		int buttonWidth = (int)(cam.viewportWidth / LEVELS_PER_ROW /2);
		int buttonHeight = (int)(cam.viewportHeight / 10);
		buttons = new LevelButton[LEVELS_COUNT];
		//Scores scores = new Scores();
		for(int button = 0; button < buttons.length; button++) {
			int row = (int)(button / LEVELS_PER_ROW);
			buttons[button] = new LevelButton(
					(int)((2*(button%LEVELS_PER_ROW)+1)*buttonWidth),
					(int)(cam.viewportHeight-(2*row+1.5)*buttonHeight),
					buttonWidth,
					buttonHeight,
					cam,
					true );
					//scores.isLevelEnabled(button+1) );
			buttons[button].setText(button + 1 + "");
		}
	}

	public void handleInput() {
	}

	public void update(float dt) {
		handleInput();
		for(int button = 0; button < buttons.length; button++) {
			buttons[button].update(dt);
			if(buttons[button].isClicked()) {
				Play.level = button + 1;
				Game.res.getSound("levelselect").play();
				gsm.setState(GameStateManager.PLAY);
				if( button == 0 ) {
					gsm.pushState(GameStateManager.INSTRUCTIONS);
				}
			}
		}
	}

	public void render() {
		sb.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		for(int button = 0; button < buttons.length; button++) {
			buttons[button].render(sb);
		}
	}
	
	public void dispose() {
		for(int button = 0; button < buttons.length; button++) {
			buttons[button].dispose();
		}
	}
	
}
