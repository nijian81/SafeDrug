package cn.com.phhc.drugSafeApp.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class CustomNumberPicker extends NumberPicker {

	public CustomNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void addView(View child) {
		super.addView(child);      
		updateView(child);
	}
	public void addView(View child, int index,android.view.ViewGroup.LayoutParams params){
		super.addView(child, index, params);
		updateView(child);  
	}
	public void addView(View child, android.view.ViewGroup.LayoutParams params){
		super.addView(child, params);
		updateView(child);
	}
	
	private void updateView(View view) {
		// TODO Auto-generated method stub
		if (view instanceof EditText) {
			((EditText) view).setTextSize(14);
			((EditText) view).setTextColor(Color.BLACK); 
			((EditText) view).setEllipsize(TruncateAt.END);
			((EditText) view).setLines(1);
		}      
		     
	}

}
