package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 意见反馈
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.App;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;

public class OpinionFeedbackFragment extends Fragment {

    int isLogin;
    CreateSQLiteDatabase databaseHelper;
    SQLiteDatabase db;
    String databaseName;
    MyAccountFragment myAccountFragment;
    ImageButton back_opinion_feedback;
    WebView opinionFeedbackWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.opinion_feedback_fragment,
                container, false);
        databaseName = "LocalDrugMessage";
        databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName, null, 1);
        db = databaseHelper.getWritableDatabase();
        String sql = "select * from anonymityRegister";
        Cursor cursor = db.rawQuery(sql, null);
        String sysUserID = null;
        while (cursor.moveToNext()) {
            sysUserID = cursor.getString(3);
        }
        db.close();
        back_opinion_feedback = (ImageButton) rootView.findViewById(R.id.back_opinion_feedback);
        back_opinion_feedback.setOnClickListener(new MyClickBackAboutUsListener());
        opinionFeedbackWebView = (WebView) rootView.findViewById(R.id.opinionFeedbackWebView);
        opinionFeedbackWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = opinionFeedbackWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        App app = (App)getActivity().getApplication();
        String url = app.getInterfaceUrl()+"guarder/feedBack?userID="+sysUserID;
        opinionFeedbackWebView.loadUrl(url);
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

    //跳转至关于我们fragment 尼见 2015-02-28
    class MyClickBackAboutUsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                db = databaseHelper.getWritableDatabase();
                String sql = "select * from anonymityRegister";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    isLogin = cursor.getInt(7);
                }
                //如果标志flag为0则让其匿名注册，如果flag为1则拒绝匿名注册。
                if (isLogin == 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    myAccountFragment = new MyAccountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 1);
                    myAccountFragment.setArguments(bundle);
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.login_fragment, myAccountFragment, "myAccountFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    myAccountFragment = new MyAccountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 0);
                    myAccountFragment.setArguments(bundle);
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.login_fragment, myAccountFragment, "myAccountFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                db.close();
            } catch (Exception e)

            {
                // TODO: handle exception
            }
        }
    }
}