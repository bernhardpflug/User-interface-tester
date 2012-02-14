package at.mkw.android.ui;

import model.ModelUtils;
import model.Scenario;
import model.Slider;
import model.Window;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
  
  public static void showAlertDialog(Context con, String msg) {
	  AlertDialog.Builder error = new AlertDialog.Builder(con);
		error.setMessage(msg);
		error.show();
  }
  
}
