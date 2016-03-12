package cn.com.phhc.drugSafeApp.drugRemind;

/**
 * 添加药品页面
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.Constant.Constant;
import cn.com.phhc.drugSafeApp.drugQuery.DrugDetailFragment;
import cn.com.phhc.drugSafeApp.myAccount.InputBirthdayDialogFragment;
import cn.com.phhc.drugSafeApp.util.ToastHelper;
import cn.com.phhc.drugSafeApp.utils.AddDrugInfo;
import cn.com.phhc.drugSafeApp.utils.App;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.DrugImageItem;
import cn.com.phhc.drugSafeApp.utils.DrugImageItemAdapter;
import cn.com.phhc.drugSafeApp.utils.GenerateTakeDrugTimeList;
import cn.com.phhc.drugSafeApp.utils.GetMd5;
import cn.com.phhc.drugSafeApp.utils.HorizontalListView;
import cn.com.phhc.drugSafeApp.utils.TakeTimeList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDrugFragment extends Fragment implements Serializable {

	SelectAddDrugMethodInAddDrugDialogFragment selectAddDrugMethodInAddDrugDialogFragment;
	ProgressDialog progressDialog;
	TextView input_drug_name, drugFrequencyTextView, drugDoseTextView,
			begin_time_text_view, end_time_text_view, interval_text_view,
			nextStep, currentPeople;
	ArrayList<DrugImageItem> list;
	// 存放本页面添加的药品信息，位于内存中
	ArrayList<AddDrugInfo> listAddDrugInfo;
	ImageView back, addDrug;
	HorizontalListView drug;
	RelativeLayout drugName, drugFrequency, drugDose, begin_time, end_time,
			interval, delete;
	ScrollView scroll_view_tab;
	EditText remark;
	DrugImageItemAdapter drugImageItemAdapter;
	String localTimeStamp, sixDayLaterDateTimeStamp,
			current_member_portrait = null, current_member_name = null;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName, current_member_id, basicDrugID_from_search = "",
			productDrugID_from_search = "", drugDose_from_dialog,
			drugDoseUnit_from_dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.add_drug_fragment, container,
				false);

		// 接收到的参数
		String drugNameParams = getArguments().getString("drugName");
		current_member_id = getArguments().getString("current_member_id");
		productDrugID_from_search = getArguments().getString("productDrugID");
		basicDrugID_from_search = getArguments().getString("basicDrugID");
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		// 查询当前服药人
		db = databaseHelper.getWritableDatabase();
		String sql = "select current_member_id, current_member_portrait,current_member_name from anonymityRegister";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			current_member_id = cursor.getString(0);
			current_member_portrait = cursor.getString(1);
			current_member_name = cursor.getString(2);
		}
		// 当前用药人
		currentPeople = (TextView) rootView.findViewById(R.id.currentPeople);
		if (current_member_name.length() == 0) {
			currentPeople.setText("自己");
		} else {
			currentPeople.setText(current_member_name);
		}
		Drawable drawable = null;
		if (current_member_portrait.length() == 0) {
			drawable = getResources().getDrawable(R.drawable.tx4);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
		} else {

			switch (current_member_portrait) {
			case "tx1":
				drawable = getResources().getDrawable(R.drawable.tx1);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx2":
				drawable = getResources().getDrawable(R.drawable.tx2);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx3":
				drawable = getResources().getDrawable(R.drawable.tx3);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx4":
				drawable = getResources().getDrawable(R.drawable.tx4);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx5":
				drawable = getResources().getDrawable(R.drawable.tx5);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx6":
				drawable = getResources().getDrawable(R.drawable.tx6);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx7":
				drawable = getResources().getDrawable(R.drawable.tx7);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			case "tx8":
				drawable = getResources().getDrawable(R.drawable.tx8);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			default:
				drawable = getResources().getDrawable(R.drawable.tx4);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				break;
			}
		}
		Drawable drawable2 = getResources().getDrawable(
				R.drawable.head_name_image);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
				drawable2.getMinimumHeight());
		currentPeople.setCompoundDrawables(null, drawable, drawable2, null);

		// 下一步
		nextStep = (TextView) rootView.findViewById(R.id.nextStep);
		nextStep.setOnClickListener(new MyClickNextStepListener());
		// 存放添加药品信息的数组
		listAddDrugInfo = new ArrayList<>();
		// 药品名称
		input_drug_name = (TextView) rootView
				.findViewById(R.id.input_drug_name);
		if (drugNameParams.length() != 0) {
			input_drug_name.setText(drugNameParams);
		}
		// 返回按钮
		back = (ImageView) rootView.findViewById(R.id.back);
		back.setOnClickListener(new MyClickBackListener());
		// 药品图片listView
		drug = (HorizontalListView) rootView.findViewById(R.id.drug);
		// 输入药品名称
		drugName = (RelativeLayout) rootView.findViewById(R.id.drugName);
		if (drugNameParams.length() != 0) {
			input_drug_name.setVisibility(View.VISIBLE);
			input_drug_name.setText(drugNameParams);
		}
		drugName.setOnClickListener(new MyClickInputDrugNameListener());
		list = new ArrayList<DrugImageItem>();
		// “1”代表已选中，“0”代表未选中
		list.add(new DrugImageItem("1"));
		drugImageItemAdapter = new DrugImageItemAdapter();
		drugImageItemAdapter.setContext(getActivity());
		drugImageItemAdapter.setArrayList(list);
		drug.setAdapter(drugImageItemAdapter);
		// 设置点击监视器
		drug.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				DrugImageItem drugImageItem = (DrugImageItem) listView
						.getItemAtPosition(position);

				int size = list.size();
				for (int i = 0; i < size; i++) {
					if (i == position) {
						list.get(position).setFlag("1");
						continue;
					}
					list.get(i).setFlag("0");
				}
				DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
				drugImageItemAdapter.setContext(getActivity());
				drugImageItemAdapter.setArrayList(list);
				drug.setAdapter(drugImageItemAdapter);
				if (listAddDrugInfo.size() == position) {
					input_drug_name.setText("");
					input_drug_name.setHint("请输入药品名称");
					drugFrequencyTextView.setText("");
					drugFrequencyTextView.setHint("请选择");
					drugDoseTextView.setText("");
					drugDoseTextView.setHint("请选择");
					begin_time_text_view.setText(localTimeStamp);
					end_time_text_view.setText(sixDayLaterDateTimeStamp);
					interval_text_view.setText("");
					interval_text_view.setHint("请选择");
					remark.setText("");
					return;
				}
				input_drug_name.setText(listAddDrugInfo.get(position)
						.getDrugName());
				drugFrequencyTextView.setText(listAddDrugInfo.get(position)
						.getCycleCount());
				drugDoseTextView.setText(listAddDrugInfo.get(position)
						.getDrugCount());
				begin_time_text_view.setText(listAddDrugInfo.get(position)
						.getBegin());
				end_time_text_view.setText(listAddDrugInfo.get(position)
						.getEnd());
				interval_text_view.setText(listAddDrugInfo.get(position)
						.getIntervalValue());
				remark.setText(listAddDrugInfo.get(position).getRemark());
			}
		});
		// 用药频率
		drugFrequency = (RelativeLayout) rootView
				.findViewById(R.id.drugFrequency);
		drugFrequency.setOnClickListener(new MyClickDrugFrequencyListener());
		drugFrequencyTextView = (TextView) rootView
				.findViewById(R.id.drugFrequencyTextView);
		// 用药剂量
		drugDose = (RelativeLayout) rootView.findViewById(R.id.drugDose);
		drugDose.setOnClickListener(new MyClickDrugDoseListener());
		drugDoseTextView = (TextView) rootView
				.findViewById(R.id.drugDoseTextView);
		// 开始日期
		begin_time = (RelativeLayout) rootView.findViewById(R.id.begin_time);
		begin_time.setOnClickListener(new MyClickBeginTimeListener());
		begin_time_text_view = (TextView) rootView
				.findViewById(R.id.begin_time_text_view);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		localTimeStamp = formatter.format(curDate);
		begin_time_text_view.setText(localTimeStamp);
		// 结束日期
		end_time = (RelativeLayout) rootView.findViewById(R.id.end_time);
		end_time.setOnClickListener(new MyClickEndTimeListener());
		end_time_text_view = (TextView) rootView
				.findViewById(R.id.end_time_text_view);
		long sixDayLater = System.currentTimeMillis() + 86400000 * 6;
		Date sixDayLaterDate = new Date(sixDayLater);
		sixDayLaterDateTimeStamp = formatter.format(sixDayLaterDate);
		end_time_text_view.setText(sixDayLaterDateTimeStamp);
		// 间隔
		interval = (RelativeLayout) rootView.findViewById(R.id.interval);
		interval.setOnClickListener(new MyClickIntervalListener());
		interval_text_view = (TextView) rootView
				.findViewById(R.id.interval_text_view);
		// 删除
		delete = (RelativeLayout) rootView.findViewById(R.id.delete);
		delete.setOnClickListener(new MyClickDeleteListener());
		// 添加按钮
		addDrug = (ImageView) rootView.findViewById(R.id.addDrug);
		addDrug.setOnClickListener(new MyClickAddDrugListener());
		// ScrollView
		scroll_view_tab = (ScrollView) rootView
				.findViewById(R.id.scroll_view_tab);
		// 备注
		remark = (EditText) rootView.findViewById(R.id.remark);
		db.close();

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

		Constant.addDrugHeight = getResources().getDimensionPixelSize(
				R.dimen.xmlTopTitleHeightAndMiddle);
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	// 点击下一步监视器 尼见 2015-03-24
	class MyClickNextStepListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// 将最后一次添加的药品信息存取内存中的实体内
			AddDrugInfo addDrugInfo = new AddDrugInfo();
			addDrugInfo.setBasicDrugID(basicDrugID_from_search);
			addDrugInfo.setProductDrugID(productDrugID_from_search);
			addDrugInfo.setDrugName(input_drug_name.getText().toString());
			addDrugInfo.setCycleDays("1");
			String takeDrugFrequency = drugFrequencyTextView.getText()
					.toString();
			String regEx = "[^0-9]";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(takeDrugFrequency);
			String takeDrugFrequencyNum = matcher.replaceAll("").trim();
			addDrugInfo.setCycleCount(takeDrugFrequencyNum);
			String takeDrugInterval = interval_text_view.getText().toString();
			matcher = pattern.matcher(takeDrugInterval);
			String takeDrugIntervalNum = matcher.replaceAll("").trim();
			if (takeDrugIntervalNum.length() == 0) {
				addDrugInfo.setIntervalValue("0");
			} else {
				// 服药时间间隔的单位已经改成了分钟，例如当选择“1”时候，表示“60分钟”
				int hour = Integer.parseInt(takeDrugIntervalNum);
				int minute = hour * 60;
				addDrugInfo.setIntervalValue(minute + "");
			}
			addDrugInfo.setIntervalUnit("");
			addDrugInfo.setBegin(begin_time_text_view.getText().toString());
			addDrugInfo.setEnd(end_time_text_view.getText().toString());
			addDrugInfo.setDrugCount(drugDose_from_dialog);
			databaseName = "drug_info";
			databaseHelper = new CreateSQLiteDatabase(getActivity(),
					databaseName, null, 1);
			db = databaseHelper.getWritableDatabase();
			String sql = "select doseUnitID from drugDoseUnitTable where doseUnit ='"
					+ drugDoseUnit_from_dialog + "'";
			Cursor cursor = db.rawQuery(sql, null);
			String drugDoseUnitID = "";
			while (cursor.moveToNext()) {
				drugDoseUnitID = cursor.getString(0);
			}
			addDrugInfo.setDrugCountUnit(drugDoseUnitID);
			addDrugInfo.setRemark(remark.getText().toString());
			listAddDrugInfo.add(addDrugInfo);
			cursor.close();
			db.close();
			// 打开添加药品确认信息
			ConfirmAddDrugInfoDialogFragment confirmAddDrugInfoDialogFragment = new ConfirmAddDrugInfoDialogFragment();
			confirmAddDrugInfoDialogFragment
					.setConfirmInterface(new ConfirmAddDrugInfoDialogFragment.ConfirmInterface() {
						@Override
						public void onConfirmInterface(String left) {

							databaseName = "LocalDrugMessage";
							databaseHelper = new CreateSQLiteDatabase(
									getActivity(), databaseName, null, 1);
							// 查询当前服药人
							db = databaseHelper.getWritableDatabase();
							// 当前日期
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd");
							// 当前手机时间
							SimpleDateFormat formatter2 = new SimpleDateFormat(
									"HH:mm");
							Date curDate = new Date(System.currentTimeMillis());
							String nowdate = formatter.format(curDate);
							String nowtime = formatter2.format(curDate);
							String sql = "select * from TakeTimeTable where memberID='"
									+ current_member_id
									+ "' and  (date>'"
									+ nowdate
									+ "' or (date='"
									+ nowdate
									+ "' and time>'" + nowtime + "'))";
							Cursor cursor = db.rawQuery(sql, null);
							if (cursor.getCount() != 0) {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										AlertDialog dialog = getAlertDialogWithAgainGenerateFormerTakeDrugTime();
										dialog.show();
									}
								});
								return ;
							}
							progressDialog = ProgressDialog.show(getActivity(),
									null, "数据加载中，请稍后...");
							progressDialog.setCancelable(true);
							new GenerateTakeDrugTime().execute();
						}
					});
			Bundle bundle = new Bundle();
			bundle.putCharSequenceArrayList("list", (ArrayList) listAddDrugInfo);
			confirmAddDrugInfoDialogFragment.setArguments(bundle);
			confirmAddDrugInfoDialogFragment.show(getActivity()
					.getFragmentManager(), "confirmAddDrugInfoDialogFragment");

		}

		// 提示用户在服药品重新生成用药时间点 尼见 2015-03-23
		AlertDialog getAlertDialogWithAgainGenerateFormerTakeDrugTime() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("为了用药安全，以往在服的药品将会重新生成用药时间");
			builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					progressDialog = ProgressDialog.show(getActivity(), null,
							"数据加载中，请稍后...");
					progressDialog.setCancelable(true);
					new GenerateTakeDrugTime().execute();
				}
			});
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}
	}

	// 点击返回监视器 尼见 2015-03-19
	class MyClickBackListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// 添加的药品将会清除提示框
			getActivity().runOnUiThread(new Runnable() {
				public void run() {

					AlertDialog dialog = getAlertDialogWithAddDrugWillDisappear();
					dialog.show();

				}
			});

		}

		// 添加药品将会消失提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithAddDrugWillDisappear() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("添加的药品将会清除");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentManager fragmentManager = getActivity()
									.getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							DrugRemindFragment drugRemindFragment = new DrugRemindFragment();
							fragmentTransaction.setCustomAnimations(
									R.anim.enter_from_left,
									R.anim.exit_to_right);
							fragmentTransaction.replace(R.id.login_fragment,
									drugRemindFragment, "drugRemindFragment");
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}
	}

	// 点击输入药品名称监视器 尼见 2015-03-19
	class MyClickInputDrugNameListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// 弹出dialogFragment去传值
			DrugNameQueryDialogFragment drugNameQueryDialogFragment = new DrugNameQueryDialogFragment();
			drugNameQueryDialogFragment
					.setConfirmInterface(new DrugNameQueryDialogFragment.ConfirmInterface() {
						@Override
						public void onConfirmInterface(String drugName,
								String basicDrugID, String productDrugID) {
							if (drugName.equals(input_drug_name.getText()
									.toString())) {
							} else {
								basicDrugID_from_search = basicDrugID;
								productDrugID_from_search = productDrugID;
								input_drug_name.setText(drugName);
								input_drug_name.setHint("请输入药品名称");
								drugFrequencyTextView.setText("");
								drugFrequencyTextView.setHint("请选择");
								drugDoseTextView.setText("");
								drugDoseTextView.setHint("请选择");
								begin_time_text_view.setText(localTimeStamp);
								end_time_text_view
										.setText(sixDayLaterDateTimeStamp);
								interval_text_view.setText("");
								interval_text_view.setHint("请选择");
								remark.setText("");
							}
						}
					});
			Bundle bundle = new Bundle();
			bundle.putString("drugName", input_drug_name.getText().toString());
			bundle.putString("current_member_id",current_member_id); 
			drugNameQueryDialogFragment.setArguments(bundle);
			drugNameQueryDialogFragment.show(getFragmentManager(),
					"drugNameQueryDialogFragment");
		}
	}

	// 点击用药频率监视器 尼见 2015-03-19
	class MyClickDrugFrequencyListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			DrugFrequencyDialogFragment drugFrequencyDialogFragment = new DrugFrequencyDialogFragment();
			Bundle bundle = new Bundle();
			String frequencyParams = drugFrequencyTextView.getText().toString();
			if (frequencyParams.length() == 0) {
				bundle.putInt("num", 1);
			} else {
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(frequencyParams);
				int time = Integer.parseInt(m.replaceAll("").trim());
				bundle.putInt("num", time);
			}
			drugFrequencyDialogFragment.setArguments(bundle);
			drugFrequencyDialogFragment
					.setConfirmInterface(new DrugFrequencyDialogFragment.ConfirmInterface() {

						@Override
						public void onConfirmInterface(int time) {
							drugFrequencyTextView.setText(time + "次");
						}
					});
			drugFrequencyDialogFragment.show(
					getActivity().getFragmentManager(),
					"drugFrequencyDialogFragment");
		}
	}

	// 点击用药剂量监视器 尼见 2015-03-19
	class MyClickDrugDoseListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			DrugDoseDialogFragment drugDoseDialogFragment = new DrugDoseDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putString("drugDose", drugDoseTextView.getText().toString());
			bundle.putString("productDrugID", productDrugID_from_search);
			drugDoseDialogFragment.setArguments(bundle);
			drugDoseDialogFragment
					.setConfirmInterface(new DrugDoseDialogFragment.ConfirmInterface() {

						@Override
						public void onConfirmInterface(String left, String right) {
							drugDoseTextView.setText(left + " " + right);
							drugDose_from_dialog = left;
							drugDoseUnit_from_dialog = right;
						}
					});
			drugDoseDialogFragment.show(getActivity().getFragmentManager(),
					"drugDoseDialogFragment");
		}
	}

	// 点击开始日期监视器 尼见 2015-03-20
	class MyClickBeginTimeListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			InputBirthdayDialogFragment inputBirthdayDialogFragment = InputBirthdayDialogFragment
					.newInstance(0, 0, 0);
			inputBirthdayDialogFragment
					.setConfirmInterface(new InputBirthdayDialogFragment.ConfirmInterface() {

						// 接口回调实现通信
						@Override
						public void onConfirmInterface(int year, int month,
								int day) {
							String month_show;
							String day_show;
							if (month <= 9) {
								month_show = "0" + month;
							} else {
								month_show = Integer.toString(month);
							}
							if (day <= 9) {
								day_show = "0" + day;
							} else {
								day_show = Integer.toString(day);
							}
							begin_time_text_view.setText(year + "-"
									+ month_show + "-" + day_show);
						}
					});
			if (begin_time_text_view.getText().toString().equals("")) {
				Bundle bundle = new Bundle();
				bundle.putInt("year", 1991);
				bundle.putInt("month", 01);
				bundle.putInt("day", 01);
				inputBirthdayDialogFragment.setArguments(bundle);
				inputBirthdayDialogFragment.show(getActivity()
						.getFragmentManager(), "inputBirthdayDialogFragment");
			} else {
				String date[] = begin_time_text_view.getText().toString()
						.split("-");
				int year = Integer.parseInt(date[0]);
				int month = Integer.parseInt(date[1]);
				int day = Integer.parseInt(date[2]);
				Bundle bundle = new Bundle();
				bundle.putInt("year", year);
				bundle.putInt("month", month);
				bundle.putInt("day", day);
				inputBirthdayDialogFragment.setArguments(bundle);
				inputBirthdayDialogFragment.show(getActivity()
						.getFragmentManager(), "inputBirthdayDialogFragment");
			}
		}
	}

	// 点击结束日期监视器 尼见 2015-03-20
	class MyClickEndTimeListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			InputBirthdayDialogFragment inputBirthdayDialogFragment = InputBirthdayDialogFragment
					.newInstance(0, 0, 0);
			inputBirthdayDialogFragment
					.setConfirmInterface(new InputBirthdayDialogFragment.ConfirmInterface() {

						// 接口回调实现通信
						@Override
						public void onConfirmInterface(int year, int month,
								int day) {
							String month_show;
							String day_show;
							if (month <= 9) {
								month_show = "0" + month;
							} else {
								month_show = Integer.toString(month);
							}
							if (day <= 9) {
								day_show = "0" + day;
							} else {
								day_show = Integer.toString(day);
							}
							end_time_text_view.setText(year + "-" + month_show
									+ "-" + day_show);
						}
					});
			if (end_time_text_view.getText().toString().equals("")) {
				Bundle bundle = new Bundle();
				bundle.putInt("year", 1991);
				bundle.putInt("month", 01);
				bundle.putInt("day", 01);
				inputBirthdayDialogFragment.setArguments(bundle);
				inputBirthdayDialogFragment.show(getActivity()
						.getFragmentManager(), "inputBirthdayDialogFragment");
			} else {
				String date[] = end_time_text_view.getText().toString()
						.split("-");
				int year = Integer.parseInt(date[0]);
				int month = Integer.parseInt(date[1]);
				int day = Integer.parseInt(date[2]);
				Bundle bundle = new Bundle();
				bundle.putInt("year", year);
				bundle.putInt("month", month);
				bundle.putInt("day", day);
				inputBirthdayDialogFragment.setArguments(bundle);
				inputBirthdayDialogFragment.show(getActivity()
						.getFragmentManager(), "inputBirthdayDialogFragment");
			}
		}
	}

	//TODO 间隔逻辑需要完善
	// 点击间隔监视器 尼见 2015-03-19
	class MyClickIntervalListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			IntervalDialogFragment intervalDialogFragment = new IntervalDialogFragment();
			Bundle bundle = new Bundle();
			String frequencyParams = interval_text_view.getText().toString();
			if (frequencyParams.length() == 0) {
				bundle.putInt("num", 1);
			} else {
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(frequencyParams);
				int time = Integer.parseInt(m.replaceAll("").trim());
				bundle.putInt("num", time);
			}
			intervalDialogFragment.setArguments(bundle);
			intervalDialogFragment
					.setConfirmInterface(new IntervalDialogFragment.ConfirmInterface() {

						@Override
						public void onConfirmInterface(int time) {
							interval_text_view.setText(time + "小时");
						}
					});
			intervalDialogFragment.show(getActivity().getFragmentManager(),
					"drugFrequencyDialogFragment");
		}
	}

	//TODO 实体信息未处理
	// 点击删除监视器 尼见 2015-03-20
	class MyClickDeleteListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int length = list.size();
			if (length == 1) {

				list = null;
				list = new ArrayList<DrugImageItem>();
				for (int i = 0; i < length - 1; i++) {
					if (i == length - 2) {
						list.add(new DrugImageItem("1"));
					} else {
						list.add(new DrugImageItem("0"));
					}
				}
				DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
				drugImageItemAdapter.setContext(getActivity());
				drugImageItemAdapter.setArrayList(list);
				drug.setAdapter(drugImageItemAdapter);
				scroll_view_tab.setVisibility(View.GONE);

				return;
			}
			list = null;
			list = new ArrayList<DrugImageItem>();
			for (int i = 0; i < length - 1; i++) {
				if (i == length - 2) {
					list.add(new DrugImageItem("1"));
				} else {
					list.add(new DrugImageItem("0"));
				}
			}
			DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
			drugImageItemAdapter.setContext(getActivity());
			drugImageItemAdapter.setArrayList(list);
			drug.setAdapter(drugImageItemAdapter);
		}
	}

	// 点击添加药品监视器 尼见 2015-03-20
	class MyClickAddDrugListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// list中没有数据，直接添加一条药物
			if (list.size() == 0) {
				list.add(new DrugImageItem("1"));
				DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
				drugImageItemAdapter.setContext(getActivity());
				drugImageItemAdapter.setArrayList(list);
				drug.setAdapter(drugImageItemAdapter);
				scroll_view_tab.setVisibility(View.VISIBLE);
				return;
			}
			if (list.size() == 5) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithAddDrugReachMax();
						dialog.show();

					}
				});

				return;
			}

			// 判断药品名称是否为空
			if (input_drug_name.getText().toString().length() == 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithNoDrugName();
						dialog.show();

					}
				});

				return;
			}

			// 判断用药频率是否正确
			if (drugFrequencyTextView.getText().toString().length() == 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithDrugFrequencyWrong();
						dialog.show();

					}
				});

				return;
			}

			// 判断用药剂量是否正确
			if (drugDoseTextView.getText().toString().length() == 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithDrugDoseWrong();
						dialog.show();

					}
				});

				return;
			}

			String[] begin_time_array = begin_time_text_view.getText()
					.toString().split("-");
			int year = Integer.parseInt(begin_time_array[0]);
			int month = Integer.parseInt(begin_time_array[1]);
			int day = Integer.parseInt(begin_time_array[2]);
			int nowDate = year * 10000 + month * 100 + day;
			String[] end_time_array = end_time_text_view.getText().toString()
					.split("-");
			int year_end = Integer.parseInt(end_time_array[0]);
			int month_end = Integer.parseInt(end_time_array[1]);
			int day_end = Integer.parseInt(end_time_array[2]);
			int endDate = year_end * 10000 + month_end * 100 + day_end;

			// 判断服药时间是否超过三个月
			if (endDate - nowDate >= 300) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithTakeDrugLongerThanOneMonth();
						dialog.show();

					}
				});

				return;
			}

			// 判断结束日期是否早于开始日期
			if (nowDate - endDate > 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithBeginDateBiggerThanEndDate();
						dialog.show();

					}
				});

				return;

			}

			// 将本次添加的药品信息存取内存中的实体内
			AddDrugInfo addDrugInfo = new AddDrugInfo();
			addDrugInfo.setBasicDrugID(basicDrugID_from_search);
			addDrugInfo.setProductDrugID(productDrugID_from_search);
			addDrugInfo.setDrugName(input_drug_name.getText().toString());
			addDrugInfo.setCycleDays("1");
			String takeDrugFrequency = drugFrequencyTextView.getText()
					.toString();
			String regEx = "[^0-9]";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(takeDrugFrequency);
			String takeDrugFrequencyNum = matcher.replaceAll("").trim();
			addDrugInfo.setCycleCount(takeDrugFrequencyNum);

			String takeDrugInterval = interval_text_view.getText().toString();
			matcher = pattern.matcher(takeDrugInterval);
			String takeDrugIntervalNum = matcher.replaceAll("").trim();
			if (takeDrugIntervalNum.length() == 0) {
				addDrugInfo.setIntervalValue("0");
			} else {
				// 服药时间间隔的单位已经改成了分钟，例如当选择“1”时候，表示“60分钟”
				int hour = Integer.parseInt(takeDrugIntervalNum);
				int minute = hour * 60;
				addDrugInfo.setIntervalValue(minute + "");
			}

			addDrugInfo.setIntervalUnit("");
			addDrugInfo.setBegin(begin_time_text_view.getText().toString());
			addDrugInfo.setEnd(end_time_text_view.getText().toString());
			addDrugInfo.setDrugCount(drugDose_from_dialog);
			databaseName = "drug_info";
			databaseHelper = new CreateSQLiteDatabase(getActivity(),
					databaseName, null, 1);
			db = databaseHelper.getWritableDatabase();
			String sql = "select doseUnitID from drugDoseUnitTable where doseUnit ='"
					+ drugDoseUnit_from_dialog + "'";
			Cursor cursor = db.rawQuery(sql, null);
			String drugDoseUnitID = "";
			while (cursor.moveToNext()) {
				drugDoseUnitID = cursor.getString(0);
			}
			cursor.close();
			db.close();
			addDrugInfo.setDrugCountUnit(drugDoseUnitID);
			addDrugInfo.setRemark(remark.getText().toString());
			listAddDrugInfo.add(addDrugInfo);
			scroll_view_tab.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setFlag("0");
			}

			selectAddDrugMethodInAddDrugDialogFragment = new SelectAddDrugMethodInAddDrugDialogFragment();
			selectAddDrugMethodInAddDrugDialogFragment
					.setConfirmInterface(new SelectAddDrugMethodInAddDrugDialogFragment.ConfirmInterface() {

						@Override
						public void onConfirmInterface(String method) {
							if (method.equals("manual")) {
								// 将ScrollView内的数据清空
								input_drug_name.setText("");
								input_drug_name.setHint("请输入药品名称");
								drugFrequencyTextView.setText("");
								drugFrequencyTextView.setHint("请选择");
								drugDoseTextView.setText("");
								drugDoseTextView.setHint("请选择");
								begin_time_text_view.setText(localTimeStamp);
								end_time_text_view
										.setText(sixDayLaterDateTimeStamp);
								interval_text_view.setText("");
								interval_text_view.setHint("请选择");
								remark.setText("");
								list.add(new DrugImageItem("1"));
								DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
								drugImageItemAdapter.setContext(getActivity());
								drugImageItemAdapter.setArrayList(list);
								drug.setAdapter(drugImageItemAdapter);
							} else {
								Intent intent = new Intent(
										"com.google.zxing.client.android.SCAN");
								intent.putExtra(
										"com.google.zxing.client.android.SCAN.SCAN_MODE",
										"QR_CODE_MODE");
								startActivityForResult(intent, 0);
							}
						}
					});

			selectAddDrugMethodInAddDrugDialogFragment.show(
					getFragmentManager(),
					"selectAddDrugMethodInAddDrugDialogFragment");

		}

		// 未输入药品名称提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithNoDrugName() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("未输入药品名称");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 输入药品数量达到上限提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithAddDrugReachMax() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("添加药品数量达到上限");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 用药频率输入不正确提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithDrugFrequencyWrong() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("用药频率输入不正确");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 用药剂量输入不正确提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithDrugDoseWrong() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("用药剂量输入不正确");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 服药时间超过三个月提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithTakeDrugLongerThanOneMonth() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("服药时间不能超过三个月");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 结束日期早于开始日期提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithBeginDateBiggerThanEndDate() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("结束日期不能早于开始日期");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
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
				while (cursor2.moveToNext()) {
					showName = cursor2.getString(0);
					basicDrugID_from_search = cursor2.getString(1);
					productDrugID_from_search = productDrugID;
				}
				selectAddDrugMethodInAddDrugDialogFragment.dismiss();
				// 将ScrollView内的数据清空
				input_drug_name.setText(showName);
				drugFrequencyTextView.setText("");
				drugFrequencyTextView.setHint("请选择");
				drugDoseTextView.setText("");
				drugDoseTextView.setHint("请选择");
				begin_time_text_view.setText(localTimeStamp);
				end_time_text_view.setText(sixDayLaterDateTimeStamp);
				interval_text_view.setText("");
				interval_text_view.setHint("请选择");
				remark.setText("");
				list.add(new DrugImageItem("1"));
				DrugImageItemAdapter drugImageItemAdapter = new DrugImageItemAdapter();
				drugImageItemAdapter.setContext(getActivity());
				drugImageItemAdapter.setArrayList(list);
				drug.setAdapter(drugImageItemAdapter);

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

	// 在工作线程中调用生成用药时间点接口 尼见 2015-05-03
	class GenerateTakeDrugTime extends AsyncTask<Object, Object, Object> {

		// 判断是否需要提示用户无法生成用药时间点
		Boolean canotGenerateTakeTimeFlag = false;
		ArrayList<GenerateTakeDrugTimeList> list1 = new ArrayList<>();
		String drugName = "", statusCodeFromServer;
		String interAction = "";// 相互作用 300
		String repeatDrug = "";// 重复用药 310
		String systemNotExist = "";// 系统不存在药品 320
		String drugNotExistRepresent = "";// 药品不存在代表品 330
		String notComputeTakeDrugTime = "";// 不计算药品服药时间 340
		String intervalIsNotReasonable = "";// 间隔不合理 350
		String errorMessage = "";// 错误信息

		@Override
		protected Object doInBackground(Object... params) {

			// 调用生成用药时间点接口 尼见 2015-03-27
			// 声明网址字符串
			App app = (App) getActivity().getApplication();
			String uriAPI2 = app.getInterfaceUrl()
					+ "guarder/api/drug/SystemGuideTime";
			HttpPost httpRequest2 = new HttpPost(uriAPI2);
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			// 获取当前时间
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());
			String localTimeStamp = formatter.format(curDate);
			databaseName = "LocalDrugMessage";
			databaseHelper = new CreateSQLiteDatabase(getActivity(),
					databaseName, null, 1);
			db = databaseHelper.getWritableDatabase();
			String sql = "select * from anonymityRegister";
			Cursor cursor = db.rawQuery(sql, null);
			String userID = null;
			while (cursor.moveToNext()) {
				userID = cursor.getString(3);
			}
			cursor.close();
			db.close();
			params2.add(new BasicNameValuePair("userID", userID));
			params2.add(new BasicNameValuePair("memberID", current_member_id));
			params2.add(new BasicNameValuePair("diningTime", "30"));
			int addDrugNum = listAddDrugInfo.size();
			switch (addDrugNum) {
			case 1:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listAddDrugInfo.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(0).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(0).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(0).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(0).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(0).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(0).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(0).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(0).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(0).getRemark() + "\"}]"));
				break;
			case 2:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listAddDrugInfo.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(0).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(0).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(0).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(0).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(0).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(0).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(0).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(0).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(0).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(1).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(1).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(1).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(1).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(1).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(1).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(1).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(1).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(1).getRemark() + "\"}]"));
				break;
			case 3:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listAddDrugInfo.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(0).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(0).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(0).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(0).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(0).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(0).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(0).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(0).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(0).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(1).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(1).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(1).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(1).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(1).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(1).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(1).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(1).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(1).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(2).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(2).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(2).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(2).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(2).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(2).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(2).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(2).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(2).getRemark() + "\"}]"));
				break;
			case 4:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listAddDrugInfo.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(0).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(0).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(0).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(0).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(0).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(0).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(0).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(0).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(0).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(1).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(1).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(1).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(1).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(1).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(1).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(1).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(1).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(1).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(2).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(2).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(2).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(2).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(2).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(2).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(2).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(2).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(2).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(3).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(3).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(3).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(3).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(3).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(3).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(3).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(3).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(3).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(3).getRemark() + "\"}]"));
				break;
			case 5:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listAddDrugInfo.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(0).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(0).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(0).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(0).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(0).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(0).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(0).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(0).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(0).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(1).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(1).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(1).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(1).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(1).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(1).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(1).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(1).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(1).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(2).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(2).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(2).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(2).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(2).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(2).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(2).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(2).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(2).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(3).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(3).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(3).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(3).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(3).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(3).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(3).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(3).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(3).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(3).getRemark()
								+ "\"}|{\"basicDrugID\":\""
								+ listAddDrugInfo.get(4).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listAddDrugInfo.get(4).getProductDrugID()
								+ "\",\"drugName\":\""
								+ listAddDrugInfo.get(4).getDrugName()
								+ "\",\"cycleDays\":\"1\",\"cycleCount\":\""
								+ listAddDrugInfo.get(4).getCycleCount()
								+ "\",\"intervalValue\":\""
								+ listAddDrugInfo.get(4).getIntervalValue()
								+ "\",\"intervalUnit\":\"\",\"begin\":\""
								+ listAddDrugInfo.get(4).getBegin()
								+ "\",\"end\":\""
								+ listAddDrugInfo.get(4).getEnd()
								+ "\",\"drugCount\":\""
								+ listAddDrugInfo.get(4).getDrugCount()
								+ "\",\"drugCountUnit\":\""
								+ listAddDrugInfo.get(4).getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listAddDrugInfo.get(4).getRemark() + "\"}]"));
				break;
			default:
				break;
			}
			try {
				// 发出http request
				httpRequest2.setEntity(new UrlEncodedFormEntity(params2,
						HTTP.UTF_8));
				// 取得http response
				HttpResponse httpResponse2 = new DefaultHttpClient()
						.execute(httpRequest2);
				// 若状态码为200 ok
				if (httpResponse2.getStatusLine().getStatusCode() == 200) {
					String strResult2 = EntityUtils.toString(httpResponse2
							.getEntity());
					JSONObject jsonObject = new JSONObject(strResult2);
					// 如果是多条数据是jsonArray，如果是一条数据是jsonObject.
					if (jsonObject.get("data") instanceof JSONObject) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ToastHelper.toastLong(getActivity(),
										"我是jsonobject");
							}
						});
						// 成功接受到服务器返回数据之后，结束进度等待窗口
						progressDialog.dismiss();
						JSONObject jsonObject1 = jsonObject
								.getJSONObject("data");
						String basicDrugID = jsonObject1
								.getString("basicDrugID");
						String productDrugID = jsonObject1
								.getString("productDrugID");
						String guideDrugID = jsonObject1
								.getString("guideDrugID");
						String tempDrugID = jsonObject1.getString("tempDrugID");
						drugName = jsonObject1.getString("drugName");
						String cycleDays = jsonObject1.getString("cycleDays");
						String cycleCount = jsonObject1.getString("cycleCount");
						String intervalValue = jsonObject1
								.getString("intervalValue");
						String intervalUnit = jsonObject1
								.getString("intervalUnit");
						String isUsing = jsonObject1.getString("isUsing");
						String begin = jsonObject1.getString("begin");
						String end = jsonObject1.getString("end");
						String drugCount = jsonObject1.getString("drugCount");
						String drugCountUnit = jsonObject1
								.getString("drugCountUnit");
						String remark = jsonObject1.getString("remark");
						String statusCode = jsonObject1.getString("statusCode");
						statusCodeFromServer = statusCode;
						ArrayList<TakeTimeList> list2 = new ArrayList<>();
						// 如果状态码是100，则有用药时间点。如果不是，则将用药时间点置空
						if (statusCodeFromServer.equals("100")) {
							JSONArray jsonArrayTakeTimeList = jsonObject1
									.getJSONArray("takeTimeList");
							for (int j = 0; j < jsonArrayTakeTimeList.length(); j++) {
								JSONObject jsonObject4 = jsonArrayTakeTimeList
										.getJSONObject(j);
								String date = jsonObject4.getString("date");
								String time = jsonObject4.getString("time");
								// 这里必须要new出一个实例，否则list2添加的值会覆盖，list添加的是对象的地址，所以必须new出新的对象。
								TakeTimeList takeTimeList = new TakeTimeList();
								takeTimeList.setDate(date);
								takeTimeList.setTime(time);
								list2.add(takeTimeList);
							}
						} else {
							TakeTimeList takeTimeList = new TakeTimeList();
							takeTimeList.setDate("");
							takeTimeList.setTime("");
							list2.add(takeTimeList);
						}
						// 保存当前用药信息的实体
						GenerateTakeDrugTimeList generateTakeDrugTimeList = new GenerateTakeDrugTimeList();
						generateTakeDrugTimeList.setLocation(0);
						generateTakeDrugTimeList.setBasicDrugID(basicDrugID);
						generateTakeDrugTimeList
								.setProductDrugID(productDrugID);
						generateTakeDrugTimeList.setGuideDrugID(guideDrugID);
						generateTakeDrugTimeList.setTempDrugID(tempDrugID);
						generateTakeDrugTimeList.setDrugName(drugName);
						generateTakeDrugTimeList.setCycleDays(cycleDays);
						generateTakeDrugTimeList.setCycleCount(cycleCount);
						generateTakeDrugTimeList
								.setIntervalValue(intervalValue);
						generateTakeDrugTimeList.setIntervalUnit(intervalUnit);
						generateTakeDrugTimeList.setIsUsing(isUsing);
						generateTakeDrugTimeList.setBegin(begin);
						generateTakeDrugTimeList.setEnd(end);
						generateTakeDrugTimeList.setDrugCount(drugCount);
						generateTakeDrugTimeList
								.setDrugCountUnit(drugCountUnit);
						generateTakeDrugTimeList.setRemark(remark);
						generateTakeDrugTimeList.setStatusCode(statusCode);
						generateTakeDrugTimeList.setTakeTimeList(list2);
						list1.add(generateTakeDrugTimeList);
						// 对状态码进行判断 尼见 2015-04-29
						// 相互作用 300
						// 重复用药 310
						// 系统不存在药品 320
						// 药品不存在代表品 330
						// 不计算药品服药时间 340
						// 间隔不合理 350
						// 返回成功 100
						if (statusCodeFromServer.equals("300")) {
							canotGenerateTakeTimeFlag = true;
							interAction = interAction.concat(drugName + " ");
						}
						if (statusCodeFromServer.equals("310")) {
							canotGenerateTakeTimeFlag = true;
							repeatDrug = repeatDrug.concat(drugName + " ");
						}
						if (statusCodeFromServer.equals("320")) {
							canotGenerateTakeTimeFlag = true;
							systemNotExist = systemNotExist.concat(drugName
									+ " ");
						}
						if (statusCodeFromServer.equals("330")) {
							canotGenerateTakeTimeFlag = true;
							drugNotExistRepresent = drugNotExistRepresent
									.concat(drugName + " ");
						}
						if (statusCodeFromServer.equals("340")) {
							canotGenerateTakeTimeFlag = true;
							notComputeTakeDrugTime = notComputeTakeDrugTime
									.concat(drugName + " ");
						}
						if (statusCodeFromServer.equals("350")) {
							canotGenerateTakeTimeFlag = true;
							intervalIsNotReasonable = intervalIsNotReasonable
									.concat(drugName + " ");
						}
						// 判断是否能生成时间点
						if (canotGenerateTakeTimeFlag == true) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {

									AlertDialog dialog = getAlertDialogWithGenerateTakeTimeWrong();
									dialog.show();

								}
							});
							canotGenerateTakeTimeFlag = false;
							return null;
						}
						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						GenerateDrugTipsFragment generateDrugTipsFragment = new GenerateDrugTipsFragment();
						Bundle bundle = new Bundle();
						bundle.putCharSequenceArrayList("takeDrugTime",
								(ArrayList) list1);
						bundle.putString("current_member_id", current_member_id);
						generateDrugTipsFragment.setArguments(bundle);
						fragmentTransaction.setCustomAnimations(
								R.anim.enter_from_right, R.anim.exit_to_left);
						fragmentTransaction.replace(R.id.login_fragment,
								generateDrugTipsFragment,
								"generateDrugTipsFragment");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						db.close();
					} else {
						// 多条数据，是jsonArray格式数据
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ToastHelper.toastLong(getActivity(),
										"我是jsonArray");
							}
						});
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						// 成功接受到服务器返回数据之后，结束进度等待窗口
						progressDialog.dismiss();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject1 = jsonArray.getJSONObject(i);
							String basicDrugID = jsonObject1
									.getString("basicDrugID");
							String productDrugID = jsonObject1
									.getString("productDrugID");
							String guideDrugID = jsonObject1
									.getString("guideDrugID");
							String tempDrugID = jsonObject1
									.getString("tempDrugID");
							drugName = jsonObject1.getString("drugName");
							String cycleDays = jsonObject1
									.getString("cycleDays");
							String cycleCount = jsonObject1
									.getString("cycleCount");
							String intervalValue = jsonObject1
									.getString("intervalValue");
							String intervalUnit = jsonObject1
									.getString("intervalUnit");
							String isUsing = jsonObject1.getString("isUsing");
							String begin = jsonObject1.getString("begin");
							String end = jsonObject1.getString("end");
							String drugCount = jsonObject1
									.getString("drugCount");
							String drugCountUnit = jsonObject1
									.getString("drugCountUnit");
							String remark = jsonObject1.getString("remark");
							String statusCode = jsonObject1
									.getString("statusCode");
							statusCodeFromServer = statusCode;
							ArrayList<TakeTimeList> list2 = new ArrayList<>();
							if (statusCodeFromServer.equals("100")) {
								JSONArray jsonArrayTakeTimeList = jsonObject1
										.getJSONArray("takeTimeList");
								for (int j = 0; j < jsonArrayTakeTimeList
										.length(); j++) {
									JSONObject jsonObject4 = jsonArrayTakeTimeList
											.getJSONObject(j);
									String date = jsonObject4.getString("date");
									String time = jsonObject4.getString("time");
									// 这里必须要new出一个实例，否则list2添加的值会覆盖，list添加的是对象的地址，所以必须new出新的对象。
									TakeTimeList takeTimeList = new TakeTimeList();
									takeTimeList.setDate(date);
									takeTimeList.setTime(time);
									list2.add(takeTimeList);
								}
							} else {
								TakeTimeList takeTimeList = new TakeTimeList();
								takeTimeList.setDate("");
								takeTimeList.setTime("");
								list2.add(takeTimeList);
							}
							GenerateTakeDrugTimeList generateTakeDrugTimeList = new GenerateTakeDrugTimeList();
							generateTakeDrugTimeList.setLocation(i);
							generateTakeDrugTimeList
									.setBasicDrugID(basicDrugID);
							generateTakeDrugTimeList
									.setProductDrugID(productDrugID);
							generateTakeDrugTimeList
									.setGuideDrugID(guideDrugID);
							generateTakeDrugTimeList.setTempDrugID(tempDrugID);
							generateTakeDrugTimeList.setDrugName(drugName);
							generateTakeDrugTimeList.setCycleDays(cycleDays);
							generateTakeDrugTimeList.setCycleCount(cycleCount);
							generateTakeDrugTimeList
									.setIntervalValue(intervalValue);
							generateTakeDrugTimeList
									.setIntervalUnit(intervalUnit);
							generateTakeDrugTimeList.setIsUsing(isUsing);
							generateTakeDrugTimeList.setBegin(begin);
							generateTakeDrugTimeList.setEnd(end);
							generateTakeDrugTimeList.setDrugCount(drugCount);
							generateTakeDrugTimeList
									.setDrugCountUnit(drugCountUnit);
							generateTakeDrugTimeList.setRemark(remark);
							generateTakeDrugTimeList.setStatusCode(statusCode);
							generateTakeDrugTimeList.setTakeTimeList(list2);
							list1.add(generateTakeDrugTimeList);

							// 对状态码进行判断 尼见 2015-04-29
							// 相互作用 300
							// 重复用药 310
							// 系统不存在药品 320
							// 药品不存在代表品 330
							// 不计算药品服药时间 340
							// 间隔不合理 350
							// 返回成功 100
							if (statusCodeFromServer.equals("300")) {
								canotGenerateTakeTimeFlag = true;
								interAction = interAction
										.concat(drugName + " ");
							}
							if (statusCodeFromServer.equals("310")) {
								canotGenerateTakeTimeFlag = true;
								repeatDrug = repeatDrug.concat(drugName + " ");
							}
							if (statusCodeFromServer.equals("320")) {
								canotGenerateTakeTimeFlag = true;
								systemNotExist = systemNotExist.concat(drugName
										+ " ");
							}
							if (statusCodeFromServer.equals("330")) {
								canotGenerateTakeTimeFlag = true;
								drugNotExistRepresent = drugNotExistRepresent
										.concat(drugName + " ");
							}
							if (statusCodeFromServer.equals("340")) {
								canotGenerateTakeTimeFlag = true;
								notComputeTakeDrugTime = notComputeTakeDrugTime
										.concat(drugName + " ");
							}
							if (statusCodeFromServer.equals("350")) {
								canotGenerateTakeTimeFlag = true;
								intervalIsNotReasonable = intervalIsNotReasonable
										.concat(drugName + " ");
							}
						}
						// 判断是否能生成时间点
						if (canotGenerateTakeTimeFlag == true) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {

									AlertDialog dialog = getAlertDialogWithGenerateTakeTimeWrong();
									dialog.show();

								}
							});
							canotGenerateTakeTimeFlag = false;
							return null;
						}

						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						GenerateDrugTipsFragment generateDrugTipsFragment = new GenerateDrugTipsFragment();
						Bundle bundle = new Bundle();
						bundle.putCharSequenceArrayList("takeDrugTime",
								(ArrayList) list1);
						bundle.putString("current_member_id", current_member_id);
						generateDrugTipsFragment.setArguments(bundle);
						fragmentTransaction.setCustomAnimations(
								R.anim.enter_from_right, R.anim.exit_to_left);
						fragmentTransaction.replace(R.id.login_fragment,
								generateDrugTipsFragment,
								"generateDrugTipsFragment");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						db.close();
					}

				}
			} catch (Exception e) {
				System.out.println(e);
			}

			return null;
		}

		// 无法生成服药时间点提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithGenerateTakeTimeWrong() {

			errorMessage = "";
			if (interAction.length() != 0) {
				errorMessage = errorMessage.concat(interAction
						+ "存在相互作用，具体用法请咨询药师，您可以手动设置服药时间");
			}
			if (repeatDrug.length() != 0) {
				errorMessage = errorMessage.concat(repeatDrug
						+ "存在重复用药，具体用法请咨询药师，您可以手动设置服药时间");
			}
			if (systemNotExist.length() != 0) {
				errorMessage = errorMessage.concat("系统暂不支持 " + systemNotExist
						+ " 药品的服药时间计算，具体用法请咨询药师，您可以手动设置服药时间");
			}
			if (drugNotExistRepresent.length() != 0) {
				errorMessage = errorMessage.concat("系统暂不支持 "
						+ drugNotExistRepresent
						+ " 药品的服药时间计算，具体用法请咨询药师，您可以手动设置服药时间");
			}
			if (notComputeTakeDrugTime.length() != 0) {
				errorMessage = errorMessage.concat("系统暂不支持 "
						+ notComputeTakeDrugTime
						+ " 药品的服药时间计算，具体用法请咨询药师，您可以手动设置服药时间");
			}
			if (intervalIsNotReasonable.length() != 0) {
				errorMessage = errorMessage.concat(intervalIsNotReasonable
						+ " 服药时间间隔过短/过长，具体用法请咨询药师，您可以手动设置服药时间");
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			builder.setTitle("提示");
			builder.setMessage(errorMessage);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentManager fragmentManager = getActivity()
									.getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							GenerateDrugTipsFragment generateDrugTipsFragment = new GenerateDrugTipsFragment();
							Bundle bundle = new Bundle();
							bundle.putCharSequenceArrayList("takeDrugTime",
									(ArrayList) list1);
							bundle.putString("current_member_id",
									current_member_id);
							generateDrugTipsFragment.setArguments(bundle);
							fragmentTransaction.setCustomAnimations(
									R.anim.enter_from_right,
									R.anim.exit_to_left);
							fragmentTransaction.replace(R.id.login_fragment,
									generateDrugTipsFragment,
									"generateDrugTipsFragment");
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						}
					});
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

	}

}