package pl.com.ezap.mazeescape.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class BBInputProcessor implements InputProcessor, GestureListener {

	@Override
	public boolean mouseMoved(int x, int y) {
		BBInput.x = x;
		BBInput.y = y;
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		BBInput.x = x;
		BBInput.y = y;
		BBInput.down = true;
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		BBInput.x = x;
		BBInput.y = y;
		BBInput.down = true;
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		BBInput.x = x;
		BBInput.y = y;
		BBInput.down = false;
		BBInput.setKey(BBInput.ZOOMIN, false);
		return false;
	}

	@Override
	public boolean keyDown(int k) {
		if(k == Keys.SPACE) BBInput.setKey(BBInput.ZOOMIN, true);
		if(k == Keys.UP) BBInput.setKey(BBInput.UP, true);
		if(k == Keys.LEFT) BBInput.setKey(BBInput.LEFT, true);
		if(k == Keys.RIGHT) BBInput.setKey(BBInput.RIGHT, true);
		if(k == Keys.DOWN) BBInput.setKey(BBInput.DOWN, true);
		if(k == Keys.BACK) BBInput.setKey(BBInput.BACK, true);
		return false;
	}

	@Override
	public boolean keyUp(int k) {
		if(k == Keys.SPACE) BBInput.setKey(BBInput.ZOOMIN, false);
		if(k == Keys.UP) BBInput.setKey(BBInput.UP, false);
		if(k == Keys.LEFT) BBInput.setKey(BBInput.LEFT, false);
		if(k == Keys.RIGHT) BBInput.setKey(BBInput.RIGHT, false);
		if(k == Keys.DOWN) BBInput.setKey(BBInput.DOWN, false);
		if(k == Keys.BACK) BBInput.setKey(BBInput.BACK, false);
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		BBInput.setKey(BBInput.ZOOMIN, true);
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if( Math.abs(velocityX)>Math.abs(velocityY) ) {
			if( velocityX > 0 ) {
				BBInput.wasFlinged(BBInput.RIGHT);
			} else {
				BBInput.wasFlinged(BBInput.LEFT);
			}
		} else {
			if( velocityY > 0 ) {
				BBInput.wasFlinged(BBInput.DOWN);
			} else {
				BBInput.wasFlinged(BBInput.UP);
			}
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
}
