package pl.com.ezap.mazeescape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.LevelButton;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.utils.Scores;

public class LevelSelect extends GameState {

	public final static int LEVELS_COUNT = 26;
	private final static int LEVELS_PER_ROW = 5;
	private final static int LEVEL_ROWS = 5;
	private final static int LEVELS_PER_PAGE = LEVEL_ROWS * LEVELS_PER_ROW;
	private final static int PAGES_COUNT = LEVELS_COUNT / LEVELS_PER_PAGE + 1;
	public static int currentPage = 1;
	private int buttonShift;

	private LevelButton[] buttons;
	private MenuButton[] pageButton;

	public LevelSelect(GameStateManager gsm) {
		super(gsm);

		if( currentPage < 1 || currentPage > PAGES_COUNT)
			currentPage = 1;
		int buttonWidth = (int)(cam.viewportWidth / LEVELS_PER_ROW /1.5);
		int buttonHeight = (int)(cam.viewportHeight / ( LEVEL_ROWS + 1 ) / 1.5);
		buttonShift = ( currentPage - 1 ) * LEVELS_PER_PAGE + 1;
		int levelsOnThisPage = currentPage == PAGES_COUNT ? LEVELS_COUNT%LEVELS_PER_PAGE : LEVELS_PER_PAGE;
		buttons = new LevelButton[levelsOnThisPage];
		Scores scores = new Scores();
		for(int button = 0; button < buttons.length; button++) {
			int row = (int)(button / LEVELS_PER_ROW);
			buttons[button] = new LevelButton(
					(int)((1.5 * (button%LEVELS_PER_ROW) + 0.75) * buttonWidth),
					(int)(cam.viewportHeight - (1.5 * row + 0.75) * buttonHeight),
					buttonWidth,
					buttonHeight,
					cam,
					scores.isLevelEnabled(button + buttonShift) );
			buttons[button].setText(button + buttonShift + "");
		}
		pageButton = new MenuButton[2];
		if(currentPage > 1) {
			pageButton[0] = new MenuButton(
					(int)(buttonWidth * 1.75),
					(int)(cam.viewportHeight - (1.5 * LEVEL_ROWS + 0.75) * buttonHeight),
					buttonWidth*3,
					buttonHeight,
					hudCam);
			pageButton[0].setText("<<<");
		}
		if(currentPage < PAGES_COUNT) {
			pageButton[1] = new MenuButton(
					(int)(cam.viewportWidth - buttonWidth * 1.75),
					(int)(cam.viewportHeight - (1.5 * LEVEL_ROWS + 0.75) * buttonHeight),
					buttonWidth*3,
					buttonHeight,
					hudCam);
			pageButton[1].setText(">>>");
		}
	}

	public void handleInput() {
	}

	public void update(float dt) {
		handleInput();
		for(int button = 0; button < buttons.length; button++) {
			buttons[button].update(dt);
			if(buttons[button].isClicked()) {
				Play.level = button + buttonShift;
				Game.res.getSound("levelselect").play();
				gsm.setState(GameStateManager.PLAY);
				if( button + buttonShift == 0 ) {
					gsm.pushState(GameStateManager.INSTRUCTIONS);
				}
			}
		}
		if(pageButton[0] != null) {
			pageButton[0].update(dt);
			if(pageButton[0].isClicked()) {
				--currentPage;
				gsm.setState(GameStateManager.LEVEL_SELECT);
			}
		}
		if(pageButton[1] != null) {
			pageButton[1].update(dt);
			if(pageButton[1].isClicked()) {
				++currentPage;
				gsm.setState(GameStateManager.LEVEL_SELECT);
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
		if(pageButton[0] != null)
			pageButton[0].render(sb);
		if(pageButton[1] != null)
			pageButton[1].render(sb);
	}

	public void dispose() {
		for(int button = 0; button < buttons.length; button++) {
			buttons[button].dispose();
		}
		if(pageButton[0] != null)
			pageButton[0].dispose();
		if(pageButton[1] != null)
			pageButton[1].dispose();
	}

}
