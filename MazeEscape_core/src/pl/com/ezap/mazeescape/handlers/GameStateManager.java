package pl.com.ezap.mazeescape.handlers;

import java.util.Stack;

import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.states.GameState;
import pl.com.ezap.mazeescape.states.InGameOptions;
import pl.com.ezap.mazeescape.states.Instructions;
import pl.com.ezap.mazeescape.states.LevelSelect;
import pl.com.ezap.mazeescape.states.Menu;
import pl.com.ezap.mazeescape.states.Play;
import pl.com.ezap.mazeescape.states.Results;

public class GameStateManager {

	private Game game;

	private Stack<GameState> gameStates;

	public static final int MENU = 83774392;
	public static final int PLAY = 388031654;
	public static final int RESULT = 57283940;
	public static final int LEVEL_SELECT = -9238732;
	public static final int IN_GAME_OPTIONS = 2342344;
	public static final int INSTRUCTIONS = 98087767;

	public GameStateManager(Game game)
	{
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);
		Play.level = 1;
	}

	public void update(float dt)
	{
		gameStates.peek().update(dt);
	}

	public void render()
	{
		gameStates.peek().render();
	}

	public Game game()
	{
		return game;
	}

	private GameState getState(int state)
	{
		switch(state) {
		case MENU:
			return new Menu(this);
		case PLAY:
			return new Play(this);
		case RESULT:
			return new Results(this);
		case LEVEL_SELECT:
			return new LevelSelect(this);
		case IN_GAME_OPTIONS:
			return new InGameOptions(this);
		case INSTRUCTIONS:
			return new Instructions(this);
		}
		return null;
	}

	public void setState(int state)
	{
		popState();
		pushState(state);
	}

	public void pushState(int state)
	{
		gameStates.push(getState(state));
	}

	public void popState()
	{
		if( !gameStates.isEmpty() ) {
			GameState g = gameStates.pop();
			g.dispose();
		}
	}

	public void clearStates()
	{
		while( !gameStates.isEmpty() ) {
			popState();
		}
	}
}
