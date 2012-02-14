package at.mkw.android.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import model.Scenario;

import xml.XmlParser;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launcher extends Activity implements OnClickListener, DialogInterface.OnClickListener {
	private View button;
	private FolderPicker folderPicker;
	private String xmlPath;
	private File xmlFile,xsdFile;

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
		xmlPath = folderPicker.getPath();

		File workingDir = new File(xmlPath);
		
		
		xmlFile = new File(workingDir.getAbsolutePath() + "/" + workingDir.getName() + ".xml");

		if (!xmlFile.exists()) {
			ConvenienceMethods.showAlertDialog(this, "Couldn't find equally named xml file inside folder");
		}

		File[] xsdFiles = workingDir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (name.endsWith(".xsd")) {
					return true;
				}
				return false;
			}

		});

		if (xsdFiles.length != 1) {
			if (xsdFiles.length == 0) {
				ConvenienceMethods.showAlertDialog(this, "Couldn't find schema file inside folder");
			} else {
				ConvenienceMethods.showAlertDialog(this, "Only one xsd file allowed inside folder");
			}
		} else {
			xsdFile = xsdFiles[0];
		}
		
		if (xmlFile.exists() && xsdFile != null) {
			try {
				ConvenienceMethods.SZENARIO = XmlParser.parseFile(null, xmlFile);
				
				if (ConvenienceMethods.SZENARIO.getDevice().equals(Scenario.Device.MOBILE)) {
					ConvenienceMethods.IMAGE_DIR = xmlPath + "/";
					Intent newIntent = ConvenienceMethods.getNewIntent(ConvenienceMethods.SZENARIO.getStartScreen(), getApplicationContext());
					startActivity(newIntent);
				}
				else {
					ConvenienceMethods.showAlertDialog(this, "This scenario is not supposed to run on a mobile device");
				}
				
			} catch (FileNotFoundException ex) {
				ConvenienceMethods.showAlertDialog(this, "Couldn't find file " + xmlFile);
			} catch (Exception ex) {
				Log.e("tester",ex.getMessage(),ex);
				ConvenienceMethods.showAlertDialog(this, ex.getMessage());
			}
		}
	}
}