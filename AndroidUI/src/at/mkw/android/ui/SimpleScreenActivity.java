package at.mkw.android.ui;

import model.Screen;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import at.mkw.android.ui.view.ScreenView;

public class SimpleScreenActivity extends BasicActivity {

	ScreenView activeView;
	
	@Override
	public void init() {
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onBackPressed() {

		String backTarget = ((Screen) curScreen).getBackButtonTarget();

		if ((backTarget == null) || (backTarget.equals("")))
			super.onBackPressed();
		else {
			model.Window target = ConvenienceMethods.SZENARIO.activateTarget(backTarget);

			Intent newIntent = ConvenienceMethods.getNewIntent(target, getApplicationContext());
			startActivity(newIntent);
		}
		return;
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		
		activeView = createView(curScreen);

		this.setContentView(activeView);
	}
	
	@Override
	public void onStop() {
		
		super.onStop();
		
		activeView.onDisappear();
		
	}

}