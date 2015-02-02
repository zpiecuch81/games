package pl.com.ezap.mazeescape.handlers;

import pl.com.ezap.mazeescape.utils.TexturesGenerator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class LevelButton extends MenuButton {

	public LevelButton(int x, int y, int width, int height, OrthographicCamera cam, boolean available) {
		super(x, y, width, height, cam, available);
	}

	protected Texture getTexture() {
		return TexturesGenerator.getLevelButton(width, height, !available);
	}
}
