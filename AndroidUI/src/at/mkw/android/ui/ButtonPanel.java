package at.mkw.android.ui;

import model.Button;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.view.MotionEvent;
import android.view.View;
import at.mkw.android.event.ButtonEvent;
import at.mkw.android.event.ButtonListener;

public class ButtonPanel extends GestureOverlayView {

	private Button button;
	private ButtonListener buttonListener;

	public ButtonPanel(Context context, final Button button) {
		super(context);
		this.button = button;

		setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					model.Window target = ConvenienceMethods.SZENARIO.getTarget(button.getTarget(Button.KlickDuration.SINGLE));

					if (buttonListener != null) {
						buttonListener.actionPerformed(new ButtonEvent(target.getFullId()));
					}
					else {
						throw new RuntimeException("button without listener");
					}
				}
				return true;
			}
		});
	}

	public void setButtonListener(ButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public ButtonListener getButtonListener() {
		return buttonListener;
	}

}
