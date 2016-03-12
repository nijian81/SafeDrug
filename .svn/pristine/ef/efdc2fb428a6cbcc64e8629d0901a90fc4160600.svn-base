package cn.com.phhc.drugSafeApp.drugRemind;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.Constant.Constant;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.JudgeNetworkState;

import com.anupcowkur.wheelmenu.WheelMenu;

public class DrugRemindFragment extends Fragment {

	int sum_value = 1;
	int cureent_value = 1;
	ImageView addDrug, beforeDay, nextDay, wheelmenuimg;
	TextView date, currentPeople;
	String localTimeStamp, localTimeStamp2, current_member_id = null,
			current_member_portrait = null, current_member_name = null,
			drugName = "";
	static long timeShow;
	Animation anim4;
	private WheelMenu wheelMenu;
	private float down_x, down_y, move_x, move_y, up_x, up_y;
	private int width, height;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	Animation anim;
	String databaseName;
	NumberPicker drug_name;
	LinearLayout logo_lay;
	LinearLayout drug_lay;
	String rightShow = "sssss";
	TextView drug_total_number;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		height = wm.getDefaultDisplay().getHeight();
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.drug_remind_fragment,
				container, false);

		wheelMenu = (WheelMenu) rootView.findViewById(R.id.dayuanhuan);
		wheelMenu.setDivCount(10);
		wheelMenu.setWheelImage(R.drawable.dayuanhuan);
		// 当前日期
		date = (TextView) rootView.findViewById(R.id.date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// 当前手机时间
		SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
		
		timeShow = System.currentTimeMillis();
		Date curDate = new Date(System.currentTimeMillis());
		localTimeStamp = formatter.format(curDate);
		localTimeStamp2 = formatter2.format(curDate);
		date.setText(localTimeStamp);

		// 查询药品名（日期，用药人，时间三个条件同时满足）
		drug_name = (NumberPicker) rootView.findViewById(R.id.drug_name);
		logo_lay = (LinearLayout) rootView.findViewById(R.id.logo_lay);
		drug_lay = (LinearLayout) rootView.findViewById(R.id.drug_lay);
		drug_total_number = (TextView) rootView
				.findViewById(R.id.drug_total_number);
		String current_member_id, memberID;
		db = databaseHelper.getWritableDatabase();
		String sql1 = "select current_member_id  from  anonymityRegister";
		Cursor cursor1 = db.rawQuery(sql1, null);
		while (cursor1.moveToNext()) {
			current_member_id = cursor1.getString(0);
		}
		String sql2 = "select distinct drugName from TakeTimeTable,anonymityregister where date='"
				+ localTimeStamp
				+ "' and TakeTimeTable.memberID=anonymityRegister.current_member_id";
		Cursor cursor2 = db.rawQuery(sql2, null);

		int i = 0;
		int size = cursor2.getCount();
		final String[] values = new String[size];
		while (cursor2.moveToNext()) {
			logo_lay.setVisibility(rootView.GONE);
			drug_lay.setVisibility(rootView.VISIBLE);
			drug_total_number.setVisibility(rootView.VISIBLE);
			values[i++] = cursor2.getString(0);
		}
		if (i == 0) {
			logo_lay.setVisibility(rootView.VISIBLE);
			drug_lay.setVisibility(rootView.GONE);
			drug_total_number.setVisibility(rootView.GONE);
		} else {
			sum_value = i;
			try {
				drug_name.setDisplayedValues(values);
				drug_name.setMaxValue(i - 1);
				drug_name.setMinValue(0);
				drug_name.setValue(0);
				drug_name
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				drug_name
						.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

							@Override
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								rightShow = values[newVal];
								cureent_value = newVal + 1;
								drug_total_number.setText("当前第" + cureent_value
										+ "/" + sum_value + "种");
							}
						});
				rightShow = values[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
			drug_total_number.setText("当前第" + cureent_value + "/" + i + "种");
		}

		db.close();

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
		currentPeople.setOnClickListener(new MyClickChangePeopleListener());
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
		// 选择用药添加方式
		addDrug = (ImageView) rootView.findViewById(R.id.addDrug);
		addDrug.setOnClickListener(new MyClickAddDrugListener());

		// 上一天
		beforeDay = (ImageView) rootView.findViewById(R.id.beforeDay);
		beforeDay.setOnClickListener(new MyClickBeforeDayListener());
		// 下一天
		nextDay = (ImageView) rootView.findViewById(R.id.nextDay);
		nextDay.setOnClickListener(new MyClickNextDayListener());
		// 得到大圆的
		// wheelmenu = (ImageView) rootView.findViewById(R.id.dayuanhuan);
		// wheelmenu.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// down_x = event.getX();
		// down_y = event.getY();
		// break;
		// case MotionEvent.ACTION_MOVE:
		// move_x = event.getX();
		// move_y = event.getX();
		// break;
		// case MotionEvent.ACTION_UP:
		// up_x = event.getX();
		// up_y = event.getY();
		// anim4 = new TranslateAnimation(down_x, up_x, down_y, up_y);
		// anim4.setDuration(1000);
		// wheelmenu.setAnimation(anim4);
		// break;
		// default:
		// break;
		// }
		// return true;
		// }
		// });
		return rootView;

	}

	private WheelMenu findViewById(int dayuanhuan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		Constant.height = getResources().getDimensionPixelSize(
				R.dimen.xmlTopTitleHeight);

		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	// 点击添加药品监视器 尼见 2015-03-18
	class MyClickAddDrugListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (JudgeNetworkState.isConnected(getActivity())) {
				db = databaseHelper.getWritableDatabase();
				String sql = "select getup from MemberInfoTable where name='"
						+ current_member_name + "'";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					String getupTimeParam = cursor.getString(0);
					if (getupTimeParam.length() == 0) {
						ConfirmTakeDrugMemberRelaxTimeDialogFragment confirmTakeDrugMemberRelaxTimeDialogFragment = new ConfirmTakeDrugMemberRelaxTimeDialogFragment();
						Bundle bundle = new Bundle();
						bundle.putString("name", current_member_name);
						confirmTakeDrugMemberRelaxTimeDialogFragment
								.setArguments(bundle);
						confirmTakeDrugMemberRelaxTimeDialogFragment.show(
								getFragmentManager(),
								"confirmTakeDrugMemberRelaxTimeDialogFragment");
						return;
					}
				}
				SelectAddDrugMethodDialogFragment selectAddDrugMethodDialogFragment = new SelectAddDrugMethodDialogFragment();
				selectAddDrugMethodDialogFragment.show(getFragmentManager(),
						"selectAddDrugMethodDialogFragment");
			} else {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						AlertDialog dialog = getAlertDialogWithNetworkIsWrong();
						dialog.show();
					}
				});
			}
		}
	}

	// 提示网络不可用 尼见 2015-04-47
	AlertDialog getAlertDialogWithNetworkIsWrong() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("当前网络不可用，请检查您的网络设置");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();

		return dialog;
	}

	// 点击上一天监视器 尼见 2015-03-20
	class MyClickBeforeDayListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			timeShow = timeShow - 86400000;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = new Date(timeShow);
			localTimeStamp = formatter.format(curDate);
			date.setText(localTimeStamp);

			// 点击上一天时查询相对应的药品
			String current_member_id, memberID;
			db = databaseHelper.getWritableDatabase();
			String sql1 = "select current_member_id  from  anonymityRegister";
			Cursor cursor1 = db.rawQuery(sql1, null);
			while (cursor1.moveToNext()) {
				current_member_id = cursor1.getString(0);
			}
			String sql2 = "select distinct drugName from TakeTimeTable,anonymityregister where date='"
					+ localTimeStamp
					+ "' and TakeTimeTable.memberID=anonymityRegister.current_member_id";
			Cursor cursor2 = db.rawQuery(sql2, null);
			int size = cursor2.getCount();
			final String[] values = new String[size];
			int i = 0;
			while (cursor2.moveToNext()) {
				logo_lay.setVisibility(v.GONE);
				drug_lay.setVisibility(v.VISIBLE);
				drug_total_number.setVisibility(v.VISIBLE);
				values[i++] = cursor2.getString(0);
			}
			if (i == 0) {
				logo_lay.setVisibility(v.VISIBLE);
				drug_lay.setVisibility(v.GONE);
				drug_total_number.setVisibility(v.GONE);
			} else {
				sum_value = i;
				try {
					drug_name.setDisplayedValues(values);
				} catch (Exception e) {
					// TODO: handle exception
				}
				drug_name.setMaxValue(i - 1);
				drug_name.setMinValue(0);
				drug_name.setValue(0);
				drug_name.setPadding(0, 0, 0, 0);
				drug_name
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				drug_name
						.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

							@Override
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								rightShow = values[newVal];
								cureent_value = newVal;
								drug_total_number.setText("当前第" + cureent_value
										+ "/" + sum_value + "种");
							}
						});
				rightShow = values[0];

				drug_total_number
						.setText("当前第" + cureent_value + "/" + i + "种");
			}

			db.close();

		}
	}

	// 点击下一天监视器 尼见 2015-03-20
	class MyClickNextDayListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			timeShow = timeShow + 86400000;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = new Date(timeShow);
			localTimeStamp = formatter.format(curDate);
			date.setText(localTimeStamp);

			// 点击下一天时查询相对应的药品
			String current_member_id, memberID;
			db = databaseHelper.getWritableDatabase();
			String sql1 = "select current_member_id  from  anonymityRegister";
			Cursor cursor1 = db.rawQuery(sql1, null);
			while (cursor1.moveToNext()) {
				current_member_id = cursor1.getString(0);
			}

			String sql2 = "select distinct drugName from TakeTimeTable,anonymityregister where date='"
					+ localTimeStamp
					+ "' and TakeTimeTable.memberID=anonymityRegister.current_member_id";
			Cursor cursor2 = db.rawQuery(sql2, null);
			int size = cursor2.getCount();
			final String[] values = new String[size];
			int i = 0;

			while (cursor2.moveToNext()) {
				logo_lay.setVisibility(v.GONE);
				drug_lay.setVisibility(v.VISIBLE);
				drug_total_number.setVisibility(v.VISIBLE);
				values[i++] = cursor2.getString(0);
			}
			if (i == 0) {
				logo_lay.setVisibility(v.VISIBLE);
				drug_lay.setVisibility(v.GONE);
				drug_total_number.setVisibility(v.GONE);
			} else {
				sum_value = i;
				try {
					drug_name.setDisplayedValues(values);
				} catch (Exception e) {
					// TODO: handle exception
				}
				drug_name.setMaxValue(i - 1);
				drug_name.setMinValue(0);
				drug_name.setValue(0);
				drug_name
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				drug_name
						.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

							@Override
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								rightShow = values[newVal];
								cureent_value = newVal;
								drug_total_number.setText("当前第" + cureent_value
										+ "/" + sum_value + "种");
							}
						});
				rightShow = values[0];
				drug_total_number
						.setText("当前第" + cureent_value + "/" + i + "种");
			}
			db.close();

		}
	}

	// 点击选择用药人监视器 尼见 2015-03-20
	class MyClickChangePeopleListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			SelectTakeDrugPeopleDialogFragment selectTakeDrugPeopleDialogFragment = new SelectTakeDrugPeopleDialogFragment();
			selectTakeDrugPeopleDialogFragment

					.setConfirmInterface(new SelectTakeDrugPeopleDialogFragment.ConfirmInterface() {
						@Override
						public void onConfirmInterface(String portrait,
								String name) {
							// 根据选择服药人返回的参数去更新当前服药人的页面显示和参数值
							currentPeople.setText(name);
							current_member_name = name;
							Drawable drawable = null;
							String portrait_flag = portrait;
							switch (portrait_flag) {
							case "tx1":
								drawable = getResources().getDrawable(
										R.drawable.tx1);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx2":
								drawable = getResources().getDrawable(
										R.drawable.tx2);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx3":
								drawable = getResources().getDrawable(
										R.drawable.tx3);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx4":
								drawable = getResources().getDrawable(
										R.drawable.tx4);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx5":
								drawable = getResources().getDrawable(
										R.drawable.tx5);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx6":
								drawable = getResources().getDrawable(
										R.drawable.tx6);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx7":
								drawable = getResources().getDrawable(
										R.drawable.tx7);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "tx8":
								drawable = getResources().getDrawable(
										R.drawable.tx8);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz1":
								drawable = getResources().getDrawable(
										R.drawable.tx1);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz2":
								drawable = getResources().getDrawable(
										R.drawable.tx2);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz3":
								drawable = getResources().getDrawable(
										R.drawable.tx3);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz4":
								drawable = getResources().getDrawable(
										R.drawable.tx4);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz5":
								drawable = getResources().getDrawable(
										R.drawable.tx5);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz6":
								drawable = getResources().getDrawable(
										R.drawable.tx6);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz7":
								drawable = getResources().getDrawable(
										R.drawable.tx7);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							case "xz8":
								drawable = getResources().getDrawable(
										R.drawable.tx8);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							default:
								drawable = getResources().getDrawable(
										R.drawable.tx4);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								break;
							}
							Drawable drawable2 = getResources().getDrawable(
									R.drawable.head_name_image);
							drawable2.setBounds(0, 0,
									drawable2.getMinimumWidth(),
									drawable2.getMinimumHeight());
							currentPeople.setCompoundDrawables(null, drawable,
									drawable2, null);
						}
					});

			Bundle bundle = new Bundle();
			bundle.putString("name", currentPeople.getText().toString());
			selectTakeDrugPeopleDialogFragment.setArguments(bundle);
			selectTakeDrugPeopleDialogFragment.show(getFragmentManager(),
					"selectTakeDrugPeopleDialogFragment");

			// 切换用药人时查询相对应的药品
			String current_member_id, memberID;
			db = databaseHelper.getWritableDatabase();
			String sql1 = "select current_member_id  from  anonymityRegister";
			Cursor cursor1 = db.rawQuery(sql1, null);
			while (cursor1.moveToNext()) {
				current_member_id = cursor1.getString(0);
			}

			String sql2 = "select distinct drugName from TakeTimeTable,anonymityregister where date='"
					+ localTimeStamp
					+ "' and TakeTimeTable.memberID=anonymityRegister.current_member_id";
			Cursor cursor2 = db.rawQuery(sql2, null);
			int size = cursor2.getCount();
			final String[] values = new String[size];
			int i = 0;
			while (cursor2.moveToNext()) {
				logo_lay.setVisibility(v.GONE);
				drug_lay.setVisibility(v.VISIBLE);
				drug_total_number.setVisibility(v.VISIBLE);
				values[i++] = cursor2.getString(0);
			}
			if (i == 0) {
				logo_lay.setVisibility(v.VISIBLE);
				drug_lay.setVisibility(v.GONE);
				drug_total_number.setVisibility(v.GONE);
			} else {
				sum_value = i;
				try {
					drug_name.setDisplayedValues(values);
				} catch (Exception e) {
					// TODO: handle exception
				}
				drug_name.setMaxValue(i - 1);
				drug_name.setMinValue(0);
				drug_name.setValue(0);
				drug_name
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				drug_name
						.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
							@Override
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								rightShow = values[newVal];
								cureent_value = newVal;
								drug_total_number.setText("当前第" + cureent_value
										+ "/" + sum_value + "种");
							}
						});
				rightShow = values[0];
				drug_total_number
						.setText("当前第" + cureent_value + "/" + i + "种");
			}
			db.close();
		}
	}

}