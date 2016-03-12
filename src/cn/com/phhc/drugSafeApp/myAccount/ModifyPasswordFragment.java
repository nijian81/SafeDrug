package cn.com.phhc.drugSafeApp.myAccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.App;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModifyPasswordFragment extends Fragment {

	ProgressDialog progressDialog;
	SQLiteDatabase db;
	MyAccountFragment myAccountFragment;
	CreateSQLiteDatabase databaseHelper;
	String newPassword, oldPassword, databaseName, userID;
	EditText old_password, new_password;
	ImageButton back_modify_password;
	TextView complete;

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

		View rootView = inflater.inflate(R.layout.modify_password_fragment,
				container, false);
		back_modify_password = (ImageButton) rootView
				.findViewById(R.id.back_modify_password);
		back_modify_password
				.setOnClickListener(new MyClickBackMyAccountListener());
		complete = (TextView) rootView.findViewById(R.id.complete);
		complete.setOnClickListener(new MyClickCompleteListener());
		old_password = (EditText) rootView.findViewById(R.id.old_password);
		new_password = (EditText) rootView.findViewById(R.id.new_password);

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

	// 跳转至我的账户fragment 尼见 2015-02-28
	class MyClickBackMyAccountListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			myAccountFragment = new MyAccountFragment();
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putInt("flag", 1);
			myAccountFragment.setArguments(bundle);
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,
					R.anim.exit_to_right);
			fragmentTransaction.replace(R.id.login_fragment, myAccountFragment,
					"myAccountFragment");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	// 点击完成按钮监视器 尼见 2015-02-28
	class MyClickCompleteListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			progressDialog = ProgressDialog.show(getActivity(), null,
					"数据加载中，请稍后...");
			new complete().execute();
		}
	}

	// 在工作线程中去修改密码 尼见 2015-04-24
	class complete extends AsyncTask<Object, Object, Object> {

		@Override
		protected Objects doInBackground(Object... params) {

			// 隐藏输入法
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					new_password.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					old_password.getWindowToken(), 0);
			oldPassword = old_password.getText().toString();
			newPassword = new_password.getText().toString();
			try {
				db = databaseHelper.getWritableDatabase();
				String sql = "select * from anonymityRegister";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					userID = cursor.getString(3);
				}
				db.close();
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 对旧密码进行判断，不能为空 尼见 2015-02-28
			if (oldPassword.length() == 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithOldPasswordIsNull();
						dialog.show();
					}
				});

				return null;
			}

			// 对新密码进行判断，不能为空 尼见 2015-03-12
			if (newPassword.length() == 0) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithNewPasswordIsNull();
						dialog.show();
					}
				});

				return null;
			}

			// 对新密码长度进行判断
			if (newPassword.length() < 6) {

				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithNewPasswordLengthLessSix();
						dialog.show();
					}
				});

				return null;
			}

			// 声明网址字符串
			App app = (App) getActivity().getApplication();
			String uriAPI = app.getInterfaceUrl()
					+ "guarder/api/user/SavePassWord";
			// 创建http post连接，post运行变量必须用NameValuePair[]数组存储
			HttpPost httpRequest = new HttpPost(uriAPI);
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			System.out.println(userID);
			params1.add(new BasicNameValuePair("userID", userID));
			params1.add(new BasicNameValuePair("oldPwd", oldPassword));
			params1.add(new BasicNameValuePair("newPwd", newPassword));
			try {
				// 发出http request
				httpRequest.setEntity(new UrlEncodedFormEntity(params1,
						HTTP.UTF_8));
				// 去的http response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				// 若状态码为200 ok
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonObject = new JSONObject(strResult);
					//成功返回数据，加载等待框dismiss();
					progressDialog.dismiss();
					String error = jsonObject.getString("error");
					if (error.isEmpty()) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								AlertDialog dialog = getAlertDialogWithModifyPasswordSuccess();
								dialog.show();
							}
						});
						return null;
					} else {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {

								AlertDialog dialog = getAlertDialogWithModifyPasswordSuccessFalse();
								dialog.show();
							}
						});

						return null;

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	// 旧密码为空提示框 尼见 2015-02-28
	AlertDialog getAlertDialogWithOldPasswordIsNull() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("旧密码不能为空");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

	// 新密码为空提示框 尼见 2015-02-28
	AlertDialog getAlertDialogWithNewPasswordIsNull() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("新密码不能为空");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

	// 新密码长度小于6位提示框 尼见 2015-02-28
	AlertDialog getAlertDialogWithNewPasswordLengthLessSix() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("新密码不能小于6位");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

	// 修改密码成功提示框 尼见 2015-02-28
	AlertDialog getAlertDialogWithModifyPasswordSuccess() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("修改密码成功");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myAccountFragment = new MyAccountFragment();
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				Bundle bundle = new Bundle();
				bundle.putInt("flag", 1);
				myAccountFragment.setArguments(bundle);
				fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,
						R.anim.exit_to_right);
				fragmentTransaction.replace(R.id.login_fragment,
						myAccountFragment, "myAccountFragment");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();
		return dialog;

	}

	// 修改密码不成功警告框 尼见 2015-02-28
	AlertDialog getAlertDialogWithModifyPasswordSuccessFalse() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("原密码不正确，请重新输入！");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		android.app.AlertDialog dialog = builder.create();
		return dialog;
	}

}