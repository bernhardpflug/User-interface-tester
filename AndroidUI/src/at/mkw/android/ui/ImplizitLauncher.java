package at.mkw.android.ui;

import android.app.Activity;
import android.os.Bundle;

public class ImplizitLauncher extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String path = getIntent().getExtras().getString("folder");
		if(path != null){
			ConvenienceMethods.startScenarioFromFolder(path, this);
		}
	}
	
}
