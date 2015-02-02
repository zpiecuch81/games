package pl.com.ezap.mazeescape.utils;

import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontFactory {

	public static BitmapFont getFont( String text, int width, int height) {
		return getFont(text, 32, width, height, false );
	}

	public static BitmapFont getFont( String text, int startSize, int width, int height, boolean onlyShrink) {
		int textSize = startSize;
		BitmapFont font = getFont(textSize);
		TextBounds textBounds = font.getBounds(text);
		while( !onlyShrink
				&& textBounds.width<width*0.5
				&& textBounds.height<height*0.75) {
			textSize += 4;
			font.dispose();
			font = getFont(textSize);
			textBounds = font.getBounds(text);
		}
		if(textBounds.width>=width*0.95 || textBounds.height>=height*0.95) {
			font.dispose();
			font = getFont(textSize-4);
		}
		return font;
	}

	public static BitmapFont getFont( int size ) {
		return getFont(size, Color.WHITE);
	}

	public static BitmapFont getFont( int size, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/timesbi.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.characters = Game.texts.get("characterSet");
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		font.setColor(color);
		font.setScale(1);
		return font;
	}
}
