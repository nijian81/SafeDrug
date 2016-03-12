package cn.com.phhc.drugSafeApp.drugRemind;

/**
 * 生成用药提醒页面
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.App;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.GenerateTakeDrugTimeList;
import cn.com.phhc.drugSafeApp.utils.TakeDrugTimeItem;

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
import java.util.TreeSet;

public class GenerateDrugTipsFragment extends Fragment implements Serializable {

	// listGenerateTakeTime存放从添加药品页面传过来的用药数据或者是从时间设置页面传来的用药数据
	// listTemp存的是listGenerateTakeTime中已经生成用药时间点的用药数据
	// listSettingTemp存的是listGeneratesTakeTime中未生成用药时间点的用药数据
	List<GenerateTakeDrugTimeList> listGenerateTakeTime, listTemp,
			listSettingTemp;
	ProgressDialog progressDialog;
	ImageView back;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	// sys_userID表示自己的userID,current_member_id表示当前服药人的userID
	String databaseName, current_member_id = "", current_member_portrait = "",
			current_member_name = "", sys_userID = "";
	ListView takeDrugTime;
	// list存放的是生成用药提示页面listView的适配器中的arrayList的全部内容
	ArrayList<TakeDrugTimeItem> list;
	// takeDrugNum存放的是listGenerateTakeTime中已经生成用药时间点的数据个数
	// setDrugNum存放的是listGenerateTakeTime中未生成用药时间点的数据个数
	int takeDrugNum = 0, setDrugNum = 0, params = 0;
	TextView nextStep, currentPeople;

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

		View rootView = inflater.inflate(R.layout.generate_drug_tips_fragment,
				container, false);

		// 接收到的参数
		current_member_id = getArguments().getString("current_member_id");
		// 从数据库中查询自己的userID
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		db = databaseHelper.getWritableDatabase();
		String sql4 = "select memberID from MeMberInfoTable where isFamily ='"
				+ "1" + "'";
		Cursor cursor4 = db.rawQuery(sql4, null);
		while (cursor4.moveToNext()) {
			sys_userID = cursor4.getString(0);
		}
		// 初始化各种list
		listTemp = new ArrayList<>();
		listSettingTemp = new ArrayList<>();
		listGenerateTakeTime = new ArrayList<GenerateTakeDrugTimeList>();
		listGenerateTakeTime = (ArrayList) getArguments()
				.getCharSequenceArrayList("takeDrugTime");
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
		// 服药时间ListView
		String time[] = new String[100];
		takeDrugTime = (ListView) rootView.findViewById(R.id.takeDrugTime);
		list = new ArrayList<TakeDrugTimeItem>();
		// 存放应该生成的用药时间点，去除了重复的用药时间点之后
		String[] singleTime = new String[10];
		// 存放生成的用药时间点的个数，去除了重复的用药时间点之后
		int sizeTreeSet = 0;
		TreeSet<String> treeSet = new TreeSet<>();
		for (int m = 0; m < listGenerateTakeTime.size(); m++) {
			if (listGenerateTakeTime.get(m).getTakeTimeList().size() == 1) {

			} else {
				int j;
				for (j = 0; j < listGenerateTakeTime.get(m).getTakeTimeList()
						.size(); j++) {
					time[j] = listGenerateTakeTime.get(m).getTakeTimeList()
							.get(j).getTime();
				}
				for (int n = 0; n < j; n++) {
					// treeSet只会add不重复的元素，会自动过滤重复元素,里面存的是不重复的用药时间点,而且会自动排序
					treeSet.add(time[n]);
				}

			}
		}

		// sizeTreeSet代表该次生成的用药指导中没有重复的时间点个数
		sizeTreeSet = treeSet.size();
		for (int l = 0; l < sizeTreeSet; l++) {
			singleTime[l] = treeSet.pollFirst();
		}
		if (sizeTreeSet == 0) {
			sizeTreeSet = 1;
		}
		// 依照时间点遍历
		for (int i = 0; i < sizeTreeSet; i++) {
			setDrugNum = 0;
			takeDrugNum = 0;
			// 依照改时间点，依次遍历每条用药指导数据
			for (int j = 0; j < listGenerateTakeTime.size(); j++) {
				if (listGenerateTakeTime.get(j).getTakeTimeList().size() == 1) {
					listSettingTemp.add(listGenerateTakeTime.get(j));
					setDrugNum++;
				} else {
					// m代表一条药物的用药指导中服药列表中时间的个数
					int m = listGenerateTakeTime.get(j).getTakeTimeList()
							.size();
					int q;
					for (q = 0; q < m; q++) {
						String time1 = listGenerateTakeTime.get(j)
								.getTakeTimeList().get(q).getTime();
						if (time1.equals(singleTime[i])) {
							listTemp.add(listGenerateTakeTime.get(j));
							takeDrugNum++;
							break;
						}
					}
				}
			}

			// 传过来5条数据
			if (setDrugNum == 0 && takeDrugNum == 5) {
				params = 1;
			}
			if (setDrugNum == 1 && takeDrugNum == 4) {
				params = 2;
			}
			if (setDrugNum == 2 && takeDrugNum == 3) {
				params = 3;
			}
			if (setDrugNum == 3 && takeDrugNum == 2) {
				params = 4;
			}
			if (setDrugNum == 4 && takeDrugNum == 1) {
				params = 5;
			}
			if (setDrugNum == 5 && takeDrugNum == 0) {
				params = 6;
			}

			// 传过来4条数据
			if (setDrugNum == 0 && takeDrugNum == 4) {
				params = 7;
			}
			if (setDrugNum == 1 && takeDrugNum == 3) {
				params = 8;
			}
			if (setDrugNum == 2 && takeDrugNum == 2) {
				params = 9;
			}
			if (setDrugNum == 3 && takeDrugNum == 1) {
				params = 10;
			}
			if (setDrugNum == 4 && takeDrugNum == 0) {
				params = 11;
			}

			// 传过来3条数据
			if (setDrugNum == 0 && takeDrugNum == 3) {
				params = 12;
			}
			if (setDrugNum == 1 && takeDrugNum == 2) {
				params = 13;
			}
			if (setDrugNum == 2 && takeDrugNum == 1) {
				params = 14;
			}
			if (setDrugNum == 3 && takeDrugNum == 0) {
				params = 15;
			}

			// 传过来2条数据
			if (setDrugNum == 0 && takeDrugNum == 2) {
				params = 16;
			}
			if (setDrugNum == 1 && takeDrugNum == 1) {
				params = 17;
			}
			if (setDrugNum == 2 && takeDrugNum == 0) {
				params = 18;
			}

			// 传过来1条数据
			if (setDrugNum == 0 && takeDrugNum == 1) {
				params = 19;
			}
			if (setDrugNum == 1 && takeDrugNum == 0) {
				params = 20;
			}
			String doseUnit[] = new String[100];
			String doseUnitSetting[] = new String[100];
			databaseName = "drug_info";
			databaseHelper = new CreateSQLiteDatabase(getActivity(),
					databaseName, null, 1);
			db = databaseHelper.getWritableDatabase();
			// 取未生成用药时间点的数据的用药剂量
			for (int m = 0; m < listSettingTemp.size(); m++) {
				String sql2 = "select doseUnit from drugDoseUnitTable where doseUnitID ='"
						+ listSettingTemp.get(m).getDrugCountUnit() + "'";
				Cursor cursor2 = db.rawQuery(sql2, null);
				while (cursor2.moveToNext()) {
					doseUnitSetting[m] = cursor2.getString(0);
				}
			}
			// 取已经生成用药时间点的数据的用药剂量
			for (int j = 0; j < listTemp.size(); j++) {
				String sql2 = "select doseUnit from drugDoseUnitTable where doseUnitID ='"
						+ listTemp.get(j).getDrugCountUnit() + "'";
				Cursor cursor2 = db.rawQuery(sql2, null);
				while (cursor2.moveToNext()) {
					doseUnit[j] = cursor2.getString(0);
				}
			}
			// (需要手动设置的个数，已经生成的用药时间点的个数)
			switch (params) {

			// 5条数据
			// （0，5）
			case 1:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 0, 5, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), listTemp.get(4)
									.getDrugName(), "每次 "
									+ listTemp.get(4).getDrugCount() + " "
									+ doseUnit[4], listTemp.get(4).getBegin(),
							listTemp.get(4).getEnd()));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 0, 5, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), listTemp.get(4)
									.getDrugName(), "每次 "
									+ listTemp.get(4).getDrugCount() + " "
									+ doseUnit[4], listTemp.get(4).getBegin(),
							listTemp.get(4).getEnd()));
				}

				break;
			// (1,4)
			case 2:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 1, 4,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 1, 4,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), "", "", "", ""));
				}

				break;
			// (2,3)
			case 3:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 2, 3,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 2, 3,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				}

				break;
			// (3,2)
			case 4:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 3, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							listTemp.get(0).getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 3, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							listTemp.get(0).getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				}

				break;
			// (4,1)
			case 5:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 4, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 4, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				}

				break;
			// (5,0)
			// 时间点这里传空值，因为没有生成时间点
			case 6:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", "", 5, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), listSettingTemp.get(4)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(4).getDrugCount()
									+ " " + doseUnitSetting[4], listSettingTemp
									.get(4).getBegin(), listSettingTemp.get(4)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", "", 5, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), listSettingTemp.get(4)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(4).getDrugCount()
									+ " " + doseUnitSetting[4], listSettingTemp
									.get(4).getBegin(), listSettingTemp.get(4)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", ""));
				}

				break;

			// 4条数据
			// (0,4)
			case 7:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 0, 4, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 0, 4, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), listTemp.get(3)
									.getDrugName(), "每次 "
									+ listTemp.get(3).getDrugCount() + " "
									+ doseUnit[3], listTemp.get(3).getBegin(),
							listTemp.get(3).getEnd(), "", "", "", ""));
				}

				break;
			// (1,3)
			case 8:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 1, 3,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 1, 3,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				}

				break;
			// (2,2)
			case 9:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 2, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 2, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				}

				break;
			// (3,1)
			case 10:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 3, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							listTemp.get(0).getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 3, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							listTemp.get(0).getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				}

				break;
			// (4,0)
			case 11:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", "", 4, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", "", 4, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), listSettingTemp.get(3)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(3).getDrugCount()
									+ " " + doseUnitSetting[3], listSettingTemp
									.get(3).getBegin(), listSettingTemp.get(3)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", ""));
				}

				break;

			// 3条数据
			// (0,3)
			case 12:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 0, 3, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 0, 3, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), listTemp.get(2)
									.getDrugName(), "每次 "
									+ listTemp.get(2).getDrugCount() + " "
									+ doseUnit[2], listTemp.get(2).getBegin(),
							listTemp.get(2).getEnd(), "", "", "", "", "", "",
							"", ""));
				}

				break;
			// (1,2)
			case 13:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 1, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 1, 2,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				}

				break;
			// (2,1)
			case 14:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 2, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 2, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", listTemp.get(0).getDrugName(),
							"每次 " + listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				}

				break;
			// (3,0)
			case 15:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", "", 3, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", "", 3, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), listSettingTemp.get(2)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(2).getDrugCount()
									+ " " + doseUnitSetting[2], listSettingTemp
									.get(2).getBegin(), listSettingTemp.get(2)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", ""));
				}

				break;

			// 2条数据
			// (0,2)
			case 16:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 0, 2, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 0, 2, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), listTemp.get(1)
									.getDrugName(), "每次 "
									+ listTemp.get(1).getDrugCount() + " "
									+ doseUnit[1], listTemp.get(1).getBegin(),
							listTemp.get(1).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", ""));
				}

				break;
			// (1,1)
			case 17:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 1, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 1, 1,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				}

				break;
			// (2,0)
			case 18:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", "", 2, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", "", 2, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), listSettingTemp.get(1)
									.getDrugName(), "每次 "
									+ listSettingTemp.get(1).getDrugCount()
									+ " " + doseUnitSetting[1], listSettingTemp
									.get(1).getBegin(), listSettingTemp.get(1)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", ""));
				}

				break;

			// 1条数据
			// (0,1)
			case 19:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", singleTime[i], 0, 1, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", singleTime[i], 0, 1, "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", listTemp.get(0)
									.getDrugName(), "每次 "
									+ listTemp.get(0).getDrugCount() + " "
									+ doseUnit[0], listTemp.get(0).getBegin(),
							listTemp.get(0).getEnd(), "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", ""));
				}

				break;
			// (1,0)
			case 20:
				if (i == 0) {
					list.add(new TakeDrugTimeItem("1", "", 1, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", ""));
				} else {
					list.add(new TakeDrugTimeItem("0", "", 1, 0,
							listSettingTemp.get(0).getDrugName(), "每次 "
									+ listSettingTemp.get(0).getDrugCount()
									+ " " + doseUnitSetting[0], listSettingTemp
									.get(0).getBegin(), listSettingTemp.get(0)
									.getEnd(), "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", ""));
				}

				break;

			default:
				break;
			}
			// 为了下次去添加已经生成和未生成的用药数据，必须将ListTemp和ListSettingTemp中的数据清空
			listTemp = null;
			listTemp = new ArrayList<>();
			// listSettingTemp = null;
			// listSettingTemp = new ArrayList<>();
		}

		TakeDrugTimeInfoItemAdapter adapter = new TakeDrugTimeInfoItemAdapter();
		adapter.setContext(getActivity());
		adapter.setArrayList(list);
		takeDrugTime.setAdapter(adapter);
		// 返回
		back = (ImageView) rootView.findViewById(R.id.back);
		back.setOnClickListener(new MyClickBackListener());

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

	// 点击返回监视器 尼见 2015-03-25
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
							AddDrugFragment addDrugFragment = new AddDrugFragment();
							Bundle bundle = new Bundle();
							bundle.putString("drugName", "");
							addDrugFragment.setArguments(bundle);
							fragmentTransaction.setCustomAnimations(
									R.anim.enter_from_left,
									R.anim.exit_to_right);
							fragmentTransaction.replace(R.id.login_fragment,
									addDrugFragment, "addDrugFragment");
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

	// 生成用药时间点适配器 尼见 2015-04-02
	class TakeDrugTimeInfoItemAdapter extends BaseAdapter implements
			SectionIndexer {

		ArrayList<TakeDrugTimeItem> list;
		Context context;

		@Override
		public int getCount() {

			return list.size();

		}

		@Override
		public TakeDrugTimeItem getItem(int position) {

			TakeDrugTimeItem takeDrugTimeItem = list.get(position);

			return takeDrugTimeItem;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;

			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.take_drug_time_info_item, null);

			// 时间和设置部分
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.time.setOnClickListener(new MyClickTimeListener());
			viewHolder.line = (View) convertView.findViewById(R.id.line);
			viewHolder.setting = (TextView) convertView
					.findViewById(R.id.setting);
			viewHolder.line_setting = (View) convertView
					.findViewById(R.id.line_setting);

			// 时间和设置对应的绿色小三角
			viewHolder.triangle = (ImageView) convertView
					.findViewById(R.id.triangle);
			viewHolder.triangle_setting = (ImageView) convertView
					.findViewById(R.id.triangle_setting);

			viewHolder.line_1 = (View) convertView.findViewById(R.id.line_1);
			viewHolder.blue_line_1 = (View) convertView
					.findViewById(R.id.blue_line_1);
			viewHolder.content_1 = (RelativeLayout) convertView
					.findViewById(R.id.content_1);
			viewHolder.content_1
					.setOnClickListener(new MyClickContentOneListener());
			viewHolder.drug_name_1 = (TextView) convertView
					.findViewById(R.id.drug_name_1);
			viewHolder.drug_desc_1 = (TextView) convertView
					.findViewById(R.id.drug_desc_1);
			viewHolder.begin_time_1 = (TextView) convertView
					.findViewById(R.id.begin_time_1);
			viewHolder.end_time_1 = (TextView) convertView
					.findViewById(R.id.end_time_1);

			viewHolder.line_2 = (View) convertView.findViewById(R.id.line_2);
			viewHolder.blue_line_2 = (View) convertView
					.findViewById(R.id.blue_line_2);
			viewHolder.content_2 = (RelativeLayout) convertView
					.findViewById(R.id.content_2);
			viewHolder.content_2
					.setOnClickListener(new MyClickContentTwoListener());
			viewHolder.drug_name_2 = (TextView) convertView
					.findViewById(R.id.drug_name_2);
			viewHolder.drug_desc_2 = (TextView) convertView
					.findViewById(R.id.drug_desc_2);
			viewHolder.begin_time_2 = (TextView) convertView
					.findViewById(R.id.begin_time_2);
			viewHolder.end_time_2 = (TextView) convertView
					.findViewById(R.id.end_time_2);

			viewHolder.line_3 = (View) convertView.findViewById(R.id.line_3);
			viewHolder.blue_line_3 = (View) convertView
					.findViewById(R.id.blue_line_3);
			viewHolder.content_3 = (RelativeLayout) convertView
					.findViewById(R.id.content_3);
			viewHolder.content_3
					.setOnClickListener(new MyClickContentThreeListener());
			viewHolder.drug_name_3 = (TextView) convertView
					.findViewById(R.id.drug_name_3);
			viewHolder.drug_desc_3 = (TextView) convertView
					.findViewById(R.id.drug_desc_3);
			viewHolder.begin_time_3 = (TextView) convertView
					.findViewById(R.id.begin_time_3);
			viewHolder.end_time_3 = (TextView) convertView
					.findViewById(R.id.end_time_3);

			viewHolder.line_4 = (View) convertView.findViewById(R.id.line_4);
			viewHolder.blue_line_4 = (View) convertView
					.findViewById(R.id.blue_line_4);
			viewHolder.content_4 = (RelativeLayout) convertView
					.findViewById(R.id.content_4);
			viewHolder.content_4
					.setOnClickListener(new MyClickContentFourListener());
			viewHolder.drug_name_4 = (TextView) convertView
					.findViewById(R.id.drug_name_4);
			viewHolder.drug_desc_4 = (TextView) convertView
					.findViewById(R.id.drug_desc_4);
			viewHolder.begin_time_4 = (TextView) convertView
					.findViewById(R.id.begin_time_4);
			viewHolder.end_time_4 = (TextView) convertView
					.findViewById(R.id.end_time_4);

			viewHolder.line_5 = (View) convertView.findViewById(R.id.line_5);
			viewHolder.content_5 = (RelativeLayout) convertView
					.findViewById(R.id.content_5);
			viewHolder.content_5
					.setOnClickListener(new MyClickContentFiveListener());
			viewHolder.drug_name_5 = (TextView) convertView
					.findViewById(R.id.drug_name_5);
			viewHolder.drug_desc_5 = (TextView) convertView
					.findViewById(R.id.drug_desc_5);
			viewHolder.begin_time_5 = (TextView) convertView
					.findViewById(R.id.begin_time_5);
			viewHolder.end_time_5 = (TextView) convertView
					.findViewById(R.id.end_time_5);

			viewHolder.line_setting_1 = (View) convertView
					.findViewById(R.id.line_setting_1);
			viewHolder.blue_line_setting_1 = (View) convertView
					.findViewById(R.id.blue_line_setting_1);
			viewHolder.content_setting_1 = (RelativeLayout) convertView
					.findViewById(R.id.content_setting_1);
			viewHolder.content_setting_1
					.setOnClickListener(new MyClickContentSettingOneListener());
			viewHolder.drug_name_setting_1 = (TextView) convertView
					.findViewById(R.id.drug_name_setting_1);
			viewHolder.drug_desc_setting_1 = (TextView) convertView
					.findViewById(R.id.drug_desc_setting_1);
			viewHolder.begin_time_setting_1 = (TextView) convertView
					.findViewById(R.id.begin_time_setting_1);
			viewHolder.end_time_setting_1 = (TextView) convertView
					.findViewById(R.id.end_time_setting_1);

			viewHolder.line_setting_2 = (View) convertView
					.findViewById(R.id.line_setting_2);
			viewHolder.blue_line_setting_2 = (View) convertView
					.findViewById(R.id.blue_line_setting_2);
			viewHolder.content_setting_2 = (RelativeLayout) convertView
					.findViewById(R.id.content_setting_2);
			viewHolder.content_setting_2
					.setOnClickListener(new MyClickContentSettingTwoListener());
			viewHolder.drug_name_setting_2 = (TextView) convertView
					.findViewById(R.id.drug_name_setting_2);
			viewHolder.drug_desc_setting_2 = (TextView) convertView
					.findViewById(R.id.drug_desc_setting_2);
			viewHolder.begin_time_setting_2 = (TextView) convertView
					.findViewById(R.id.begin_time_setting_2);
			viewHolder.end_time_setting_2 = (TextView) convertView
					.findViewById(R.id.end_time_setting_2);

			viewHolder.line_setting_3 = (View) convertView
					.findViewById(R.id.line_setting_3);
			viewHolder.blue_line_setting_3 = (View) convertView
					.findViewById(R.id.blue_line_setting_3);
			viewHolder.content_setting_3 = (RelativeLayout) convertView
					.findViewById(R.id.content_setting_3);
			viewHolder.content_setting_3
					.setOnClickListener(new MyClickContentSettingThreeListener());
			viewHolder.drug_name_setting_3 = (TextView) convertView
					.findViewById(R.id.drug_name_setting_3);
			viewHolder.drug_desc_setting_3 = (TextView) convertView
					.findViewById(R.id.drug_desc_setting_3);
			viewHolder.begin_time_setting_3 = (TextView) convertView
					.findViewById(R.id.begin_time_setting_3);
			viewHolder.end_time_setting_3 = (TextView) convertView
					.findViewById(R.id.end_time_setting_3);

			viewHolder.line_setting_4 = (View) convertView
					.findViewById(R.id.line_setting_4);
			viewHolder.blue_line_setting_4 = (View) convertView
					.findViewById(R.id.blue_line_setting_4);
			viewHolder.content_setting_4 = (RelativeLayout) convertView
					.findViewById(R.id.content_setting_4);
			viewHolder.content_setting_4
					.setOnClickListener(new MyClickContentSettingFourListener());
			viewHolder.drug_name_setting_4 = (TextView) convertView
					.findViewById(R.id.drug_name_setting_4);
			viewHolder.drug_desc_setting_4 = (TextView) convertView
					.findViewById(R.id.drug_desc_setting_4);
			viewHolder.begin_time_setting_4 = (TextView) convertView
					.findViewById(R.id.begin_time_setting_4);
			viewHolder.end_time_setting_4 = (TextView) convertView
					.findViewById(R.id.end_time_setting_4);

			viewHolder.line_setting_5 = (View) convertView
					.findViewById(R.id.line_setting_5);
			viewHolder.content_setting_5 = (RelativeLayout) convertView
					.findViewById(R.id.content_setting_5);
			viewHolder.content_setting_5
					.setOnClickListener(new MyClickContentSettingFiveListener());
			viewHolder.drug_name_setting_5 = (TextView) convertView
					.findViewById(R.id.drug_name_setting_5);
			viewHolder.drug_desc_setting_5 = (TextView) convertView
					.findViewById(R.id.drug_desc_setting_5);
			viewHolder.begin_time_setting_5 = (TextView) convertView
					.findViewById(R.id.begin_time_setting_5);
			viewHolder.end_time_setting_5 = (TextView) convertView
					.findViewById(R.id.end_time_setting_5);

			int takeDrugNum = list.get(position).getTake_drug_num();
			int setDrugNum = list.get(position).getSet_drug_num();
			int num_setting = 1;
			if (setDrugNum == 0 && takeDrugNum == 5) {
				num_setting = 1;
			}
			if (setDrugNum == 1 && takeDrugNum == 4) {
				num_setting = 2;
			}
			if (setDrugNum == 2 && takeDrugNum == 3) {
				num_setting = 3;
			}
			if (setDrugNum == 3 && takeDrugNum == 2) {
				num_setting = 4;
			}
			if (setDrugNum == 4 && takeDrugNum == 1) {
				num_setting = 5;
			}
			if (setDrugNum == 5 && takeDrugNum == 0) {
				num_setting = 6;
			}
			if (setDrugNum == 0 && takeDrugNum == 4) {
				num_setting = 7;
			}
			if (setDrugNum == 1 && takeDrugNum == 3) {
				num_setting = 8;
			}
			if (setDrugNum == 2 && takeDrugNum == 2) {
				num_setting = 9;
			}
			if (setDrugNum == 3 && takeDrugNum == 1) {
				num_setting = 10;
			}
			if (setDrugNum == 4 && takeDrugNum == 0) {
				num_setting = 11;
			}
			if (setDrugNum == 0 && takeDrugNum == 3) {
				num_setting = 12;
			}
			if (setDrugNum == 1 && takeDrugNum == 2) {
				num_setting = 13;
			}
			if (setDrugNum == 2 && takeDrugNum == 1) {
				num_setting = 14;
			}
			if (setDrugNum == 3 && takeDrugNum == 0) {
				num_setting = 15;
			}
			if (setDrugNum == 0 && takeDrugNum == 2) {
				num_setting = 16;
			}
			if (setDrugNum == 1 && takeDrugNum == 1) {
				num_setting = 17;
			}
			if (setDrugNum == 2 && takeDrugNum == 0) {
				num_setting = 18;
			}
			if (setDrugNum == 0 && takeDrugNum == 1) {
				num_setting = 19;
			}
			if (setDrugNum == 1 && takeDrugNum == 0) {
				num_setting = 20;
			}
			String time;
			String timeArray[] = new String[10];
			int hour, minute, timeValue;
			switch (num_setting) {
			// (0,5)
			case 1:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
				} else {
					// 不是第一次添加，隐藏设置栏
				}
				// 设置的栏和那条粗线
				viewHolder.setting.setVisibility(View.GONE);
				viewHolder.line_setting.setVisibility(View.GONE);
				// 绿色小三角
				viewHolder.triangle_setting.setVisibility(View.GONE);
				// 字底下的蓝线
				viewHolder.blue_line_setting_1.setVisibility(View.GONE);
				viewHolder.blue_line_setting_2.setVisibility(View.GONE);
				viewHolder.blue_line_setting_3.setVisibility(View.GONE);
				viewHolder.blue_line_setting_4.setVisibility(View.GONE);
				// 左侧和内容对应的粗线
				viewHolder.line_setting_1.setVisibility(View.GONE);
				viewHolder.line_setting_2.setVisibility(View.GONE);
				viewHolder.line_setting_3.setVisibility(View.GONE);
				viewHolder.line_setting_4.setVisibility(View.GONE);
				viewHolder.line_setting_5.setVisibility(View.GONE);
				// 整体的内容框
				viewHolder.content_setting_1.setVisibility(View.GONE);
				viewHolder.content_setting_2.setVisibility(View.GONE);
				viewHolder.content_setting_3.setVisibility(View.GONE);
				viewHolder.content_setting_4.setVisibility(View.GONE);
				viewHolder.content_setting_5.setVisibility(View.GONE);

				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());
				time = viewHolder.time.getText().toString();
				timeArray = time.split(":");
				hour = Integer.parseInt(timeArray[0]);
				minute = Integer.parseInt(timeArray[1]);
				timeValue = hour * 100 + minute;
				// 根据时间决定时间旁边的图标
				if (timeValue < 1000) {
					// 必须按照这种格式去用程序加载图片，否则图片会不显示
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzao);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1000 && timeValue < 1600) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzhong);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1600 && timeValue <= 2359) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvwan);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				// 设置时间栏
				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());

				// 分别设置服药时间
				viewHolder.drug_name_1.setText(this.list.get(position)
						.getDrug_name_1());
				viewHolder.drug_desc_1.setText(this.list.get(position)
						.getDrug_desc_1());
				viewHolder.begin_time_1.setText(this.list.get(position)
						.getBegin_date_1());
				viewHolder.end_time_1.setText(this.list.get(position)
						.getEnd_date_1());

				viewHolder.drug_name_2.setText(this.list.get(position)
						.getDrug_name_2());
				viewHolder.drug_desc_2.setText(this.list.get(position)
						.getDrug_desc_2());
				viewHolder.begin_time_2.setText(this.list.get(position)
						.getBegin_date_2());
				viewHolder.end_time_2.setText(this.list.get(position)
						.getEnd_date_2());

				viewHolder.drug_name_3.setText(this.list.get(position)
						.getDrug_name_3());
				viewHolder.drug_desc_3.setText(this.list.get(position)
						.getDrug_desc_3());
				viewHolder.begin_time_3.setText(this.list.get(position)
						.getBegin_date_3());
				viewHolder.end_time_3.setText(this.list.get(position)
						.getEnd_date_3());

				viewHolder.drug_name_4.setText(this.list.get(position)
						.getDrug_name_4());
				viewHolder.drug_desc_4.setText(this.list.get(position)
						.getDrug_desc_4());
				viewHolder.begin_time_4.setText(this.list.get(position)
						.getBegin_date_4());
				viewHolder.end_time_4.setText(this.list.get(position)
						.getEnd_date_4());

				viewHolder.drug_name_5.setText(this.list.get(position)
						.getDrug_name_5());
				viewHolder.drug_desc_5.setText(this.list.get(position)
						.getDrug_desc_5());
				viewHolder.begin_time_5.setText(this.list.get(position)
						.getBegin_date_5());
				viewHolder.end_time_5.setText(this.list.get(position)
						.getEnd_date_5());

				break;
			// (1,4)
			case 2:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

					viewHolder.drug_name_4.setText(this.list.get(position)
							.getDrug_name_4());
					viewHolder.drug_desc_4.setText(this.list.get(position)
							.getDrug_desc_4());
					viewHolder.begin_time_4.setText(this.list.get(position)
							.getBegin_date_4());
					viewHolder.end_time_4.setText(this.list.get(position)
							.getEnd_date_4());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);

					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

					viewHolder.drug_name_4.setText(this.list.get(position)
							.getDrug_name_4());
					viewHolder.drug_desc_4.setText(this.list.get(position)
							.getDrug_desc_4());
					viewHolder.begin_time_4.setText(this.list.get(position)
							.getBegin_date_4());
					viewHolder.end_time_4.setText(this.list.get(position)
							.getEnd_date_4());
				}

				break;
			// (2,3)
			case 3:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				}

				break;
			// (3,2)
			case 4:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_5.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

				}

				break;
			// (4,1)
			case 5:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 整体的内容框
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());
					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());
					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				}

				break;
			// (5,0)
			case 6:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);
					// 字底部的蓝线
					viewHolder.blue_line_1.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					// 内容部分和侧边的白线
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());

					viewHolder.drug_name_setting_5.setText(this.list.get(
							position).getDrug_name_setting_5());
					viewHolder.drug_desc_setting_5.setText(this.list.get(
							position).getDrug_desc_setting_5());
					viewHolder.begin_time_setting_5.setText(this.list.get(
							position).getBegin_date_setting_5());
					viewHolder.end_time_setting_5.setText(this.list.get(
							position).getEnd_date_setting_5());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);
					// 字底部的蓝线
					viewHolder.blue_line_1.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					// 内容部分和侧边的白线
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());

					viewHolder.drug_name_setting_5.setText(this.list.get(
							position).getDrug_name_setting_5());
					viewHolder.drug_desc_setting_5.setText(this.list.get(
							position).getDrug_desc_setting_5());
					viewHolder.begin_time_setting_5.setText(this.list.get(
							position).getBegin_date_setting_5());
					viewHolder.end_time_setting_5.setText(this.list.get(
							position).getEnd_date_setting_5());

				}

				break;
			// (0,4)
			case 7:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
				} else {
					// 不是第一次添加，隐藏设置栏
				}
				// 设置的栏和那条粗线
				viewHolder.setting.setVisibility(View.GONE);
				viewHolder.line_setting.setVisibility(View.GONE);
				// 绿色三角
				viewHolder.triangle_setting.setVisibility(View.GONE);
				// 字底下的蓝线
				viewHolder.blue_line_setting_1.setVisibility(View.GONE);
				viewHolder.blue_line_setting_2.setVisibility(View.GONE);
				viewHolder.blue_line_setting_3.setVisibility(View.GONE);
				viewHolder.blue_line_setting_4.setVisibility(View.GONE);
				// 左侧粗线
				viewHolder.line_setting_1.setVisibility(View.GONE);
				viewHolder.line_setting_2.setVisibility(View.GONE);
				viewHolder.line_setting_3.setVisibility(View.GONE);
				viewHolder.line_setting_4.setVisibility(View.GONE);
				viewHolder.line_setting_5.setVisibility(View.GONE);
				// 整体的内容框
				viewHolder.content_setting_1.setVisibility(View.GONE);
				viewHolder.content_setting_2.setVisibility(View.GONE);
				viewHolder.content_setting_3.setVisibility(View.GONE);
				viewHolder.content_setting_4.setVisibility(View.GONE);
				viewHolder.content_setting_5.setVisibility(View.GONE);
				// 下面已经生成时间点的内容设置
				viewHolder.blue_line_4.setVisibility(View.GONE);
				viewHolder.line_5.setVisibility(View.GONE);
				viewHolder.content_5.setVisibility(View.GONE);

				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());
				time = viewHolder.time.getText().toString();
				timeArray = time.split(":");
				hour = Integer.parseInt(timeArray[0]);
				minute = Integer.parseInt(timeArray[1]);
				timeValue = hour * 100 + minute;
				// 根据时间决定时间旁边的图标
				if (timeValue < 1000) {
					// 必须按照这种格式去用程序加载图片，否则图片会不显示
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzao);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1000 && timeValue < 1600) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzhong);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1600 && timeValue <= 2359) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvwan);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				// 设置时间栏
				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());

				// 分别设置服药时间
				viewHolder.drug_name_1.setText(this.list.get(position)
						.getDrug_name_1());
				viewHolder.drug_desc_1.setText(this.list.get(position)
						.getDrug_desc_1());
				viewHolder.begin_time_1.setText(this.list.get(position)
						.getBegin_date_1());
				viewHolder.end_time_1.setText(this.list.get(position)
						.getEnd_date_1());

				viewHolder.drug_name_2.setText(this.list.get(position)
						.getDrug_name_2());
				viewHolder.drug_desc_2.setText(this.list.get(position)
						.getDrug_desc_2());
				viewHolder.begin_time_2.setText(this.list.get(position)
						.getBegin_date_2());
				viewHolder.end_time_2.setText(this.list.get(position)
						.getEnd_date_2());

				viewHolder.drug_name_3.setText(this.list.get(position)
						.getDrug_name_3());
				viewHolder.drug_desc_3.setText(this.list.get(position)
						.getDrug_desc_3());
				viewHolder.begin_time_3.setText(this.list.get(position)
						.getBegin_date_3());
				viewHolder.end_time_3.setText(this.list.get(position)
						.getEnd_date_3());

				viewHolder.drug_name_4.setText(this.list.get(position)
						.getDrug_name_4());
				viewHolder.drug_desc_4.setText(this.list.get(position)
						.getDrug_desc_4());
				viewHolder.begin_time_4.setText(this.list.get(position)
						.getBegin_date_4());
				viewHolder.end_time_4.setText(this.list.get(position)
						.getEnd_date_4());

				break;
			// (1,3)
			case 8:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				}

				break;
			// (2,2)
			case 9:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());
				}

				break;
			// (3,1)
			case 10:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				}

				break;
			// (4,0)
			case 11:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 整体的内容框
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 左侧粗线
					// viewHolder.line_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());

					viewHolder.drug_name_setting_4.setText(this.list.get(
							position).getDrug_name_setting_4());
					viewHolder.drug_desc_setting_4.setText(this.list.get(
							position).getDrug_desc_setting_4());
					viewHolder.begin_time_setting_4.setText(this.list.get(
							position).getBegin_date_setting_4());
					viewHolder.end_time_setting_4.setText(this.list.get(
							position).getEnd_date_setting_4());

				}

				break;
			// (0,3)
			case 12:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 设置的栏和那条粗线
					viewHolder.setting.setVisibility(View.GONE);
					viewHolder.line_setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下面已经生成时间点的内容设置
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下面已经生成时间点的内容设置
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

					viewHolder.drug_name_3.setText(this.list.get(position)
							.getDrug_name_3());
					viewHolder.drug_desc_3.setText(this.list.get(position)
							.getDrug_desc_3());
					viewHolder.begin_time_3.setText(this.list.get(position)
							.getBegin_date_3());
					viewHolder.end_time_3.setText(this.list.get(position)
							.getEnd_date_3());

				}

				break;
			// (1,2)
			case 13:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

					viewHolder.drug_name_2.setText(this.list.get(position)
							.getDrug_name_2());
					viewHolder.drug_desc_2.setText(this.list.get(position)
							.getDrug_desc_2());
					viewHolder.begin_time_2.setText(this.list.get(position)
							.getBegin_date_2());
					viewHolder.end_time_2.setText(this.list.get(position)
							.getEnd_date_2());

				}

				break;
			// (2,1)
			case 14:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧粗线
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());
				}

				break;
			// (3,0)
			case 15:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 整体的内容框
					viewHolder.content_setting_5.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					// viewHolder.line_setting_5.setVisibility(View.GONE);
					// viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

					viewHolder.drug_name_setting_3.setText(this.list.get(
							position).getDrug_name_setting_3());
					viewHolder.drug_desc_setting_3.setText(this.list.get(
							position).getDrug_desc_setting_3());
					viewHolder.begin_time_setting_3.setText(this.list.get(
							position).getBegin_date_setting_3());
					viewHolder.end_time_setting_3.setText(this.list.get(
							position).getEnd_date_setting_3());
				}

				break;
			// (0,2)
			case 16:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
				} else {
					// 不是第一次添加，隐藏设置栏
				}
				// 设置的栏和那条粗线
				viewHolder.setting.setVisibility(View.GONE);
				viewHolder.line_setting.setVisibility(View.GONE);
				// 绿色三角
				viewHolder.triangle.setVisibility(View.GONE);
				// 字底下的蓝线
				viewHolder.blue_line_setting_1.setVisibility(View.GONE);
				viewHolder.blue_line_setting_2.setVisibility(View.GONE);
				viewHolder.blue_line_setting_3.setVisibility(View.GONE);
				viewHolder.blue_line_setting_4.setVisibility(View.GONE);
				// 左侧的每个区域对应的粗线
				viewHolder.line_setting_1.setVisibility(View.GONE);
				viewHolder.line_setting_2.setVisibility(View.GONE);
				viewHolder.line_setting_3.setVisibility(View.GONE);
				viewHolder.line_setting_4.setVisibility(View.GONE);
				viewHolder.line_setting_5.setVisibility(View.GONE);
				// 整体的内容框
				viewHolder.content_setting_1.setVisibility(View.GONE);
				viewHolder.content_setting_2.setVisibility(View.GONE);
				viewHolder.content_setting_3.setVisibility(View.GONE);
				viewHolder.content_setting_4.setVisibility(View.GONE);
				viewHolder.content_setting_5.setVisibility(View.GONE);
				// 下面已经生成时间点的内容设置
				viewHolder.blue_line_4.setVisibility(View.GONE);
				viewHolder.line_5.setVisibility(View.GONE);
				viewHolder.content_5.setVisibility(View.GONE);
				viewHolder.blue_line_3.setVisibility(View.GONE);
				viewHolder.line_4.setVisibility(View.GONE);
				viewHolder.content_4.setVisibility(View.GONE);
				viewHolder.blue_line_2.setVisibility(View.GONE);
				viewHolder.line_3.setVisibility(View.GONE);
				viewHolder.content_3.setVisibility(View.GONE);

				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());
				time = viewHolder.time.getText().toString();
				timeArray = time.split(":");
				hour = Integer.parseInt(timeArray[0]);
				minute = Integer.parseInt(timeArray[1]);
				timeValue = hour * 100 + minute;
				// 根据时间决定时间旁边的图标
				if (timeValue < 1000) {
					// 必须按照这种格式去用程序加载图片，否则图片会不显示
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzao);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1000 && timeValue < 1600) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzhong);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1600 && timeValue <= 2359) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvwan);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				// 设置时间栏
				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());

				// 分别设置服药时间
				viewHolder.drug_name_1.setText(this.list.get(position)
						.getDrug_name_1());
				viewHolder.drug_desc_1.setText(this.list.get(position)
						.getDrug_desc_1());
				viewHolder.begin_time_1.setText(this.list.get(position)
						.getBegin_date_1());
				viewHolder.end_time_1.setText(this.list.get(position)
						.getEnd_date_1());

				viewHolder.drug_name_2.setText(this.list.get(position)
						.getDrug_name_2());
				viewHolder.drug_desc_2.setText(this.list.get(position)
						.getDrug_desc_2());
				viewHolder.begin_time_2.setText(this.list.get(position)
						.getBegin_date_2());
				viewHolder.end_time_2.setText(this.list.get(position)
						.getEnd_date_2());

				break;
			// (1,1)
			case 17:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 左侧的每个区域对应的粗线
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);

					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());
					time = viewHolder.time.getText().toString();
					timeArray = time.split(":");
					hour = Integer.parseInt(timeArray[0]);
					minute = Integer.parseInt(timeArray[1]);
					timeValue = hour * 100 + minute;
					// 根据时间决定时间旁边的图标
					if (timeValue < 1000) {
						// 必须按照这种格式去用程序加载图片，否则图片会不显示
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzao);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1000 && timeValue < 1600) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvzhong);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					if (timeValue >= 1600 && timeValue <= 2359) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.lvwan);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						viewHolder.time.setCompoundDrawables(null, null,
								drawable, null);
					}
					// 设置时间栏
					viewHolder.time.setText(this.list.get(position)
							.getTake_drug_time());

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					// 分别设置服药时间
					viewHolder.drug_name_1.setText(this.list.get(position)
							.getDrug_name_1());
					viewHolder.drug_desc_1.setText(this.list.get(position)
							.getDrug_desc_1());
					viewHolder.begin_time_1.setText(this.list.get(position)
							.getBegin_date_1());
					viewHolder.end_time_1.setText(this.list.get(position)
							.getEnd_date_1());

				}

				break;
			// (2,0)
			case 18:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 整体的内容框
					viewHolder.content_setting_5.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					// viewHolder.line_setting_5.setVisibility(View.GONE);
					// viewHolder.line_setting_4.setVisibility(View.GONE);
					// viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());

					viewHolder.drug_name_setting_2.setText(this.list.get(
							position).getDrug_name_setting_2());
					viewHolder.drug_desc_setting_2.setText(this.list.get(
							position).getDrug_desc_setting_2());
					viewHolder.begin_time_setting_2.setText(this.list.get(
							position).getBegin_date_setting_2());
					viewHolder.end_time_setting_2.setText(this.list.get(
							position).getEnd_date_setting_2());

				}

				break;
			// (0,1)
			case 19:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
				} else {
					// 不是第一次添加，隐藏设置栏
				}
				// 设置的栏和那条粗线
				viewHolder.setting.setVisibility(View.GONE);
				viewHolder.line_setting.setVisibility(View.GONE);
				// 绿色三角
				viewHolder.triangle_setting.setVisibility(View.GONE);
				// 字底下的蓝线
				viewHolder.blue_line_setting_1.setVisibility(View.GONE);
				viewHolder.blue_line_setting_2.setVisibility(View.GONE);
				viewHolder.blue_line_setting_3.setVisibility(View.GONE);
				viewHolder.blue_line_setting_4.setVisibility(View.GONE);
				// 左侧的每个区域对应的粗线
				viewHolder.line_setting_1.setVisibility(View.GONE);
				viewHolder.line_setting_2.setVisibility(View.GONE);
				viewHolder.line_setting_3.setVisibility(View.GONE);
				viewHolder.line_setting_4.setVisibility(View.GONE);
				viewHolder.line_setting_5.setVisibility(View.GONE);
				// 整体的内容框
				viewHolder.content_setting_1.setVisibility(View.GONE);
				viewHolder.content_setting_2.setVisibility(View.GONE);
				viewHolder.content_setting_3.setVisibility(View.GONE);
				viewHolder.content_setting_4.setVisibility(View.GONE);
				viewHolder.content_setting_5.setVisibility(View.GONE);
				// 下面已经生成时间点的内容设置
				viewHolder.blue_line_4.setVisibility(View.GONE);
				viewHolder.line_5.setVisibility(View.GONE);
				viewHolder.content_5.setVisibility(View.GONE);
				viewHolder.blue_line_3.setVisibility(View.GONE);
				viewHolder.line_4.setVisibility(View.GONE);
				viewHolder.content_4.setVisibility(View.GONE);
				viewHolder.blue_line_2.setVisibility(View.GONE);
				viewHolder.line_3.setVisibility(View.GONE);
				viewHolder.content_3.setVisibility(View.GONE);
				viewHolder.blue_line_1.setVisibility(View.GONE);
				viewHolder.line_2.setVisibility(View.GONE);
				viewHolder.content_2.setVisibility(View.GONE);

				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());
				time = viewHolder.time.getText().toString();
				timeArray = time.split(":");
				hour = Integer.parseInt(timeArray[0]);
				minute = Integer.parseInt(timeArray[1]);
				timeValue = hour * 100 + minute;
				// 根据时间决定时间旁边的图标
				if (timeValue < 1000) {
					// 必须按照这种格式去用程序加载图片，否则图片会不显示
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzao);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1000 && timeValue < 1600) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvzhong);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				if (timeValue >= 1600 && timeValue <= 2359) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.lvwan);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					viewHolder.time.setCompoundDrawables(null, null, drawable,
							null);
				}
				// 设置时间栏
				viewHolder.time.setText(this.list.get(position)
						.getTake_drug_time());

				// 分别设置服药时间
				viewHolder.drug_name_1.setText(this.list.get(position)
						.getDrug_name_1());
				viewHolder.drug_desc_1.setText(this.list.get(position)
						.getDrug_desc_1());
				viewHolder.begin_time_1.setText(this.list.get(position)
						.getBegin_date_1());
				viewHolder.end_time_1.setText(this.list.get(position)
						.getEnd_date_1());

				break;
			// (1,0)
			case 20:
				if (this.list.get(position).getIsFirstAdd().equals("1")) {
					// 是第一次添加，显示设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 左侧的每个区域对应的粗线
					// viewHolder.line_setting_2.setVisibility(View.GONE);
					// viewHolder.line_setting_3.setVisibility(View.GONE);
					// viewHolder.line_setting_4.setVisibility(View.GONE);
					// viewHolder.line_setting_5.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());
				} else {
					// 不是第一次添加，隐藏设置栏
					// 字底下的蓝线
					viewHolder.blue_line_setting_1.setVisibility(View.GONE);
					viewHolder.blue_line_setting_2.setVisibility(View.GONE);
					viewHolder.blue_line_setting_3.setVisibility(View.GONE);
					viewHolder.blue_line_setting_4.setVisibility(View.GONE);
					// 左侧的粗线
					viewHolder.line_setting_1.setVisibility(View.GONE);
					viewHolder.line_setting_2.setVisibility(View.GONE);
					viewHolder.line_setting_3.setVisibility(View.GONE);
					viewHolder.line_setting_4.setVisibility(View.GONE);
					viewHolder.line_setting_5.setVisibility(View.GONE);
					// 整体的内容框
					viewHolder.content_setting_1.setVisibility(View.GONE);
					viewHolder.content_setting_2.setVisibility(View.GONE);
					viewHolder.content_setting_3.setVisibility(View.GONE);
					viewHolder.content_setting_4.setVisibility(View.GONE);
					viewHolder.content_setting_5.setVisibility(View.GONE);
					// 设置栏
					viewHolder.line_setting.setVisibility(View.GONE);
					viewHolder.setting.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle_setting.setVisibility(View.GONE);
					// 下方的生成时间点的部分隐藏
					viewHolder.line_5.setVisibility(View.GONE);
					viewHolder.content_5.setVisibility(View.GONE);
					viewHolder.blue_line_4.setVisibility(View.GONE);
					viewHolder.line_4.setVisibility(View.GONE);
					viewHolder.content_4.setVisibility(View.GONE);
					viewHolder.blue_line_3.setVisibility(View.GONE);
					viewHolder.line_3.setVisibility(View.GONE);
					viewHolder.content_3.setVisibility(View.GONE);
					viewHolder.blue_line_2.setVisibility(View.GONE);
					viewHolder.line_2.setVisibility(View.GONE);
					viewHolder.content_2.setVisibility(View.GONE);
					viewHolder.blue_line_1.setVisibility(View.GONE);
					viewHolder.line_1.setVisibility(View.GONE);
					viewHolder.content_1.setVisibility(View.GONE);
					// 时间设置栏
					viewHolder.time.setVisibility(View.GONE);
					viewHolder.line.setVisibility(View.GONE);
					// 绿色三角
					viewHolder.triangle.setVisibility(View.GONE);

					// 未生成服药时间的栏目设置
					viewHolder.drug_name_setting_1.setText(this.list.get(
							position).getDrug_name_setting_1());
					viewHolder.drug_desc_setting_1.setText(this.list.get(
							position).getDrug_desc_setting_1());
					viewHolder.begin_time_setting_1.setText(this.list.get(
							position).getBegin_date_setting_1());
					viewHolder.end_time_setting_1.setText(this.list.get(
							position).getEnd_date_setting_1());
				}

				break;
			default:
				break;
			}

			return convertView;
		}

		public void setMargins(View v, int l, int t, int r, int b) {
			if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
				ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
						.getLayoutParams();
				p.setMargins(l, t, r, b);
				v.requestLayout();
			}
		}

		// 生成用药提示那个页面的各个组件全部在viewHolder中定义和使用 尼见 2015-04-07
		final class ViewHolder {

			// 顶部的时间设置栏及其上面的粗线
			TextView time, setting;
			View line, line_setting;
			// 生成用药时间点和未生成用药时间点对应的绿色的小三角
			ImageView triangle, triangle_setting;
			// 以下分别的内容设置栏及其对应的左侧的粗线
			TextView drug_name_1, drug_name_setting_1;
			TextView drug_desc_1, drug_desc_setting_1;
			TextView begin_time_1, begin_time_setting_1;
			TextView end_time_1, end_time_setting_1;
			View line_1, blue_line_1, line_setting_1, blue_line_setting_1;
			RelativeLayout content_1, content_setting_1;

			TextView drug_name_2, drug_name_setting_2;
			TextView drug_desc_2, drug_desc_setting_2;
			TextView begin_time_2, begin_time_setting_2;
			TextView end_time_2, end_time_setting_2;
			View line_2, blue_line_2, line_setting_2, blue_line_setting_2;
			RelativeLayout content_2, content_setting_2;

			TextView drug_name_3, drug_name_setting_3;
			TextView drug_desc_3, drug_desc_setting_3;
			TextView begin_time_3, begin_time_setting_3;
			TextView end_time_3, end_time_setting_3;
			View line_3, blue_line_3, line_setting_3, blue_line_setting_3;
			RelativeLayout content_3, content_setting_3;

			TextView drug_name_4, drug_name_setting_4;
			TextView drug_desc_4, drug_desc_setting_4;
			TextView begin_time_4, begin_time_setting_4;
			TextView end_time_4, end_time_setting_4;
			View line_4, blue_line_4, line_setting_4, blue_line_setting_4;
			RelativeLayout content_4, content_setting_4;

			TextView drug_name_5, drug_name_setting_5;
			TextView drug_desc_5, drug_desc_setting_5;
			TextView begin_time_5, begin_time_setting_5;
			TextView end_time_5, end_time_setting_5;
			View line_5, line_setting_5, blue_line_setting_5;
			RelativeLayout content_5, content_setting_5;

		}

		public void setContext(Context context) {
			this.context = context;
		}

		public void setArrayList(ArrayList<TakeDrugTimeItem> list) {
			this.list = list;
		}

		@Override
		public Object[] getSections() {
			return new Object[0];
		}

		@Override
		public int getPositionForSection(int section) {

			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		public void updateListView(ArrayList<TakeDrugTimeItem> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		// 点击时间监视器 尼见 2015-04-07
		class MyClickTimeListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {

			}
		}

		// 点击content_setting_1监视器 尼见 2015-04-07
		class MyClickContentSettingOneListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SetTakeDrugTimeFragment setTakeDrugTimeFragment = new SetTakeDrugTimeFragment();
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("takeDrugTime",
						(ArrayList) listGenerateTakeTime);
				bundle.putString("takeDrugFrequency", listSettingTemp.get(0)
						.getCycleCount());
				// setNum代表将要设置的第几项内容
				bundle.putInt("setNum", 0);
				bundle.putInt("location", listSettingTemp.get(0).getLocation());
				setTakeDrugTimeFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						setTakeDrugTimeFragment, "setTakeDrugTimeFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}

		// 点击content_setting_2监视器 尼见 2015-04-07
		class MyClickContentSettingTwoListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SetTakeDrugTimeFragment setTakeDrugTimeFragment = new SetTakeDrugTimeFragment();
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("takeDrugTime",
						(ArrayList) listGenerateTakeTime);
				bundle.putString("takeDrugFrequency", listSettingTemp.get(1)
						.getCycleCount());
				bundle.putInt("setNum", 1);
				bundle.putInt("location", listSettingTemp.get(1).getLocation());
				setTakeDrugTimeFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						setTakeDrugTimeFragment, "setTakeDrugTimeFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}

		// 点击content_setting_3监视器 尼见 2015-04-07
		class MyClickContentSettingThreeListener implements
				View.OnClickListener {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SetTakeDrugTimeFragment setTakeDrugTimeFragment = new SetTakeDrugTimeFragment();
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("takeDrugTime",
						(ArrayList) listGenerateTakeTime);
				bundle.putString("takeDrugFrequency", listSettingTemp.get(2)
						.getCycleCount());
				bundle.putInt("setNum", 2);
				bundle.putInt("location", listSettingTemp.get(2).getLocation());
				setTakeDrugTimeFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						setTakeDrugTimeFragment, "setTakeDrugTimeFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}

		// 点击content_setting_4监视器 尼见 2015-04-07
		class MyClickContentSettingFourListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SetTakeDrugTimeFragment setTakeDrugTimeFragment = new SetTakeDrugTimeFragment();
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("takeDrugTime",
						(ArrayList) listGenerateTakeTime);
				bundle.putString("takeDrugFrequency", listSettingTemp.get(3)
						.getCycleCount());
				bundle.putInt("setNum", 3);
				bundle.putInt("location", listSettingTemp.get(3).getLocation());
				setTakeDrugTimeFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						setTakeDrugTimeFragment, "setTakeDrugTimeFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}

		// 点击content_setting_5监视器 尼见 2015-04-07
		class MyClickContentSettingFiveListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				SetTakeDrugTimeFragment setTakeDrugTimeFragment = new SetTakeDrugTimeFragment();
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("takeDrugTime",
						(ArrayList) listGenerateTakeTime);
				bundle.putString("takeDrugFrequency", listSettingTemp.get(4)
						.getCycleCount());
				bundle.putInt("setNum", 4);
				bundle.putInt("location", listSettingTemp.get(4).getLocation());
				setTakeDrugTimeFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(
						R.anim.enter_from_right, R.anim.exit_to_left);
				fragmentTransaction.replace(R.id.login_fragment,
						setTakeDrugTimeFragment, "setTakeDrugTimeFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}

		// 点击content_1监视器 尼见 2015-04-07
		class MyClickContentOneListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {

			}
		}

		// 点击content_2监视器 尼见 2015-04-07
		class MyClickContentTwoListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {

			}
		}

		// 点击content_3监视器 尼见 2015-04-07
		class MyClickContentThreeListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {

			}
		}

		// 点击content_4监视器 尼见 2015-04-07
		class MyClickContentFourListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {

			}
		}

		// 点击content_5监视器 尼见 2015-04-07
		class MyClickContentFiveListener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				// 添加的药品将会清除提示框
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithTest();
						dialog.show();

					}
				});
			}

			// 添加药品将会消失提示框 尼见 2015-03-23
			AlertDialog getAlertDialogWithTest() {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity(),
						android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

				builder.setTitle("提示");
				builder.setMessage("我是content的内容，我被点击了");
				builder.setPositiveButton("确定", null);
				builder.setNeutralButton("取消", null);
				builder.setCancelable(false);
				android.app.AlertDialog dialog = builder.create();
				return dialog;
			}
		}

	}

	// 点击下一步监视器 尼见 2015-04-09
	private class MyClickNextStepListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			for (int i = 0; i < listGenerateTakeTime.size(); i++) {
				if (listGenerateTakeTime.get(i).getTakeTimeList().size() == 1) {
					// 有药品用药时间尚未设置提示框
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							AlertDialog dialog = getAlertDialogWithHaveDrugTimeNotSet();
							dialog.show();

						}
					});

					return;
				}
			}

			// 确认保存用药信息 尼见 2015-04-09
			getActivity().runOnUiThread(new Runnable() {
				public void run() {

					AlertDialog dialog = getAlertDialogWithConfirmSaveTakeDrugInfo();
					dialog.show();

				}
			});

		}

		// 有药品尚未设置服药时间提示框 尼见 2015-04-09
		AlertDialog getAlertDialogWithHaveDrugTimeNotSet() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("有药品尚未设置服药时间，请设置后继续");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 确认保存用药信息提示框 尼见 2015-04-09
		AlertDialog getAlertDialogWithConfirmSaveTakeDrugInfo() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("确认保存用药信息");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							progressDialog = ProgressDialog.show(getActivity(),
									null, "数据加载中，请稍后...");
							progressDialog.setCancelable(true);
							new SaveTakeDrugTime().execute();

						}
					});
			builder.setNegativeButton("取消", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}
	}

	// 在工作线程中调用保存用药时间点接口 尼见 2015-04-09
	class SaveTakeDrugTime extends AsyncTask<Object, Object, Object> {

		ArrayList<GenerateTakeDrugTimeList> list1 = new ArrayList<>();
		String drugName = "", statusCodeFromServer;

		@Override
		protected Object doInBackground(Object... params) {

			// 调用生成用药时间点接口 尼见 2015-03-27
			// 声明网址字符串
			App app = (App) getActivity().getApplication();
			String uriAPI2 = app.getInterfaceUrl()
					+ "guarder/api/drug/SaveGuide";
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

			String takeTimeListArray[] = new String[10];
			// addDrugNum代表用药数据的个数
			int addDrugNum = listGenerateTakeTime.size();
			for (int i = 0; i < addDrugNum; i++) {
				int timeListSizeAll = listGenerateTakeTime.get(i)
						.getTakeTimeList().size();
				takeTimeListArray[i] = "[{";
				for (int j = 0; j < timeListSizeAll; j++) {
					if (j == timeListSizeAll - 1) {
						takeTimeListArray[i] = takeTimeListArray[i]
								.concat("\"date\":\""
										+ listGenerateTakeTime.get(0)
												.getTakeTimeList().get(i)
												.getDate()
										+ "\",\"time\":\""
										+ listGenerateTakeTime.get(0)
												.getTakeTimeList().get(i)
												.getTime() + "\"}]");
						break;
					}
					takeTimeListArray[i] = takeTimeListArray[i]
							.concat("\"date\":\""
									+ listGenerateTakeTime.get(0)
											.getTakeTimeList().get(i).getDate()
									+ "\",\"time\":\""
									+ listGenerateTakeTime.get(0)
											.getTakeTimeList().get(i).getTime()
									+ "\",");
				}
			}
			switch (addDrugNum) {
			case 1:
				// json格式中在一级路径下中括号用"|"分隔，在二级路径下用","分隔。
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(0)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(0).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(0)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(0).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(0).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(0).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(0)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(0).getRemark()
								+ "\",\"takeTimeList\":" + takeTimeListArray[0]
								+ "}]"));
				break;
			case 2:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(0)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(0).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(0)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(0).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(0).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(0).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(0)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(0).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[0]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(1)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(1).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(1).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(1).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(1)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(1).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(1).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(1).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(1)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(1).getRemark()
								+ "\",\"takeTimeList\":" + takeTimeListArray[1]
								+ "}]"));
				break;
			case 3:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(0)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(0).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(0)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(0).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(0).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(0).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(0)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(0).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[0]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(1)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(1).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(1).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(1).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(1)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(1).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(1).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(1).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(1)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(1).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[1]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(2)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(2).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(2).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(2).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(2)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(2).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(2).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(2).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(2)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(2).getRemark()
								+ "\",\"takeTimeList\":" + takeTimeListArray[2]
								+ "}]"));
				break;
			case 4:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(0)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(0).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(0)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(0).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(0).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(0).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(0)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(0).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[0]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(1)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(1).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(1).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(1).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(1)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(1).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(1).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(1).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(1)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(1).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[1]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(2)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(2).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(2).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(2).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(2)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(2).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(2).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(2).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(2)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(2).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[2]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(3).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(3)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(3).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(3).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(3).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(3)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(3).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(3).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(3).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(3)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(3).getRemark()
								+ "\",\"takeTimeList\":" + takeTimeListArray[3]
								+ "}]"));
				break;
			case 5:
				params2.add(new BasicNameValuePair("drugList",
						"[{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(0)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(0).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(0)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(0).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(0).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(0).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(0)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(0).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[0]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(1).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(1)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(1).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(1).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(1).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(1)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(1).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(1).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(1).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(1)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(1).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[1]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(2).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(2)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(2).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(2).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(2).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(2)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(2).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(2).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(2).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(2)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(2).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[2]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(3).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(3)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(3).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(3).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(3).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(3)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(3).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(3).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(3).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(3)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(3).getRemark()
								+ "\",\"takeTimeList\":"
								+ takeTimeListArray[3]
								+ "}|{\"basicDrugID\":\""
								+ listGenerateTakeTime.get(4).getBasicDrugID()
								+ "\",\"productDrugID\":\""
								+ listGenerateTakeTime.get(4)
										.getProductDrugID()
								+ "\",\"guideDrugID\":\""
								+ listGenerateTakeTime.get(4).getGuideDrugID()
								+ "\",\"drugName\":\""
								+ listGenerateTakeTime.get(4).getDrugName()
								+ "\",\"freq\":\""
								+ listGenerateTakeTime.get(4).getCycleCount()
								+ "\",\"cycle\":\""
								+ listGenerateTakeTime.get(4)
										.getIntervalValue()
								+ "\",\"begin\":\""
								+ listGenerateTakeTime.get(4).getBegin()
								+ "\",\"end\":\""
								+ listGenerateTakeTime.get(4).getEnd()
								+ "\",\"dose\":\""
								+ listGenerateTakeTime.get(4).getDrugCount()
								+ "\",\"doseUnit\":\""
								+ listGenerateTakeTime.get(4)
										.getDrugCountUnit()
								+ "\",\"remark\":\""
								+ listGenerateTakeTime.get(4).getRemark()
								+ "\",\"takeTimeList\":" + takeTimeListArray[4]
								+ "}]"));
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
					JSONObject jsonObjectData = jsonObject
							.getJSONObject("data");
					String latestSynch = jsonObjectData
							.getString("latestSynch");
					databaseName = "LocalDrugMessage";
					databaseHelper = new CreateSQLiteDatabase(getActivity(),
							databaseName, null, 1);
					db = databaseHelper.getWritableDatabase();
					if (addDrugNum == 1) {
						JSONObject jsonObject1 = jsonObjectData
								.getJSONObject("drugLists");
						String guideDrugID = jsonObject1
								.getString("guideDrugID");
						String note = jsonObject1.getString("note");
						String sql3 = "insert into DrugInfoTable (guideDrugID,userID,memberID,drugID,drugName,beginDate,endDate,note,updateTime,updateState)values('"
								+ listGenerateTakeTime.get(0).getGuideDrugID()
								+ "','"
								+ sys_userID
								+ "','"
								+ current_member_id
								+ "','"
								+ listGenerateTakeTime.get(0).getBasicDrugID()
								+ "','"
								+ listGenerateTakeTime.get(0).getDrugName()
								+ "','"
								+ listGenerateTakeTime.get(0).getBegin()
								+ "','"
								+ listGenerateTakeTime.get(0).getEnd()
								+ "','"
								+ note
								+ "','"
								+ latestSynch
								+ "','"
								+ "0" + "')";
						db.execSQL(sql3);
						for (int j = 0; j < listGenerateTakeTime.get(0)
								.getTakeTimeList().size(); j++) {
							String sql5 = "insert into TakeTimeTable (drugID,guideDrugID,memberID,drugName,date,time,ConfirmStates,ConfirmTime,cycle,freq,dose,doseUnit,condition,updateTime,updateState)values('"
									+ listGenerateTakeTime.get(0)
											.getBasicDrugID()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getGuideDrugID()
									+ "','"
									+ current_member_id
									+ "','"
									+ listGenerateTakeTime.get(0).getDrugName()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getTakeTimeList().get(j).getDate()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getTakeTimeList().get(j).getTime()
									+ "','"
									+ "0"
									+ "','"
									+ ""
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getIntervalValue()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getCycleCount()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getDrugCount()
									+ "','"
									+ listGenerateTakeTime.get(0)
											.getDrugCountUnit()
									+ "','"
									+ listGenerateTakeTime.get(0).getRemark()
									+ "','" + latestSynch + "','" + "0" + "')";
							db.execSQL(sql5);
						}
					} else {
						JSONArray drugLists = jsonObjectData
								.getJSONArray("drugLists");

						// 注意无论是takeDrugInfo表还是takeTimeTalbe插入一条同样的记录会报错。
						for (int i = 0; i < drugLists.length(); i++) {
							JSONObject jsonObject3 = drugLists.getJSONObject(i);
							String guideDrugID = jsonObject3
									.getString("guideDrugID");
							String note = jsonObject3.getString("note");
							String sql4 = "select * from DrugInfoTable where guideDrugID = '"
									+ listGenerateTakeTime.get(i)
											.getGuideDrugID() + "'";
							Cursor cursor2 = db.rawQuery(sql4, null);
							if (cursor2.getCount() == 0) {
								String sql3 = "insert into DrugInfoTable (guideDrugID,userID,memberID,drugID,drugName,beginDate,endDate,note,updateTime,updateState)values('"
										+ listGenerateTakeTime.get(i)
												.getGuideDrugID()
										+ "','"
										+ sys_userID
										+ "','"
										+ current_member_id
										+ "','"
										+ listGenerateTakeTime.get(i)
												.getBasicDrugID()
										+ "','"
										+ listGenerateTakeTime.get(i)
												.getDrugName()
										+ "','"
										+ listGenerateTakeTime.get(i)
												.getBegin()
										+ "','"
										+ listGenerateTakeTime.get(i).getEnd()
										+ "','"
										+ note
										+ "','"
										+ latestSynch
										+ "','" + "0" + "')";
								db.execSQL(sql3);
							}
						}
						for (int i = 0; i < drugLists.length(); i++) {
							for (int j = 0; j < listGenerateTakeTime.get(i)
									.getTakeTimeList().size(); j++) {

								String sql6 = "select * from TakeTimeTable where guideDrugID = '"
										+ listGenerateTakeTime.get(i)
												.getGuideDrugID()
										+ "' and date = '"
										+ listGenerateTakeTime.get(i)
												.getTakeTimeList().get(j)
												.getDate()
										+ "' and time = '"
										+ listGenerateTakeTime.get(i)
												.getTakeTimeList().get(j)
												.getTime() + "'";
								Cursor cursor2 = db.rawQuery(sql6, null);
								if (cursor2.getCount() == 0) {
									String sql5 = "insert into TakeTimeTable (drugID,guideDrugID,memberID,drugName,date,time,ConfirmStates,ConfirmTime,cycle,freq,dose,doseUnit,condition,updateTime,updateState)values('"
											+ listGenerateTakeTime.get(i)
													.getBasicDrugID()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getGuideDrugID()
											+ "','"
											+ current_member_id
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getDrugName()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getTakeTimeList().get(j)
													.getDate()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getTakeTimeList().get(j)
													.getTime()
											+ "','"
											+ "0"
											+ "','"
											+ ""
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getIntervalValue()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getCycleCount()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getDrugCount()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getDrugCountUnit()
											+ "','"
											+ listGenerateTakeTime.get(i)
													.getRemark()
											+ "','"
											+ latestSynch + "','" + "0" + "')";
									db.execSQL(sql5);
								}
							}
						}
						db.close();
					}
					// 成功接受到服务器返回数据之后，结束进度等待窗口
					progressDialog.dismiss();
					FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					DrugRemindFragment drugRemindFragment = new DrugRemindFragment();
					fragmentTransaction.setCustomAnimations(
							R.anim.enter_from_left, R.anim.exit_to_right);
					fragmentTransaction.replace(R.id.login_fragment,
							drugRemindFragment, "drugRemindFragment");
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}
			} catch (Exception e) {
				System.out.println(e);
			}

			return null;
		}

		// 结束日期早于开始日期提示框 尼见 2015-03-23
		AlertDialog getAlertDialogWithTakeTimeIntervalIsUnreasonable() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage(drugName + " 服药时间间隔不合理");
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