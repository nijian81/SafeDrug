package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 注册fragment
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.App;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.TimeButton;

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

public class RegisterFragment extends Fragment {

	MyAccountFragment myAccountFragment;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	Intent intent;
	static String authCodeFromServer;
	String cellphoneNumber, password, authCodeFromUser, databaseName, userID,
			uuid, type, deviceType;
	EditText input_cellphone, input_password, input_authCode;
	TimeButton button_get_auth_code;
	Button button_complete;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		intent = new Intent();
		intent.setAction("com.phhc.service.AnonymityRegisterService");
		getActivity().startService(intent);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.register_fragment, container,
				false);
		button_get_auth_code = (TimeButton) rootView
				.findViewById(R.id.button_get_check_code);
		button_get_auth_code
				.setOnClickListener(new MyClickGetAuthCodeButtonListener());
		button_get_auth_code.setTextAfter("秒后重新获取").setTextBefore("点击获取验证码")
				.setLength(5 * 1000);
		button_complete = (Button) rootView.findViewById(R.id.button_complete);
		button_complete.setOnClickListener(new MyClickCompleteButtonListener());
		input_cellphone = (EditText) rootView
				.findViewById(R.id.input_cellphone);
		input_password = (EditText) rootView.findViewById(R.id.input_password);
		input_authCode = (EditText) rootView.findViewById(R.id.input_auth_code);
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

	// 点击获取验证码按钮监视器 尼见 2015-02-28
	class MyClickGetAuthCodeButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {

			cellphoneNumber = input_cellphone.getText().toString();
			type = "1";
			new GetAuthCode(cellphoneNumber, type).execute();

		}

	}

	// 注册完成按钮监视器 尼见 2015-02-28
	class MyClickCompleteButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			new Register().execute();
		}
	}

	// 在工作线程中注册 尼见 2015-02-28
	class Register extends AsyncTask<Object, Object, Object> {

		@Override
		protected Objects doInBackground(Object... params) {

			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					input_cellphone.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					input_password.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					input_authCode.getWindowToken(), 0);
			cellphoneNumber = input_cellphone.getText().toString();
			password = input_password.getText().toString();
			authCodeFromUser = input_authCode.getText().toString();
			try {
				db = databaseHelper.getWritableDatabase();
				String sql = "select * from anonymityRegister";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					userID = cursor.getString(3);
					uuid = cursor.getString(6);
				}
				db.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			deviceType = "a";

			// 声明网址字符串，如果报内部服务器异常，可能是调用的网址不对。
			App app = (App) getActivity().getApplication();
			String uriAPI = app.getInterfaceUrl() + "guarder/api/user/Register";
			// 创建http post连接
			// post运行变量必须用NameValuePair[]数组存储
			HttpPost httpRequest = new HttpPost(uriAPI);
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("name", cellphoneNumber));
			params1.add(new BasicNameValuePair("pwd", password));
			params1.add(new BasicNameValuePair("userID", userID));
			params1.add(new BasicNameValuePair("code", authCodeFromUser));
			params1.add(new BasicNameValuePair("dtype", deviceType));
			params1.add(new BasicNameValuePair("device", uuid));

			// 对手机号进行判断，第一位是‘1’，其余10位是0-9任意数字 尼见 2015-02-28
			if (!cellphoneNumber.matches("1[0-9]{10}")) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithCellphoneWrong();
						dialog.show();

					}
				});

				return null;
			}

			// 对验证码进行判断，为六位数字 尼见 2015-02-28
			if (!authCodeFromUser.matches("[0-9]{6}")) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithAuthCodeWrong();
						dialog.show();
					}
				});

				return null;

			}

			// 对密码进行验证，长度不少于六位的任意字符 尼见 2015-02-28
			if (!password.matches(".{6,}")) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithPasswordLessSix();
						dialog.show();

					}
				});

				return null;

			}

			// 注册不成功检验，原因：验证码失效、错误，或者手机号已经注册 尼见 2015-02-28
			if (!authCodeFromUser.equals(authCodeFromServer)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithRegisterFalse();
						dialog.show();
					}
				});

				return null;

			}

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
					String result = jsonObject.getString("result");
					if (result.equals("200")) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {

								AlertDialog dialog = getAlertDialogWithRegisterSuccess();
								dialog.show();
							}
						});

						return null;
					}
				} else {
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		// 注册成功 尼见 2015-02-28
		AlertDialog getAlertDialogWithRegisterSuccess() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("恭喜您");
			builder.setMessage("注册成功");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
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
							fragmentTransaction.setCustomAnimations(
									R.anim.enter_from_left,
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

		// 手机号不正确警告框 尼见 2015-02-28
		AlertDialog getAlertDialogWithCellphoneWrong() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("您输入的手机号不正确");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;

		}

		// 验证码错误警告框 尼见 2015-02-28
		AlertDialog getAlertDialogWithAuthCodeWrong() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("请输入6位验证码");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 密码长度少于六位警告框 尼见 2015-02-28
		AlertDialog getAlertDialogWithPasswordLessSix() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("密码不能少于6位");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 注册不成功警告框 尼见 2015-02-28
		AlertDialog getAlertDialogWithRegisterFalse() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("该手机号已经注册或者验证码错误或已经失效，如尚未注册，请重新获取验证码！");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

	}

	// 注册时获取验证码 尼见 2015-02-28
	class GetAuthCode extends AsyncTask<Object, Object, Object> {

		String cellphoneNumber, type;

		public GetAuthCode(String cellphoneNumber, String type) {
			this.cellphoneNumber = cellphoneNumber;
			this.type = type;
		}

		@Override
		protected Objects doInBackground(Object... params) {

			// 对手机号进行判断，第一位是‘1’，其余10位是0-9任意数字
			if (!cellphoneNumber.matches("1[0-9]{10}")) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog dialog = getAlertDialogWithCellphoneWrong();
						dialog.show();

					}
				});

				return null;
			}

			// 声明网址字符串
			App app = (App) getActivity().getApplication();
			String uriAPI = app.getInterfaceUrl()
					+ "guarder/api/user/GetCheckCode";
			// 创建http post连接
			// post运行变量必须用NameValuePair[]数组存储
			HttpPost httpRequest = new HttpPost(uriAPI);
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("name", cellphoneNumber));
			params1.add(new BasicNameValuePair("type", type));
			try {
				// 发出http request
				httpRequest.setEntity(new UrlEncodedFormEntity(params1,
						HTTP.UTF_8));
				// 取得http response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				// 若服务器成功响应，则状态码为200
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonObject = new JSONObject(strResult);
					String error = jsonObject.getString("error");
					JSONObject jsonObjectData = jsonObject
							.getJSONObject("data");
					authCodeFromServer = jsonObjectData.getString("code");
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							AlertDialog dialog = getAlertDialogWithShowAuthCode();
							dialog.show();
						}
					});
					if (authCodeFromServer.equals("0")) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								AlertDialog dialog = getAlertDialogWithCellphoneNumIsRegistered();
								dialog.show();
							}
						});

						return null;
					}
				} else {
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		// 注册不成功警告框 尼见 2015-03-04
		AlertDialog getAlertDialogWithCellphoneNumIsRegistered() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("该手机号已经注册!");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 弹出服务于验证码  尼见 2015-03-04
		AlertDialog getAlertDialogWithShowAuthCode() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage(authCodeFromServer);
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;
		}

		// 手机号不正确警告框 尼见 2015-03-04
		AlertDialog getAlertDialogWithCellphoneWrong() {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity(),
					android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

			builder.setTitle("提示");
			builder.setMessage("您输入的手机号不正确");
			builder.setPositiveButton("确定", null);
			builder.setCancelable(false);
			android.app.AlertDialog dialog = builder.create();
			return dialog;

		}
	}

}
