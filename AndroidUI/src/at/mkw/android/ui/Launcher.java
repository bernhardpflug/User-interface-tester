package at.mkw.android.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launcher extends Activity implements OnClickListener, DialogInterface.OnClickListener {
	private View button;
	private FolderPicker folderPicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start);

		button = (Button) findViewById(R.id.folderbutton);
		button.setOnClickListener(this);
	}

	public void onClick(View v) {
		folderPicker = new FolderPicker(this, this, 0);
		folderPicker.show();
	}

	public void onClick(DialogInterface arg0, int arg1) {
		ConvenienceMethods.startScenarioFromFolder(folderPicker.getPath(), this,null);
	}
	
	

}