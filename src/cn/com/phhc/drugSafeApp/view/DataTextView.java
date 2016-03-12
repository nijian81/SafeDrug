package cn.com.phhc.drugSafeApp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class DataTextView extends TextView{
	
	
	public DataTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.rotate(-36, getMeasuredHeight()/2, getMeasuredWidth()/2);
		
		super.onDraw(canvas);
		
	}
	
}
