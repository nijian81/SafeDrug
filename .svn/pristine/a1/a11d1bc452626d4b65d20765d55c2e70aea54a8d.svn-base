package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 家庭成员管理对应的页面
 */

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.MemberItem;
import cn.com.phhc.drugSafeApp.utils.MemberItemAdapter;
import cn.com.phhc.drugSafeApp.utils.PersonalInformation;

import java.util.ArrayList;

public class FamilyMemberManageFragment extends Fragment {

	AddMemberInformationFragment addMemberInformationFragment;
	ArrayList<PersonalInformation> listToCompleteInformations;
	ModifyMemberInformationFragment modifyMemberInformationFragment;
	MemberItemAdapter memberItemAdapter;
	ArrayList<MemberItem> list;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	String databaseName, portrait, name;
	ImageButton add_familyMember;
	CompletePersonalInformationFragment completePersonalInformationFragment;
	ListView familyMemberListView;
	int familyMemberNum;

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
				R.layout.family_member_manage_fragment, container, false);

		// 添加
		add_familyMember = (ImageButton) rootView
				.findViewById(R.id.add_familyMember);
		add_familyMember.setOnClickListener(new MyClickAddMemberListener());
		familyMemberListView = (ListView) rootView
				.findViewById(R.id.familyMember);
		db = databaseHelper.getWritableDatabase();
		String sysUserID = null;
		String sql1 = "select * from anonymityRegister";
		Cursor cursor1 = db.rawQuery(sql1, null);
		while (cursor1.moveToNext()) {
			sysUserID = cursor1.getString(3);
		}
		String sql = "select photo, name from MemberInfoTable where isFamily != '"
				+ "1" + "'";
		Cursor cursor = db.rawQuery(sql, null);
		list = new ArrayList<MemberItem>();
		while (cursor.moveToNext()) {
			familyMemberListView.setVisibility(View.VISIBLE);
			portrait = cursor.getString(0);
			name = cursor.getString(1);
			list.add(new MemberItem(portrait, name));
		}
		familyMemberNum = list.size();
		memberItemAdapter = new MemberItemAdapter();
		memberItemAdapter.setContext(getActivity());
		memberItemAdapter.setArrayList(list);
		familyMemberListView.setAdapter(memberItemAdapter);
		db.close();
		// 设置点击监视器
		familyMemberListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> listView, View view,
							int position, long id) {
						listToCompleteInformations = new ArrayList<>();
						MemberItem memberItem = (MemberItem) listView
								.getItemAtPosition(position);
						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						modifyMemberInformationFragment = new ModifyMemberInformationFragment();
						Bundle bundle = new Bundle();
						bundle.putString("name", memberItem.getName());
						modifyMemberInformationFragment.setArguments(bundle);
						fragmentTransaction.setCustomAnimations(
								R.anim.enter_from_right, R.anim.exit_to_left);
						fragmentTransaction.replace(R.id.login_fragment,
								modifyMemberInformationFragment,
								"modifyMemberInformationFragment");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}

				});

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

	// 点击添加成员监视器 尼见 2015-03-05
	class MyClickAddMemberListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (familyMemberNum == 4) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog dialog = getAlertDialogWithNumUpLimits();
						dialog.show();
					}
				});
				return;
			}
			listToCompleteInformations = new ArrayList<>();
			addMemberInformationFragment = new AddMemberInformationFragment();
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			Bundle bundle = new Bundle();
			// 设置标志位，isMyself为0表示添加成员信息，isMyself为1表示修改本人信息。
			bundle.putInt("isMyself", 0);
//			bundle.putCharSequenceArrayList("PersonalInformation",
//					(ArrayList) listToCompleteInformations);
			addMemberInformationFragment.setArguments(bundle);
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,
					R.anim.exit_to_left);
			fragmentTransaction.replace(R.id.login_fragment,
					addMemberInformationFragment,
					"addMemberInformationFragment");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	// 家庭成员上限提示框 尼见 2015-03-11
	AlertDialog getAlertDialogWithNumUpLimits() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("家庭成员已达到上限，不能添加!");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		return dialog;
	}

}