package pl.com.ezap.mazeescape.handlers;

import pl.com.ezap.mazeescape.utils.FontFactory;
import pl.com.ezap.mazeescape.utils.TexturesGenerator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class MenuButton {

	// center at x, y
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected final boolean available;

	Vector3 vec;
	private OrthographicCamera cam;
	private TextureRegion texture;
	private BitmapFont font;
	private TextBounds textBounds;

	private boolean clicked;

	private String text;

	public MenuButton(int x, int y, int width, int height, OrthographicCamera cam) {
		this(x, y, width, height, cam, true);
	}

	public MenuButton(int x, int y, int width, int height, OrthographicCamera cam, boolean available) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.available = available;

		this.vec = new Vector3();
		this.cam = cam;

		this.texture = getTexture();
	}

	protected TextureRegion getTexture() {
		return new TextureRegion(
				TexturesGenerator.getRegularButton(
				width,
				height,
				available ? Color.PURPLE : Color.GRAY));
	}

	public boolean isClicked() {
		return clicked && available;
	}

	public void setText(String s) {
		text = s;
		if( font != null ) {
			font.dispose();
		}
		font = FontFactory.getFont(text, 46, width, height, true);
		textBounds = font.getBounds(text);
	}

	public void update(float dt) {
		if( BBInput.wasTap() ) {
			vec.set(BBInput.x, BBInput.y, 0);
			cam.unproject(vec);
			if(vec.x > x - width / 2 && vec.x < x + width / 2 &&
				vec.y > y - height / 2 && vec.y < y + height / 2) {
				clicked = true;
			}
		} else {
			clicked = false;
		}
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(texture, x-(width/2), y-(height/2), width, height);
		if(text != null) {
			font.draw(sb, text, x-textBounds.width/2, y+textBounds.height/2);
		}
		sb.end();
	}

	public void dispose() {
		if(font != null) {
			font.dispose();
		}
	}
}
