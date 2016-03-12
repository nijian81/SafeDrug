package cn.com.phhc.drugSafeApp.drugRemind;

/**
 * 添加药品页面中，选择间隔时间对话框
 */
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

/**
 * Created by lenovo on 2015/2/27.
 */
public class IntervalDialogFragment extends DialogFragment {

	ConfirmInterface listener;
	NumberPicker frequency;
	TextView cancel, save;

	public static IntervalDialogFragment newInstance(String sex) {
		IntervalDialogFragment frag = new IntervalDialogFragment();
		Bundle args = new Bundle();
		args.putString("sex", sex);
		frag.setArguments(args);
		return frag;
	}

	// 定制fragmentDialog弹出动作
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	public interface ConfirmInterface {
		public void onConfirmInterface(int time);
	}

	public void setConfirmInterface(ConfirmInterface listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.interval_dialog, container,
				false);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		int time = getArguments().getInt("num");
		frequency = (NumberPicker) rootView.findViewById(R.id.frequency);
		frequency.setMaxValue(24);
		frequency.setMinValue(1);
		frequency.setValue(time);
		Window window = getDialog().getWindow();
		window.setGravity(Gravity.BOTTOM);
		// 保存
		save = (TextView) rootView.findViewById(R.id.save);
		save.setOnClickListener(new MyClickSaveListener());
		// 取消
		cancel = (TextView) rootView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new MyClickCancelListener());

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	// 点击保存监视器 尼见 2015-03-03
	class MyClickSaveListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			listener.onConfirmInterface(frequency.getValue());
			dismiss();
		}
	}

	// 点击取消监视器 尼见 2015-03-03
	class MyClickCancelListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			dismiss();
		}
	}

}
