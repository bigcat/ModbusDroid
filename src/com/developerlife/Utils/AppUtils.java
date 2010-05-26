package com.developerlife.Utils;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.widget.*;

/**
 * AppUtils is a helper class that makes it easy to perform frequently used tasks in Android development.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jul 8, 2008, 2:35:39 PM
 */
public class AppUtils {

/** 127.0.0.1 in the emulator points back to itself. Use this if you want to access your host OS */
public static String EmulatorLocalhost = "10.0.2.2";

/** shows a short message on top of your app... it goes away automatically after a short delay */
public static void showToastShort(Activity a, String msg) {
  Toast.makeText(a,
                 msg,
                 Toast.LENGTH_SHORT).show();
}

/** shows a short message on top of your app... it goes away automatically after a long delay */
public static void showToastLong(Activity a, String msg) {
  Toast.makeText(a,
                 msg,
                 Toast.LENGTH_LONG).show();
}

/**
 * create an image view, given a drawable. you can set the max size of this imageview as well.
 *
 * @param iconWidth  -1 means dont set this
 * @param iconHeight -1 means dont set this
 * @param imageRes   -1 means dont set this
 */
public static ImageView createImageView(Context activity, int iconWidth, int iconHeight, int imageRes) {
  ImageView icon = new ImageView(activity);
  icon.setAdjustViewBounds(true);
  icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

  if (iconHeight != -1) icon.setMaxHeight(iconHeight);
  if (iconWidth != -1) icon.setMaxWidth(iconWidth);

  if (imageRes != -1) icon.setImageResource(imageRes);
  return icon;
}

/** simply resizes a given drawable resource to the given width and height */
public static Drawable resizeImage(Context ctx,
                                   int resId,
                                   int iconWidth,
                                   int iconHeight)
{

  // load the origial Bitmap
  Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),
                                                  resId);

  int width = BitmapOrg.getWidth();
  int height = BitmapOrg.getHeight();
  int newWidth = iconWidth;
  int newHeight = iconHeight;

  // calculate the scale
  float scaleWidth = ((float) newWidth) / width;
  float scaleHeight = ((float) newHeight) / height;

  // create a matrix for the manipulation
  Matrix matrix = new Matrix();
  // resize the Bitmap
  matrix.postScale(scaleWidth, scaleHeight);

  // if you want to rotate the Bitmap
  // matrix.postRotate(45);

  // recreate the new Bitmap
  Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                                             width, height, matrix, true);

  // make a Drawable from Bitmap to allow to set the Bitmap
  // to the ImageView, ImageButton or what ever
  return new BitmapDrawable(resizedBitmap);

}

}//end class AppUtils
