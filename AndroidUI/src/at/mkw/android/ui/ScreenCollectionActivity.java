package at.mkw.android.ui;

import java.util.ArrayList;

import model.Button;
import model.ModelUtils;
import model.Screen;
import model.ScreenCollection;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ScreenCollectionActivity extends Activity {
  private static final int SWIPE_MIN_DISTANCE = 50;
  private static final int SWIPE_MAX_OFF_PATH = 80;
  private static final int SWIPE_THRESHOLD_VELOCITY = 20;
  private GestureDetector gestureDetector;

  private ScreenCollection curScreen;
  private ImageView view;
  private ArrayList<model.Window> sliderItems;
  private int curPos;
  private ArrayList<Button> buttons;
  private DisplayMetrics metrics;
  private Bitmap bitmap;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    Bundle b = getIntent().getExtras();
    if (b != null) {
      curScreen = (ScreenCollection) ConvenienceMethods.SZENARIO.activateTarget(b.getString(ConvenienceMethods.CUR_ID));

      curPos = curScreen.getCurrentPosition();
    }

    setContentView(R.layout.main);

    gestureDetector = new GestureDetector(new MyGestureDetector());

    initCurScreen();
  }

  private void initCurScreen() {

    sliderItems = curScreen.getWindowItems();

    Screen activeScreen;
    if (sliderItems.get(curPos) instanceof ScreenCollection) {
      ScreenCollection collection = (ScreenCollection) sliderItems.get(curPos);

      activeScreen = ModelUtils.getActiveScreen(collection);
    } else {
      activeScreen = (Screen) sliderItems.get(curPos);
    }

    buttons = activeScreen.getButtons();

    view = (ImageView) findViewById(R.id.ImageView);
    view.setAdjustViewBounds(true);
    view.setScaleType(ScaleType.CENTER);

    bitmap = BitmapFactory.decodeFile(ConvenienceMethods.IMAGE_DIR + activeScreen.getImages().get(0));
    view.setImageBitmap(Bitmap.createScaledBitmap(bitmap, metrics.widthPixels, metrics.heightPixels, true));

    view.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          float realX = ((float) bitmap.getWidth() / (float) metrics.widthPixels) * event.getX();
          float realY = ((float) bitmap.getHeight() / (float) metrics.heightPixels) * event.getY();

          for (Button b : buttons) {
            if (b.getBounds().contains((int) realX, (int) realY)) {

              model.Window target = ConvenienceMethods.SZENARIO.activateTarget(b.getTarget(Button.KlickDuration.SINGLE));

              Intent newIntent = ConvenienceMethods.getNewIntent(target, getApplicationContext());
              reset();
              startActivity(newIntent);
            }
          }
        }
        return true;
      }
    });
  }

  private void reset() {
    bitmap = null;
  }
  
  class MyGestureDetector extends SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      try {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
          return false;
        // right to left swipe
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

          if (curPos < curScreen.getWindowItems().size() - 1)
            curScreen.setCurrentPosition(curPos++);

        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

          if (curPos > 0)
            curScreen.setCurrentPosition(curPos--);

        }
      } catch (Exception e) {
      }

      initCurScreen();
      return false;
    }
  }
}