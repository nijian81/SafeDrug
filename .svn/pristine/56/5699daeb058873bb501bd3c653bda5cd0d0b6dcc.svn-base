package cn.com.phhc.drugSafeApp.myAccount;

/**
 * @author nijian
 * 
 * 这个类是用于登录的，当登录成功之后会自动调用下行接口，然后先将本地数据库清空，在将服务器端的数据存在本地。
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.util.LogUtil;
import cn.com.phhc.drugSafeApp.util.ToastHelper;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LoginFragment extends Fragment {

	ProgressDialog progressDialog;
	CreateSQLiteDatabase databaseHelper;
	SQLiteDatabase db;
	Button login_button, forget_password_button;
	String cellphoneNumber, password, deviceType, databaseName, uuid;
	EditText input_cellphone, input_password;
	ForgetPasswordFragment forgetPasswordFragment;
	MyAccountFragment myAccountFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		databaseName = "LocalDrugMessage";
		databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName,
				null, 1);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.login_fragment, container,
				false);
		input_cellphone = (EditText) rootView
				.findViewById(R.id.input_cellphone);
		input_password = (EditText) rootView.findViewById(R.id.input_password);
		login_button = (Button) rootView.findViewById(R.id.button_login);
		login_button.setOnClickListener(new MyClickLoginButtonListener());
		forget_password_button = (Button) rootView
				.findViewById(R.id.forget_password);
		forget_password_button
				.setOnClickListener(new MyClickForgetPasswordButtonListener());
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

	// 点击忘记密码监视器 尼见 2015-04-24
	class MyClickForgetPasswordButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			forgetPasswordFragment = new ForgetPasswordFragment();
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,
					R.anim.exit_to_left);
			fragmentTransaction.replace(R.id.login_fragment,
					forgetPasswordFragment, "forgetPasswordFragment");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	// 登录按钮点击监听器实现 尼见 2014-02-28
	class MyClickLoginButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			progressDialog = ProgressDialog.show(getActivity(), null,
					"数据加载中，请稍后...");
			new Login().execute();
		}
	}

	// 在工作线程中登录 尼见 2014-02-28
	class Login extends AsyncTask<Object, Object, Object> {

		@Override
		protected Objects doInBackground(Object... params) {

			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					input_cellphone.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					input_password.getWindowToken(), 0);

			cellphoneNumber = input_cellphone.getText().toString();
			password = input_password.getText().toString();
			deviceType = "a";

			// 对手机号进行判断，第一位是‘1’，其余10位是0-9任意数字
			if (!cellphoneNumber.matches("1[0-9]{10}")) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						AlertDialog dialog = getAlertDialogWithCellphoneWrong();
						dialog.show();

					}
				});

				return null;
			}

			// 声明网址字符串
			App app = (App) getActivity().getApplication();
			String uriAPI = app.getInterfaceUrl() + "guarder/api/user/Login";
			// 查询本手机的UUID
			db = databaseHelper.getWritableDatabase();
			String sql = "select * from anonymityRegister";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				uuid = cursor.getString(6);
			}
			// 创建http post连接
			// post运行变量必须用NameValuePair[]数组存储
			HttpPost httpRequest = new HttpPost(uriAPI);
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("name", cellphoneNumber));
			params1.add(new BasicNameValuePair("pwd", password));
			params1.add(new BasicNameValuePair("dtype", deviceType));
			params1.add(new BasicNameValuePair("device", uuid));
			try {
				// 发出http request
				httpRequest.setEntity(new UrlEncodedFormEntity(params1,
						HTTP.UTF_8));
				// 取得http response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				// 若状态码为200 ok
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonObject = new JSONObject(strResult);
					String error = jsonObject.getString("error");
					if (!error.isEmpty()) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								AlertDialog dialog = getAlertDialogWithUsernameOrPasswordWrong();
								dialog.show();

							}
						});

						return null;
					} else {

						JSONObject jsonObjectData = jsonObject
								.getJSONObject("data");
						// 登录成功从服务器返回的当前用户ID
						String sysUser_ID = jsonObjectData
								.getString("sysUser_ID");
						try {
							// 对数据库中登录标志字段更新，1表示已登录，0表示未登录
							String sql2 = "update anonymityRegister set isLogin = '1'";
							db.execSQL(sql2);
							String sql3 = "delete from MemberInfoTable";
							//TODO 把服药时间表和药品信息表数据清空
							db.execSQL(sql3);
							String sql4 = "update anonymityRegister set sys_userID = ''";
							db.execSQL(sql4);

							// 声明网址字符串
							App app1 = (App) getActivity().getApplication();
							String uriAPI2 = app1.getInterfaceUrl()
									+ "guarder/api/user/SynchGet";
							HttpPost httpRequest2 = new HttpPost(uriAPI2);
							List<NameValuePair> params2 = new ArrayList<NameValuePair>();
							// 获取当前时间
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date curDate = new Date(System.currentTimeMillis());
							String localTimeStamp = formatter.format(curDate);
							params2.add(new BasicNameValuePair("userID",
									sysUser_ID));
							params2.add(new BasicNameValuePair("latestSynch",
									"2015-04-10 12:12:12"));
							try {
								// 发出http request
								httpRequest2
										.setEntity(new UrlEncodedFormEntity(
												params2, HTTP.UTF_8));
								// 取得http response
								HttpResponse httpResponse2 = new DefaultHttpClient()
										.execute(httpRequest2);
								// 若状态码为200 ok
								if (httpResponse2.getStatusLine()
										.getStatusCode() == 200) {
									String strResult2 = EntityUtils
											.toString(httpResponse2.getEntity());
									JSONObject jsonObject2 = new JSONObject(
											strResult2);
									JSONObject jsonObjectData2 = jsonObject2
											.getJSONObject("data");
									if (jsonObjectData2.get("memberList") instanceof JSONObject) {

										JSONObject jsonObject3 = jsonObjectData2
												.getJSONObject("memberList");
										String memberID = jsonObject3
												.getString("memberID");
										String isFamily = jsonObject3
												.getString("isFamily");
										String nick = jsonObject3
												.getString("nick");
										String tel = jsonObject3
												.getString("tel");
										String sex = jsonObject3
												.getString("sex");
										String birth = jsonObject3
												.getString("birth");
										String photo = jsonObject3
												.getString("photo");
										JSONObject jsonObjectRestTime = jsonObject3
												.getJSONObject("restTime");
										String getup = jsonObjectRestTime
												.getString("getup");
										String breakfast = jsonObjectRestTime
												.getString("breakfast");
										String lunch = jsonObjectRestTime
												.getString("lunch");
										String supper = jsonObjectRestTime
												.getString("supper");
										String sleep = jsonObjectRestTime
												.getString("sleep");
										String sql5 = "update anonymityRegister set sys_userID = '"
												+ memberID + "'";
										db.execSQL(sql5);
										String sql6 = "insert into MemberInfoTable (memberID, name, isFamily, photo, tel, sex, birth, getup, breakfast, lunch, dinner, sleep, updateTime, updateState) values('"
												+ memberID
												+ "','"
												+ nick
												+ "','"
												+ isFamily
												+ "','"
												+ photo
												+ "','"
												+ tel
												+ "','"
												+ sex
												+ "','"
												+ birth
												+ "','"
												+ getup
												+ "','"
												+ breakfast
												+ "','"
												+ lunch
												+ "','"
												+ supper
												+ "','"
												+ sleep
												+ "','"
												+ ""
												+ "','"
												+ "0"
												+ "')";
										db.execSQL(sql6);
									} else {

										JSONArray jsonArray = jsonObjectData2
												.getJSONArray("memberList");
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											String memberID = jsonObject3
													.getString("memberID");
											String isFamily = jsonObject3
													.getString("isFamily");
											String nick = jsonObject3
													.getString("nick");
											String tel = jsonObject3
													.getString("tel");
											String sex = jsonObject3
													.getString("sex");
											String birth = jsonObject3
													.getString("birth");
											String photo = jsonObject3
													.getString("photo");
											JSONObject jsonObjectRestTime = jsonObject3
													.getJSONObject("restTime");
											String getup = jsonObjectRestTime
													.getString("getup");
											String breakfast = jsonObjectRestTime
													.getString("breakfast");
											String lunch = jsonObjectRestTime
													.getString("lunch");
											String supper = jsonObjectRestTime
													.getString("supper");
											String sleep = jsonObjectRestTime
													.getString("sleep");
											// 第一条记录为本人记录，第一次的时候更新sys_userID
											if (i == 0) {
												String sql5 = "update anonymityRegister set sys_userID = '"
														+ memberID + "'";
												db.execSQL(sql5);
												String sql6 = "insert into MemberInfoTable (memberID, name, isFamily, photo, tel, sex, birth, getup, breakfast, lunch, dinner, sleep, updateTime, updateState) values('"
														+ memberID
														+ "','"
														+ nick
														+ "','"
														+ isFamily
														+ "','"
														+ photo
														+ "','"
														+ tel
														+ "','"
														+ sex
														+ "','"
														+ birth
														+ "','"
														+ getup
														+ "','"
														+ breakfast
														+ "','"
														+ lunch
														+ "','"
														+ supper
														+ "','"
														+ sleep
														+ "','"
														+ ""
														+ "','" + "0" + "')";
												db.execSQL(sql6);
											} else {
												// 其余记录为成员记录,只需要更新成员信息表
												String sql6 = "insert into MemberInfoTable (memberID, name, isFamily, photo, tel, sex, birth, getup, breakfast, lunch, dinner, sleep, updateTime, updateState) values('"
														+ memberID
														+ "','"
														+ nick
														+ "','"
														+ isFamily
														+ "','"
														+ photo
														+ "','"
														+ tel
														+ "','"
														+ sex
														+ "','"
														+ birth
														+ "','"
														+ getup
														+ "','"
														+ breakfast
														+ "','"
														+ lunch
														+ "','"
														+ supper
														+ "','"
														+ sleep
														+ "','"
														+ ""
														+ "','" + "0" + "')";
												db.execSQL(sql6);
											}
										}
									}

									db.close();
								}
							} catch (Exception e) {
								System.out.println(e);
							}
							progressDialog.dismiss();
							db.close();
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println(e);
						}

						// 从fragment中实现fragment的替换注意两点：
						// 1.getActivity().getSupportFragmentManager()为的是拿到fragment所在activity的管理对象
						// 2.myAccountFragment = new
						// MyAccountFragment()为了的是得到fragment的示例，上述两点缺一不可，否则都会报空指针异常。
						myAccountFragment = new MyAccountFragment();
						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						Bundle bundle = new Bundle();
						bundle.putInt("flag", 1);
						myAccountFragment.setArguments(bundle);
						fragmentTransaction.setCustomAnimations(
								R.anim.enter_from_left, R.anim.exit_to_right);
						fragmentTransaction.replace(R.id.login_fragment,
								myAccountFragment, "myAccountFragment");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();

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
	}

	// 手机号不正确警告框 尼见 2015-02-28
	AlertDialog getAlertDialogWithCellphoneWrong() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("您输入的手机号不正确");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		return dialog;
	}

	// 用户名或者密码不正确警告框 尼见 2015-02-28
	AlertDialog getAlertDialogWithUsernameOrPasswordWrong() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("提示");
		builder.setMessage("用户名或密码错误，请确认");
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		return dialog;
	}

}