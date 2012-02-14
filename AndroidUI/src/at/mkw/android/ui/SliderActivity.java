package at.mkw.android.ui;

import java.util.ArrayList;

import model.ModelUtils;
import model.Slider;
import android.content.Intent;
import at.mkw.android.ui.view.ScreenView;
import at.mkw.android.ui.widget.HorizontalPager;

public class SliderActivity extends BasicActivity {

	int currentPos;
	ArrayList<ScreenView> sliderViews;

	@Override
	public void init() {

	}

	@Override
	public void onStart() {

		super.onStart();

		HorizontalPager viewSlider = new HorizontalPager(this);

		final Slider slider = ModelUtils.getSlider(curScreen);

		sliderViews = new ArrayList<ScreenView>();
		
		for (model.Window window : slider.getWindowItems()) {
			ScreenView screenView = createView(window);
			viewSlider.addView(screenView);
			sliderViews.add(screenView);
		}

		// go to last seen view of slider
		currentPos = slider.getCurrentPosition();

		viewSlider.setCurrentScreen(currentPos, false);
		
		viewSlider.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
			
			public void onScreenSwitched(int screen) {
				slider.setCurrentPosition(screen);
				currentPos = screen;
			}
		});

		this.setContentView(viewSlider);
	}

	@Override
	public void onStop() {

		super.onStop();

		for (ScreenView view : sliderViews) {
			view.onDisappear();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		
		if (sliderViews != null) {
			String backButtonTarget = sliderViews.get(currentPos).getScreen().getBackButtonTarget();
			
			if (backButtonTarget != null && backButtonTarget.length() > 0) {
				model.Window target = ConvenienceMethods.SZENARIO.activateTarget(backButtonTarget);

				Intent newIntent = ConvenienceMethods.getNewIntent(target, getApplicationContext());
				startActivity(newIntent);
				return;
			}
		}
		
		super.onBackPressed();
	}
}
