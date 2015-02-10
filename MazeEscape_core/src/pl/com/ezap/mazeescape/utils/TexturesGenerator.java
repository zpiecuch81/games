package pl.com.ezap.mazeescape.utils;

import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexturesGenerator {

	public static Texture getRegularButton( int width, int height, Color color) {
		int radius = width/3;
		if( radius > 15) radius = 15; 
		Pixmap pix = new Pixmap(width, height, Format.RGBA8888);
		pix.setColor(color);
		pix.fillRectangle(0, radius, pix.getWidth(), pix.getHeight()-2*radius);
		pix.fillRectangle(radius, 0, pix.getWidth() - 2*radius, pix.getHeight());
		// Bottom-left circle
		pix.fillCircle(radius, radius, radius);
		// Top-left circle
		pix.fillCircle(radius, pix.getHeight()-radius, radius);
		// Bottom-right circle
		pix.fillCircle(pix.getWidth()-radius, radius, radius);
		// Top-right circle
		pix.fillCircle(pix.getWidth()-radius, pix.getHeight()-radius, radius);
		Texture texture = new Texture(pix);
		pix.dispose();
		return texture;
	}

	public static TextureRegion getLevelButton( int width, int height, boolean grayed) {
		Texture tex=Game.res.getTexture("levels");
		return new TextureRegion(tex, grayed ? 95 : 0, 0, 95, 100);
	}
}
