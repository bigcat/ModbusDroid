package com.developerlife.Utils;

import android.view.*;
import android.widget.*;

/**
 * LayoutUtils is a helper class that makes it a lot easier to perform Android layouts in Java code,
 * without using XML.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jul 3, 2008, 11:59:37 AM
 */
public class LayoutUtils {

public enum Layout {
  WidthFill_HeightFill,
  WidthWrap_HeightWrap,
  WidthWrap_HeightFill,
  WidthFill_HeightWrap;

  public void applyViewGroupParams(View component) {
    applyViewGroupLayoutParamsTo(this, component);
  }

  public void applyLinearLayoutParams(View linearlayout) {
    applyLinearLayoutParamsTo(this, linearlayout);
  }

  public void applyTableRowParams(View cell) {
    applyTableRowLayoutParamsTo(this, cell);
  }

  public void applyTableLayoutParams(View row) {
    applyTableLayoutParamsTo(this, row);
  }

}

private static void applyLinearLayoutParamsTo(Layout layout, View view) {

  switch (layout) {
    case WidthFill_HeightFill:
      view.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.FILL_PARENT,
          LinearLayout.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthFill_HeightWrap:
      view.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.FILL_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
      ));
      break;
    case WidthWrap_HeightFill:
      view.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthWrap_HeightWrap:
      view.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
      ));
      break;
  }

}

private static void applyViewGroupLayoutParamsTo(Layout layout, View view) {

  switch (layout) {
    case WidthFill_HeightFill:
      view.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.FILL_PARENT,
          ViewGroup.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthFill_HeightWrap:
      view.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.FILL_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
      ));
      break;
    case WidthWrap_HeightFill:
      view.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthWrap_HeightWrap:
      view.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
      ));
      break;
  }

}

private static void applyTableRowLayoutParamsTo(Layout layout, View view) {

  switch (layout) {
    case WidthFill_HeightFill:
      view.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.FILL_PARENT,
          TableRow.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthFill_HeightWrap:
      view.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.FILL_PARENT,
          TableRow.LayoutParams.WRAP_CONTENT
      ));
      break;
    case WidthWrap_HeightFill:
      view.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.WRAP_CONTENT,
          TableRow.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthWrap_HeightWrap:
      view.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.WRAP_CONTENT,
          TableRow.LayoutParams.WRAP_CONTENT
      ));
      break;
  }

}

private static void applyTableLayoutParamsTo(Layout layout, View view) {

  switch (layout) {
    case WidthFill_HeightFill:
      view.setLayoutParams(new TableLayout.LayoutParams(
          TableLayout.LayoutParams.FILL_PARENT,
          TableLayout.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthFill_HeightWrap:
      view.setLayoutParams(new TableLayout.LayoutParams(
          TableLayout.LayoutParams.FILL_PARENT,
          TableLayout.LayoutParams.WRAP_CONTENT
      ));
      break;
    case WidthWrap_HeightFill:
      view.setLayoutParams(new TableLayout.LayoutParams(
          TableLayout.LayoutParams.WRAP_CONTENT,
          TableLayout.LayoutParams.FILL_PARENT
      ));
      break;
    case WidthWrap_HeightWrap:
      view.setLayoutParams(new TableLayout.LayoutParams(
          TableLayout.LayoutParams.WRAP_CONTENT,
          TableLayout.LayoutParams.WRAP_CONTENT
      ));
      break;
  }

}

}//end class LayoutUtils
