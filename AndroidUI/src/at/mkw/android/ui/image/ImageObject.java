package at.mkw.android.ui.image;

import android.graphics.Bitmap;

public class ImageObject {

  public Bitmap image;
  public float xScale, yScale;
  
  public ImageObject(Bitmap image, float xScale, float yScale) {
    super();
    this.image = image;
    this.xScale = xScale;
    this.yScale = yScale;
  }
  
}
