package pl.com.ezap.mazeescape.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Scores {

	private Preferences prefs = Gdx.app.getPreferences("MazeEscapeScores");

	public Scores() {
	}

	public void setStarsCount( int level, int count) {
		prefs.putInteger("s" + level, count);
		prefs.flush();
	}

	public int getStarsCount(int level) {
		return prefs.getInteger("s" + level);
	}

	public boolean isLevelEnabled(int level) {
		if(level == 1) {
			return true;
		}
		return getStarsCount(level-1) > 0;
	}

	public void setBestTime( int level, float time) {
		prefs.putFloat("b" + level, time);
		prefs.flush();
	}

	public float getBestTime(int level) {
		return prefs.getFloat("b" + level);
	}

	public void setPossibleTime( int level, float count) {
		prefs.putFloat("p" + level, count);
		prefs.flush();
	}

	public float getPossibleTime(int level) {
		return prefs.getFloat("p" + level);
	}
}
