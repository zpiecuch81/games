package pl.com.ezap.mazeescape.utils;

import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class CountDown {
	private static final float MIN_SCALE = 1;
	private static final float MAX_SCALE = 20;
	private float countTo;
	private float currentCount=0;

	private BitmapFont font;
	private TextBounds textBounds;

	public CountDown(float countTo)
	{
		this.countTo = countTo;

		this.font = new BitmapFont();
		this.font.setColor(Color.WHITE);
		this.font.setScale((float) 7);
	}

	public void update(float dt)
	{
		currentCount += dt;
	}

	public void render(SpriteBatch sb)
	{
		if( isStillCounting() ) {
			float ratio = 1 - currentCount%1;
			float newScale = MIN_SCALE + (MAX_SCALE-MIN_SCALE)*ratio;
			font.setScale(newScale);
			String toDisplay = ""+getCurrentDigit();
			textBounds = font.getBounds(toDisplay);
			sb.begin();
			font.draw(sb, toDisplay, (Game.V_WIDTH-textBounds.width)/2, (Game.V_HEIGHT+textBounds.height)/2);
			sb.end();
		}
	}

	public boolean isStillCounting()
	{
		return currentCount < countTo;
	}

	private int getCurrentDigit()
	{
		return (int)((countTo - currentCount + 1));
	}
}
