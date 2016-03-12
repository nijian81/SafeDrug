package cn.com.phhc.drugSafeApp.drugQuery;

/**
 * 药品查询主页面
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.DrugItem;
import cn.com.phhc.drugSafeApp.utils.DrugItemAdapter;
import cn.com.phhc.drugSafeApp.utils.GetMd5;
import cn.com.phhc.drugSafeApp.utils.SideBar;

public class DrugQueryFragment extends Fragment {

	ProgressDialog progressDialog;
	DrugDetailQueryFragment drugDetailQueryFragment;
	ImageView search;
	SideBar alpha;
	TextView dialog_center, scan;
	DrugDetailFragment drugDetailFragment;
	ArrayList<DrugItem> list;
	DrugItemAdapter drugItemAdapter;
	ListView listView;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName, drugName, productDrugId, basicDrugId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		databaseName = "drug_info";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.drug_query_fragment,
				container, false);

		// 二维码按钮
		scan = (TextView) rootView.findViewById(R.id.scan);
		scan.setOnClickListener(new MyClickOpenBarcodeScannerListener());
		// 字母导航条按钮
		alpha = (SideBar) rootView.findViewById(R.id.alpha);
		alpha.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = drugItemAdapter.getPositionForSection(s
						.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}
			}
		});
		search = (ImageView) rootView.findViewById(R.id.search);
		search.setOnClickListener(new MyClickSearchListener());
		listView = (ListView) rootView.findViewById(R.id.list);
		// 设置点击监视器
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				DrugItem drugItem = (DrugItem) listView
						.getItemAtPosition(position);
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				drugDetailFragment = new DrugDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString("productDrugId", drugItem.getProductDrugId());
				bundle.putString("basicDrugId", drugItem.getBasicDrugId());
				bundle.putString("drugName", drugItem.getDrugName());
				drugDetailFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						drugDetailFragment, "drugDetailFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}

		});
		// 将外部数据库导入到手机中
		File file = new File("/data/data/cn.com.phhc.drugSafeApp/databases/",
				"drug_info");
		list = new ArrayList<DrugItem>();
		if (!file.exists()) {
			try {
				InputStream in = getResources()
						.openRawResource(R.raw.drug_info);
				FileOutputStream out = new FileOutputStream(file);
				int n;
				byte a[] = new byte[1024];
				while ((n = in.read(a)) != -1) {
					out.write(a, 0, n);
				}
				out.close();
				in.close();
			} catch (IOException exp) {
			}
		}
		// 从数据库中查询数据
		db = databaseHelper.getWritableDatabase();
		String sql = "select showName, firstWord, productDrugId,basicDrugId from drugSearchTable where type = '1' and firstWord not like '2' and firstWord not like '3' and firstWord not like '5' order by commonPinYin";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			drugName = cursor.getString(0);
			String letter = cursor.getString(1);
			productDrugId = cursor.getString(2);
			basicDrugId = cursor.getString(3);
			list.add(new DrugItem(productDrugId, drugName, basicDrugId, letter));
		}
		drugItemAdapter = new DrugItemAdapter();
		drugItemAdapter.setContext(getActivity());
		drugItemAdapter.setArrayList(list);
		listView.setAdapter(drugItemAdapter);
		db.close();
		dialog_center = (TextView) rootView.findViewById(R.id.dialog_center);
		alpha = (SideBar) rootView.findViewById(R.id.alpha);

		return rootView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	// 点击搜索监视器 尼见 2015-03-10
	class MyClickSearchListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			drugDetailQueryFragment = new DrugDetailQueryFragment();
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_down,
					R.anim.exit_to_up);
			fragmentTransaction.replace(R.id.login_fragment,
					drugDetailQueryFragment, "drugDetailQueryFragment");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	// 点击二维码监视器 尼见 2015-03-17
	class MyClickOpenBarcodeScannerListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
					"QR_CODE_MODE");
			startActivityForResult(intent, 0);

		}

	}

	//处理扫描返回的结果 尼见 2015-05-03
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
				String basicDrugID = "";
				String drugName = "";
				db = databaseHelper.getWritableDatabase();
				// 查询条形码对应的productID
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
				String sql2 = "select basicDrugID, showName FROM drugSearchTable where productDrugID = '"
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
				while (cursor2.moveToNext()) {
					basicDrugID = cursor2.getString(0);
					drugName = cursor2.getString(1);
				}
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				drugDetailFragment = new DrugDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString("productDrugId", productDrugID);
				bundle.putString("basicDrugId", basicDrugID);
				bundle.putString("drugName", drugName);
				drugDetailFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						drugDetailFragment, "drugDetailFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
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
		builder.setMessage("未识别的药品条码信息，请确认");
		builder.setPositiveButton("确定", null);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

}