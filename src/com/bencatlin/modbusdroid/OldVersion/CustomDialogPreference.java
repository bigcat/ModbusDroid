package com.bencatlin.modbusdroid.OldVersion;

import com.bencatlin.modbusdroid.R;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomDialogPreference extends DialogPreference {
	
	private TextView companyInfoTextBox;
	private Context mContext;

	public CustomDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.company_info_layout);
		mContext = context;
	}
	
	@Override
	protected void onBindDialogView (View view) {
		super.onBindDialogView(view);
		
		companyInfoTextBox = (TextView) view.findViewById(R.id.company_info_textview);
		//This sets the dialog text to a string resource formatted as HTML
		companyInfoTextBox.setText(Html.fromHtml(mContext.getString(R.string.company_info_text) ) );
		/*companyInfoTextBox.setText(Html.fromHtml("<h2>Brought to you by BigCat Inc.</h2><br />" + 
		"<p>For more information please check out <a href=\"http://www.bencatlin.com/sofware-projects/modbus-droid/\">BenCatlin.com/software-projects/modbus-droid</a>.</p> <br /><p>This software is based on the Modbus library <a href=\"http://sourceforge.net/projects/modbus4j/\">Modbus4J</a>, created by <a href=\"http://www.serotoninsoftware.com/\">Seretonin software</a>.</p><br />" +
		"<p>This app is also open source, licensed under the <a href=\"http://www.gnu.org/licenses/gpl-3.0.html\">GPL version 3</a>, and its source code is hosted and available at <a href=\"http://launchpad.net/modbusdroid/\">Launchpad.net/modbusdroid</a>.  Source code hosting will eventually be available on github.  Watch our main site for the announcement of when that takes place.</p><br />") );*/
	}
}