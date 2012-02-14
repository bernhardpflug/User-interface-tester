package at.mkw.android.ui;

import model.Window;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import at.mkw.android.ui.image.ImagePool;
import at.mkw.android.ui.view.ScreenView;

public abstract class BasicActivity extends Activity {

	private boolean debug = false;
	protected model.Window curScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//		MemoryInfo minfo = new ActivityManager.MemoryInfo();
//		activityManager.getMemoryInfo(minfo);
//		
//		Log.d("avMemory", ""+minfo.availMem);
		
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Bundle b = getIntent().getExtras();

		if (b != null) {

			// TODO

			// Durch bilder wird immer outofmemory exception geworfen
			// Wenn exception geworfen wird verliert er static variable, darum
			// sollte
			// szenario parcelable werden (oder wrapper schreiben)

			// derzeitige lšsung: Hashmap in Convenience methods

			// Log.e("curID", ""+b.getString(ConvenienceMethods.CUR_ID));
			// Log.e("szenario", ""+ConvenienceMethods.SZENARIO);
			// Log.e("target",
			// ""+ConvenienceMethods.SZENARIO.getTarget(b.getString(ConvenienceMethods.CUR_ID)));
			curScreen = (Window) ConvenienceMethods.SZENARIO.activateTarget(b.getString(ConvenienceMethods.CUR_ID));

			init();
		}
	}

	public abstract void init();

//	protected OnGestureListener gestureListener = new OnGestureListener() {
//
//		public boolean onDown(MotionEvent arg0) {
//			return false;
//		}
//
//		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
//			return false;
//		}
//
//		public void onLongPress(MotionEvent arg0) {
//		}
//
//		public boolean onScroll(MotionEvent start, MotionEvent current, float disX, float disY) {
//
//			int overallDisY = (int)(current.getRawY() - start.getRawY());
//			Log.d(BasicActivity.class.toString(), "scroll of "+overallDisY);
//			
//			return true;
//		}
//
//		public void onShowPress(MotionEvent arg0) {
//		}
//
//		public boolean onSingleTapUp(MotionEvent arg0) {
//			return false;
//		}
//
//	};
//	protected GestureDetector gestureScanner = new GestureDetector(gestureListener);
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent me) {
//		return gestureScanner.onTouchEvent(me);
//	}

	protected ScreenView createView(Window window) {

		ScreenView scroll = new ScreenView(this,window);
		scroll.setHorizontalScrollBarEnabled(false);
		return scroll;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("Debug");
		menu.add("Restart");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals("Debug")) {

			debug = !debug;

			//TODO enable debugging in subclasses
			
		} else if (item.getTitle().equals("Restart")) {
			ImagePool.reset();
			Intent intent = new Intent(this.getApplicationContext(), Launcher.class);
			startActivity(intent);
		}

		return true;
	}
}
