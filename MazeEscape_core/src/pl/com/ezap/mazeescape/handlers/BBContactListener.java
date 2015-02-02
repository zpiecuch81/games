package pl.com.ezap.mazeescape.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class BBContactListener implements ContactListener {

	private boolean end = false;
	private boolean wallHit = false;
	private boolean prevWallHit = false;

	public BBContactListener() {
		super();
	}

	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if(fa == null || fb == null)
			return;
		if(fa.getUserData() != null && fa.getUserData().equals("end")) {
			end = true;
		}else if(fb.getUserData() != null && fb.getUserData().equals("end")) {
			end = true;
		}else {
			wallHit = true;
		}
	}

	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if(fa == null || fb == null)
			return;
		if(fa.getUserData() != null && fa.getUserData().equals("end")) {
		}else if(fb.getUserData() != null && fb.getUserData().equals("end")) {
		}else {
			wallHit = false;
		}
	}

	public boolean isEnd() {
		return end;
	}

	public boolean wallHit() {
		boolean toReturn = !prevWallHit && wallHit;
		prevWallHit = wallHit;
		return toReturn;
	}

	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}

}
