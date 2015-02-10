package pl.com.ezap.mazeescape.states;

import java.sql.Timestamp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import pl.com.ezap.mazeescape.entities.Player;
import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.utils.FontFactory;
import pl.com.ezap.mazeescape.utils.Scores;

public class Results extends GameState {

	public static Player player;
	private final int menuHeight;
	private BitmapFont font;
	private MenuButton next;
	private MenuButton repeat;
	private MenuButton menu;
	private String result;
	private String bestResult;
	private Vector2 resultPosition;
	private Vector2 bestResultPosition;
	private int gainedStarsCount;
	private TextureRegion[] gainedStars;

	public Results(GameStateManager gsm) {
		super(gsm);

		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		menuHeight = (int)(cam.viewportHeight / 11);

		new Scores().setStarsCount(Play.level, 0);

		calculateGainedStars();
		createGainedStars();
		setTexts();
		createButtons();
	}

	@Override
	public void handleInput() {
	}

	@Override
	public void update(float dt) {
		next.update(dt);
		repeat.update(dt);
		menu.update(dt);

		if(next.isClicked()) {
			Play.level = Play.level + 1;
			gsm.setState(GameStateManager.PLAY);
		}
		if(repeat.isClicked()) {
			gsm.setState(GameStateManager.PLAY);
		}
		if(menu.isClicked()) {
			gsm.setState(GameStateManager.MENU);
		}
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(hudCam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		sb.begin();
		int width = Gdx.graphics.getWidth()/7;
		for( int i=0;i<gainedStars.length;++i) {
			sb.draw(gainedStars[i], (1+i*2)*width, (int)(5*menuHeight), (int)(width*1.5), (int)(width*1.5));
		}
		font.draw(sb, result, resultPosition.x, resultPosition.y);
		font.draw(sb, bestResult, bestResultPosition.x, bestResultPosition.y);
		sb.end();

		next.render(sb);
		repeat.render(sb);
		menu.render(sb);
	}

	@Override
	public void dispose() {
		font.dispose();
		next.dispose();
		repeat.dispose();
		menu.dispose();
	}

	private String timeFloatToString(float timeAsFloat)
	{
		Timestamp time = new Timestamp((long) (timeAsFloat * 1000));
		String result = time.toString().substring(14);
		switch( result.length() ){
		case 7:
			result = result + "0";
			break;
		case 9:
			result = result.substring(0, 8);
			break;
		}
		return result;
	}

	private void calculateGainedStars()
	{
		Scores scores = new Scores();
		float bestPossible = scores.getPossibleTime(Play.level);
		float currentTime = player.getTime();
		if( currentTime < bestPossible * 1.6 ) {
			gainedStarsCount = 3;
		} else if( currentTime < bestPossible * 3 ) {
			gainedStarsCount = 2;
		} else if( currentTime < bestPossible * 4 ) {
			gainedStarsCount = 1;
		}
		if( gainedStarsCount > scores.getStarsCount(Play.level) ) {
			scores.setStarsCount( Play.level, gainedStarsCount );
		}
	}

	private void createGainedStars()
	{
		gainedStars = new TextureRegion[3];
		Texture tex=Game.res.getTexture("star");
		gainedStars[0] = new TextureRegion(tex, gainedStarsCount > 0 ? 0 : 254, 0, 254, 242);
		gainedStars[1] = new TextureRegion(tex, gainedStarsCount > 1 ? 0 : 254, 0, 254, 242);
		gainedStars[2] = new TextureRegion(tex, gainedStarsCount > 2 ? 0 : 254, 0, 254, 242);
	}

	private void setTexts()
	{
		result = Game.texts.format("result", timeFloatToString(player.getTime()));
		font = FontFactory.getFont(result, (int)(Gdx.graphics.getWidth() * 0.9), Gdx.graphics.getHeight() );
		TextBounds resultBounds = font.getBounds(result);
		resultPosition = new Vector2(
				(Gdx.graphics.getWidth()-resultBounds.width)/2,
				(Gdx.graphics.getHeight()+resultBounds.height)/2 + 3*menuHeight );

		Scores scores = new Scores();
		float bestTime = scores.getBestTime(Play.level);
		if( bestTime == 0 || player.getTime() < bestTime ) {
			scores.setBestTime(Play.level, player.getTime());
			bestResult = Game.texts.get("newRecord");
		} else {
			bestResult = Game.texts.format("record", timeFloatToString(bestTime));
		}
		TextBounds bestResultBounds = font.getBounds(bestResult);
		bestResultPosition = new Vector2(
				(Gdx.graphics.getWidth()-bestResultBounds.width)/2,
				(Gdx.graphics.getHeight()+bestResultBounds.height)/2 + 2*menuHeight );
	}

	private void createButtons()
	{
		Scores scores = new Scores();
		next = new MenuButton(Game.V_WIDTH/2, (int)(4.5*menuHeight), (int)(cam.viewportWidth*0.8), menuHeight, cam, scores.getStarsCount(Play.level) > 0 );
		next.setText( Game.texts.get("next") );
		repeat = new MenuButton(Game.V_WIDTH/2, (int)(3*menuHeight), (int)(cam.viewportWidth*0.8), menuHeight, cam);
		repeat.setText( Game.texts.get("repeat") );
		menu = new MenuButton(Game.V_WIDTH/2, (int)(1.5*menuHeight), (int)(cam.viewportWidth*0.8), menuHeight, cam);
		menu.setText( Game.texts.get("menu") );
	}
}
