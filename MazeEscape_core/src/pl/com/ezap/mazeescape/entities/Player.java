package pl.com.ezap.mazeescape.entities;

import pl.com.ezap.mazeescape.handlers.B2DVars;
import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {

	private Body body;
	private Texture tex;

	private float time;
	
	public Player(Body body) {
		this.body=body;
		tex = Game.res.getTexture("dot");
	}

	public float getTime() {
		return time;
	}

	public void addTime(float time) {
		this.time += time;
	}

	public Body getBody() {
		return body;
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void update(float dt) {
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(tex, (body.getPosition().x * B2DVars.PPM - tex.getWidth() / 2), (int) (body.getPosition().y * B2DVars.PPM - tex.getHeight() / 2));
		sb.end();
	}
}
