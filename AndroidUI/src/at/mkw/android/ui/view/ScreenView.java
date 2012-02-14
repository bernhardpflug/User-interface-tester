package at.mkw.android.ui.view;

import java.util.ArrayList;

import model.Button;
import model.ModelUtils;
import model.Screen;
import model.Window;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import at.mkw.android.event.ButtonEvent;
import at.mkw.android.event.ButtonListener;
import at.mkw.android.ui.ButtonPanel;
import at.mkw.android.ui.ConvenienceMethods;
import at.mkw.android.ui.R;

/**
 * Used for every model.screen object to display itself
 * 
 * Contains an image and buttons for targeting other screens
 * 
 * Delegates touch events to parents to support combination with view flipper
 * 
 * @author Bernhard
 *
 */
public class ScreenView extends android.widget.ScrollView {

	private Screen screen;
	private static final int thresholdX = 30;
	private int startX;
	
	private ArrayList<GestureOverlayView> buttonPanels;
	
	private ImageView imageView;
	
	public ScreenView(final Context context, Window window) {
		super(context);
		
		buttonPanels = new ArrayList<GestureOverlayView>();
		
		screen = ModelUtils.getActiveScreen(window);

		// layout
		AbsoluteLayout layout = new AbsoluteLayout(context);

		// image
		imageView = new ImageView(context,window);
		imageView.onDisplay();

		layout.addView(imageView);

		// add buttons
		ButtonListener bListener = new ButtonListener() {
			public void actionPerformed(ButtonEvent event) {
				
				model.Window target = ConvenienceMethods.SZENARIO.activateTarget(event.getTarget());
				
				Intent newIntent = ConvenienceMethods.getNewIntent(target, context);

				context.startActivity(newIntent);
			}
		};
		
		for (final Button button : screen.getButtons()) {

			ButtonPanel linkPanel = new ButtonPanel(context,button);
			linkPanel.setButtonListener(bListener);

			buttonPanels.add(linkPanel);

			AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams((int) (button.getBounds().getWidth() * imageView.getScaleX()), (int) (button.getBounds().getHeight() * imageView.getScaleY()), (int) (button.getBounds().getX() * imageView.getScaleX()), (int) (button.getBounds().getY() * imageView.getScaleY()));

			layout.addView(linkPanel, params);
		}
		
		this.addView(layout);
	}
	
	/**
	 * Enables/disables showing all button panels
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		
		for (GestureOverlayView linkpanel : buttonPanels) {

			if (debug) {
				linkpanel.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.my_border));
			} else {
				linkpanel.setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}
	
	public void onDisappear() {
		imageView.onDisappear();
	}
	
	/**
	 * Handles delegation to view flipper to enable both vertical scrolling
	 * with scroll view and horizontal scrolling in view flipper
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction()== MotionEvent.ACTION_DOWN) {
			startX = (int)event.getX();
			
		}
		
		if (event.getAction()== MotionEvent.ACTION_MOVE){
			int diffX = Math.abs((int)event.getX()-startX);
			
			if (diffX > thresholdX && this.getParent() instanceof View) {
				
				View view = (View)this.getParent();
				return view.onTouchEvent(event);
			}
		}
		else {
			if (this.getParent() instanceof View) {
				View view = (View)this.getParent();
				view.onTouchEvent(event);
			}
		}
//		Log.d(this.getClass().toString(), ""+event.getAction());
		
		return super.onTouchEvent(event);
//		return false;
	}

	public Screen getScreen() {
		return screen;
	}

}
