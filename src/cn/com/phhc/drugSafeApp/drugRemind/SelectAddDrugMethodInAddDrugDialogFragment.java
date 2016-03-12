package cn.com.phhc.drugSafeApp.drugRemind;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.Constant.Constant;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;

/**
 * Created by lenovo on 2015/2/27. 
 * 药品添加页面中的点击添加药品，弹出的选择添加药品方式提示框
 */
public class SelectAddDrugMethodInAddDrugDialogFragment extends DialogFragment {

	ConfirmInterface listener;
	RelativeLayout scan, manualAdd;
	String current_member_id;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName;

	public static SelectAddDrugMethodInAddDrugDialogFragment newInstance(
			String sex) {
		SelectAddDrugMethodInAddDrugDialogFragment frag = new SelectAddDrugMethodInAddDrugDialogFragment();
		Bundle args = new Bundle();
		args.putString("sex", sex);
		frag.setArguments(args);
		return frag;
	}

	public interface ConfirmInterface {
		public void onConfirmInterface(String method);
	}

	public void setConfirmInterface(ConfirmInterface listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.select_add_drug_method_dialog, container, false);

		db = databaseHelper.getWritableDatabase();
		String sql2 = "select current_member_id, current_member_portrait,current_member_name from anonymityRegister";
		Cursor cursor2 = db.rawQuery(sql2, null);
		while (cursor2.moveToNext()) {
			current_member_id = cursor2.getString(0);
		}
		// 扫一扫
		scan = (RelativeLayout) rootView.findViewById(R.id.scan);
		scan.setOnClickListener(new MyClickScanListener());
		// 手工添加
		manualAdd = (RelativeLayout) rootView.findViewById(R.id.manualAdd);
		manualAdd.setOnClickListener(new MyClickManualAddListener());
		Window window = getDialog().getWindow();
		// set "origin" to top left corner, so to speak
		window.setGravity(Gravity.TOP | Gravity.RIGHT);
		// 设置背景色为透明
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// after that, setting values for x and y works "naturally"
		WindowManager.LayoutParams params = window.getAttributes();
		params.x = 0;
		params.y = Constant.addDrugHeight;
		window.setAttributes(params);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

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

	// 点击扫一扫监视器 尼见 2015-03-03
	class MyClickScanListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			String method = "scan";
			listener.onConfirmInterface(method);
		}
	}

	// 点击手动添加监视器 尼见 2015-03-03
	class MyClickManualAddListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			String method = "manual";
			listener.onConfirmInterface(method);
			dismiss();
		}
	}

}
