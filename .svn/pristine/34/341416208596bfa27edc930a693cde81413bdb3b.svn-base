package cn.com.phhc.drugSafeApp.drugRemind;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.Constant.Constant;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.GetMd5;

/**
 * Created by lenovo on 2015/2/27. 用药提醒首页中的点击添加药品，弹出的选择添加药品方式提示框
 */
public class SelectAddDrugMethodDialogFragment extends DialogFragment {

	ConfirmInterface listener;
	RelativeLayout scan, manualAdd;
	String current_member_id;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName;

	public static SelectAddDrugMethodDialogFragment newInstance(String sex) {
		SelectAddDrugMethodDialogFragment frag = new SelectAddDrugMethodDialogFragment();
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
		params.y = Constant.height;
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
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
					"QR_CODE_MODE");
			startActivityForResult(intent, 0);
		}
	}

	// 点击手动添加监视器 尼见 2015-03-03
	class MyClickManualAddListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			AddDrugFragment addDrugFragment = new AddDrugFragment();
			Bundle bundle = new Bundle();
			bundle.putString("drugName", "");
			bundle.putString("current_member_id", current_member_id);
			addDrugFragment.setArguments(bundle);
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,
					R.anim.exit_to_left);
			fragmentTransaction.replace(R.id.login_fragment, addDrugFragment,
					"addDrugFragment");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			dismiss();
		}
	}

	// 处理扫码返回的数据 尼见 2015-05-03
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == 0) {
			if (resultCode == -1) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// ToastHelper.toastLong(getActivity(), "contents: " + contents
				// + " format: " + format);
				String barcode = GetMd5.stringMD5(contents);
				String productDrugID = "";
				String showName = "";
				databaseName = "drug_info";
				databaseHelper = new CreateSQLiteDatabase(getActivity(),
						databaseName, null, 1);
				db = databaseHelper.getWritableDatabase();
				// 查询条形码对应的productID，注意条码数据最后四位有四个空格，负责无法识别!!!
				String sql = "select productDrugID from barCodeTable where barcode = '"
						+ barcode + "    '";
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor.getCount() == 0) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AlertDialog dialog = getAlertDialogWithNotFoundDrugInfo();
							dialog.show();
						}
					});

					return;
				}
				while (cursor.moveToNext()) {
					productDrugID = cursor.getString(0);
				}
				// 查询productID对应的basicDrugID
				String sql2 = "select showName, basicDrugID FROM drugSearchTable where productDrugID = '"
						+ productDrugID + "'";
				Cursor cursor2 = db.rawQuery(sql2, null);
				if (cursor2.getCount() == 0) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AlertDialog dialog = getAlertDialogWithNotFoundDrugInfo();
							dialog.show();
						}
					});

					return;
				}
				String basicDrugID_from_search = "";
				String productDrugID_from_search = "";
				while (cursor2.moveToNext()) {
					showName = cursor2.getString(0);
					basicDrugID_from_search = cursor2.getString(1);
					productDrugID_from_search = productDrugID;
				}

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				AddDrugFragment addDrugFragment = new AddDrugFragment();
				Bundle bundle = new Bundle();
				bundle.putString("basicDrugID", basicDrugID_from_search);
				bundle.putString("productDrugID", productDrugID_from_search);
				bundle.putString("drugName", showName);
				bundle.putString("current_member_id", current_member_id);
				addDrugFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						addDrugFragment, "addDrugFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				dismiss();
				// Handle successful scan
			} else if (resultCode == 0) {
				// Handle cancel
			}
		}

	}

	// 药品信息未识别提示框 尼见 2015-05-01
	AlertDialog getAlertDialogWithNotFoundDrugInfo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("没有药品数据。");
		builder.setPositiveButton("确定", null);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

}
