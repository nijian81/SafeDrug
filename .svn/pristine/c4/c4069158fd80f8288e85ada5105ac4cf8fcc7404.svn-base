package cn.com.phhc.drugSafeApp.drugRemind;

/**
 * 药品添加页面，输入药品名称查询页面
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.drugRemind.AddDrugFragment.GenerateTakeDrugTime;
import cn.com.phhc.drugSafeApp.utils.AddDrugItemAdapter;
import cn.com.phhc.drugSafeApp.utils.CharacterParser;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.DrugItem;
import cn.com.phhc.drugSafeApp.utils.PinyinComparator;

public class DrugNameQueryDialogFragment extends DialogFragment {

	ConfirmInterface listener;
	ImageView back;
	EditText input_search;
	TextView confirm;
	ListView listView;
	PinyinComparator pinyinComparator;
	CharacterParser characterParser;
	ArrayList<DrugItem> list;
	AddDrugItemAdapter addDrugItemAdapter;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName, drugName, drugNameParams, productDrugId, basicDrugId,
			basicDrugID_to_send, productDrugID_to_send, current_member_id;

	// 定制fragmentDialog弹出动作
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	public interface ConfirmInterface {
		public void onConfirmInterface(String drugName, String basicDrugID,
				String productDrugID);
	}

	public void setConfirmInterface(ConfirmInterface listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		databaseName = "drug_info";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.drug_name_query_fragment,
				container, false);

		drugNameParams = getArguments().getString("drugName");
		current_member_id = getArguments().getString("current_member_id");
		// 返回按钮
		back = (ImageView) rootView.findViewById(R.id.back);
		back.setOnClickListener(new MyClickBackListener());
		input_search = (EditText) rootView.findViewById(R.id.input_search);
		input_search.setText(drugNameParams);
		// 实现搜索框改变监听器
		input_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				if (s.toString().length() >= 2) {
					filterData(s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		String sql = "select showName, productDrugId, basicDrugId, commonPinyin, basicSpecifications from drugSearchTable where type = '1' and isValidData = '1' order by commonPinYin asc";
		db = databaseHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		list = new ArrayList<DrugItem>();
		while (cursor.moveToNext()) {
			drugName = cursor.getString(0);
			productDrugId = cursor.getString(1);
			basicDrugId = cursor.getString(2);
			String pinyin = cursor.getString(3);
			String desc = cursor.getString(4);
			list.add(new DrugItem(productDrugId, drugName, basicDrugId, "",
					pinyin, desc));
		}
		db.close();
		addDrugItemAdapter = new AddDrugItemAdapter();
		addDrugItemAdapter.setContext(getActivity());
		addDrugItemAdapter.setArrayList(list);
		Collections.sort(list, pinyinComparator);
		listView = (ListView) rootView.findViewById(R.id.list);
		listView.setAdapter(addDrugItemAdapter);
		listView.setVisibility(View.INVISIBLE);
		// 确认
		confirm = (TextView) rootView.findViewById(R.id.confirm);
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onConfirmInterface(input_search.getText().toString(),
						"", "");
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(getActivity().INPUT_METHOD_SERVICE);
				// 显示或者隐藏输入法
				imm.hideSoftInputFromWindow(input_search.getWindowToken(), 0);
				dismiss();
			}
		});
		// 点击listView监视器
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			//存放当前点击项的药品名称
			String drugNameClick = "";
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				DrugItem drugItem = (DrugItem) listView
						.getItemAtPosition(position);
				drugNameClick = drugItem.getDrugName();
				// 显示或者隐藏输入法
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(getActivity().INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(input_search.getWindowToken(), 0);

				databaseName = "LocalDrugMessage";
				databaseHelper = new CreateSQLiteDatabase(getActivity(),
						databaseName, null, 1);
				// 查询当前服药人
				db = databaseHelper.getWritableDatabase();
				// 当前日期
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				// 当前手机时间
				SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
				Date curDate = new Date(System.currentTimeMillis());
				String nowdate = formatter.format(curDate);
				String nowtime = formatter2.format(curDate);
				String sql = "select drugName from TakeTimeTable where memberID='"
						+ current_member_id + "' and  (date>'" + nowdate
						+ "' or (date='" + nowdate + "' and time>'" + nowtime
						+ "'))";
				Cursor cursor = db.rawQuery(sql, null);
				while(cursor.moveToNext()){
					String drugName = cursor.getString(0);
					if(drugName.equals(drugItem.getDrugName())){
						
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								AlertDialog dialog = getAlertDialogWithExistRepeatDrugName();
								dialog.show();
							}
						});
						
						return;
					}
				}
				
				listener.onConfirmInterface(drugItem.getDrugName(),
						drugItem.getBasicDrugId(), drugItem.getProductDrugId());
				dismiss();
			}

			// 提示用户在服药品重新生成用药时间点 尼见 2015-03-23
			AlertDialog getAlertDialogWithExistRepeatDrugName() {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity(),
						android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

				builder.setTitle("提示");
				builder.setMessage(drugNameClick+" 正在服用，不能重复添加，您可以去用药日程修改用药信息！");
				builder.setPositiveButton("确定", null);
				builder.setCancelable(false);
				android.app.AlertDialog dialog = builder.create();
				return dialog;
			}
		});

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

	// 点击返回药品添加页面监视器 尼见 2015-03-19
	class MyClickBackListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			// 显示或者隐藏输入法
			imm.hideSoftInputFromWindow(input_search.getWindowToken(), 0);
			dismiss();
		}
	}

	// 输入字符过滤药品监视器 尼见 2015-03-19
	private void filterData(String filterStr) {

		ArrayList<DrugItem> filterDateList = new ArrayList<DrugItem>();
		String upper = filterStr.toUpperCase();
		if (TextUtils.isEmpty(upper)) {
			filterDateList = list;
			addDrugItemAdapter.updateListView(filterDateList);
			listView.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (DrugItem drugItem : list) {
				if (drugItem.getPinyin().contains(upper)
						|| drugItem.getDrugName().contains(filterStr)) {
					filterDateList.add(drugItem);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		addDrugItemAdapter.updateListView(filterDateList);
		listView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStart() {

		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		super.onStart();
	}

}