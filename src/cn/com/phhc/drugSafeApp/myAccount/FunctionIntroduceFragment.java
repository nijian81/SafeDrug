package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 功能介绍
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

public class FunctionIntroduceFragment extends Fragment {

    AboutUsFragment aboutUsFragment;
    ImageButton back_function_introduce;
    WebView functionIntroduceWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.function_introduce_fragment,
                container, false);
        back_function_introduce = (ImageButton)rootView.findViewById(R.id.back_function_introduce);
        back_function_introduce.setOnClickListener(new MyClickBackAboutUsListener());
        functionIntroduceWebView = (WebView)rootView.findViewById(R.id.functionIntroduceWebView);
        functionIntroduceWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = functionIntroduceWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        App app = (App)getActivity().getApplication();
        String url = app.getInterfaceUrl()+"guarder/proIntro";
        functionIntroduceWebView.loadUrl(url);

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