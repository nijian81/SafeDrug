package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 公司介绍
 */

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

public class CompanyIntroduceFragment extends Fragment {

    AboutUsFragment aboutUsFragment;
    ImageButton back_company_introduce;
    WebView companyIntroduceWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.company_introduce_fragment,
                container, false);
        back_company_introduce = (ImageButton)rootView.findViewById(R.id.back_company_introduce);
        back_company_introduce.setOnClickListener(new MyClickBackAboutUsListener());
        companyIntroduceWebView = (WebView)rootView.findViewById(R.id.companyIntroduceWebView);
        companyIntroduceWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = companyIntroduceWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        App app = (App)getActivity().getApplication();
        String url = app.getInterfaceUrl()+"guarder/intor?versionCode=1.0&typeInteger=3";
        companyIntroduceWebView.loadUrl(url);

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
    class MyClickBackAboutUsListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            aboutUsFragment = new AboutUsFragment();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.login_fragment, aboutUsFragment, "aboutUsFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}