package at.mkw.android.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import model.ModelUtils;
import model.Scenario;
import model.Screen;
import model.Slider;
import model.Window;
import xml.XmlParser;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ConvenienceMethods {
  public static String CUR_ID = "aktID";
  public static String IMAGE_DIR = "//sdcard//imgs//";
  
  public static Scenario SZENARIO;
  
  public static void set_Image_Dir(String newPath) {
    IMAGE_DIR = newPath;
  }
  
  public static Intent getNewIntent(Window window, Context appContext) {
    Intent newIntent = null;
    Bundle b = new Bundle();
    b.putString(CUR_ID, window.getFullId());
    
    Slider slider = ModelUtils.getSlider(window);
    
    //if screen is not part of a slider
    if (slider == null) {
    	newIntent = new Intent(appContext, SimpleScreenActivity.class);
    }
    else {
    	newIntent = new Intent(appContext, SliderActivity.class);
    }
    
    newIntent.putExtras(b);    
    return newIntent;
  }
  
  public static void startScenarioFromFolder(String folder, Context context, String targetName){
		String xmlPath = folder;

		File workingDir = new File(xmlPath);
		File xsdFile = null;
		File xmlFile = new File(workingDir.getAbsolutePath() + "/" + workingDir.getName() + ".xml");

		if (!xmlFile.exists()) {
			ConvenienceMethods.showAlertDialog(context, "Couldn't find equally named xml file inside folder");
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
				ConvenienceMethods.showAlertDialog(context, "Couldn't find schema file inside folder");
			} else {
				ConvenienceMethods.showAlertDialog(context, "Only one xsd file allowed inside folder");
			}
		} else {
			xsdFile = xsdFiles[0];
		}
		
		if (xmlFile.exists() && xsdFile != null) {
			try {
				ConvenienceMethods.SZENARIO = XmlParser.parseFile(null, xmlFile);
				
				if (ConvenienceMethods.SZENARIO.getDevice().equals(Scenario.Device.MOBILE)) {
					ConvenienceMethods.IMAGE_DIR = xmlPath + "/";
					
					//if certain target is specified to be launched do it
					if (targetName != null) {
						Screen screen = ConvenienceMethods.SZENARIO.getScreen(targetName);
						
						if (screen == null) {
							throw new RuntimeException("Couldn't find target for "+targetName);
						}
						Intent newIntent = ConvenienceMethods.getNewIntent(screen, context);
						context.startActivity(newIntent);
					}
					//else load start screen
					else {
						Intent newIntent = ConvenienceMethods.getNewIntent(ConvenienceMethods.SZENARIO.getStartScreen(), context);
						context.startActivity(newIntent);
					}
				}
				else {
					ConvenienceMethods.showAlertDialog(context, "This scenario is not supposed to run on a mobile device");
				}
				
			} catch (FileNotFoundException ex) {
				ConvenienceMethods.showAlertDialog(context, "Couldn't find file " + xmlFile);
			} catch (Exception ex) {
				Log.e("tester",ex.getMessage(),ex);
				ConvenienceMethods.showAlertDialog(context, ex.getMessage());
			}
		}
	}
  
  public static void showAlertDialog(Context con, String msg) {
	  AlertDialog.Builder error = new AlertDialog.Builder(con);
		error.setMessage(msg);
		error.show();
  }
  
}
