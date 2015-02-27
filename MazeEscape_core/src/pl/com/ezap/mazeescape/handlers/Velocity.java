package pl.com.ezap.mazeescape.handlers;

import com.badlogic.gdx.math.Vector2;

public class Velocity {

	private final float MIN_VELOCITY = 1.5f;
	private final float MAX_VELOCITY = 2.7f;
	private final float INC_VELOCITY = (MAX_VELOCITY - MIN_VELOCITY) / 20;
	private float velocity = MIN_VELOCITY;
	private float time = 0;

	private enum Direction{ NONE, LEFT, RIGHT, UP, DOWN };
	private Direction direction = Direction.NONE;

	public void left() {
		direction = Direction.LEFT;
	}

	public void right() {
		direction = Direction.RIGHT;
	}

	public void up() {
		direction = Direction.UP;
	}

	public void down() {
		direction = Direction.DOWN;
	}

	public void none() {
		velocity = MIN_VELOCITY;
		time = 0;
		direction = Direction.NONE;
	}

	public Vector2 getVelocity() {
		float x = 0;
		float y = 0;
		switch( direction ){
		case LEFT:
			x = -velocity;
			break;
		case RIGHT:
			x = velocity;
			break;
		case UP:
			y = velocity;
			break;
		case DOWN:
			y = -velocity;
			break;
		case NONE:
			break;
		default:
			break;
		}
		return new Vector2(x,y);
	}

	public boolean shouldSwitch() {
		return direction != Direction.NONE;
	}

	public void update(float dt) {
		if( direction != Direction.NONE ) {
			time += dt;
			if( time > 0.1f ) {
				time -= 0.1f;
				velocity += INC_VELOCITY;
				if( velocity > MAX_VELOCITY ) {
					velocity = MAX_VELOCITY;
				}
			}
		}
	}
}
