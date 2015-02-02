package pl.com.ezap.mazeescape.entities;

import java.sql.Timestamp;

import pl.com.ezap.mazeescape.main.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {

	private Player player;
	private BitmapFont font;

	public HUD(Player player) {
		this.player = player;
		this.font = new BitmapFont();
		this.font.setColor(Color.WHITE);
		this.font.setScale(2);
	}

	public void render(SpriteBatch sb) {
		sb.begin();

		Timestamp time = new Timestamp((long) (player.getTime() * 1000));
		String timeString = time.toString().substring(14);
		switch( timeString.length() ){
		case 7:
			timeString = timeString + "0";
			break;
		case 9:
			timeString = timeString.substring(0, 8);
			break;
		}

		font.draw(sb, timeString, Game.V_HEIGHT *0.15f, Game.V_HEIGHT *0.95f);

		sb.end();
	}

}
