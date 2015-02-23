package pl.com.ezap.mazeescape;

import pl.com.ezap.mazeescape.main.Game;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RateApp.app_launched( this );

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Game(), config);
		AdView adView = new AdView(this); // Put in your secret key here
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-4163298076894798/2080813469");
		AdRequest.Builder adBuilder = new AdRequest.Builder();
		adBuilder.addTestDevice("515D01A11F47A111FC078E270323739B");
		adView.loadAd(adBuilder.build());

		RelativeLayout.LayoutParams adParams = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		RelativeLayout layout = new RelativeLayout(this);
		layout.setBackgroundColor(0xffffffff);
		layout.setBackgroundResource(R.drawable.ezap_logo_v);
		layout.addView(gameView);
		layout.addView(adView, adParams);
		setContentView(layout);
	}
}
