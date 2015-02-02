package pl.com.ezap.mazeescape.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

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

	public static Texture getLevelButton( int width, int height, boolean grayed) {

		Pixmap pix = new Pixmap(width, height, Format.RGBA8888);
		pix.setColor(grayed?Color.DARK_GRAY:Color.BLUE);
		pix.fillRectangle(0,0,width,height);

		int dx = width / 7;
		int dy = height / 7;
		pix.setColor(grayed?Color.GRAY:Color.ORANGE);
		pix.fillRectangle(0, dy, 2*dx, dy);
		pix.fillRectangle(dx, 2*dy, 2*dx, dy);
		pix.fillRectangle(2*dx, 3*dy, 2*dx, dy);
		pix.fillRectangle(3*dx, 4*dy, dx, 3*dy);
		pix.fillRectangle(3*dx, 6*dy, 4*dx, dy);
		pix.fillRectangle(6*dx, dy, dx, 6*dy);
		pix.fillRectangle(4*dx, dy, 3*dx, dy);

		Texture texture = new Texture(pix);
		pix.dispose();
		return texture;
	}
}
