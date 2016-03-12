package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 使用协议
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

public class UsingProtocolFragment extends Fragment {

    AboutUsFragment aboutUsFragment;
    ImageButton back_using_protocol;
    WebView usingProtocolWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.using_protocol_fragment,
                container, false);
        back_using_protocol = (ImageButton)rootView.findViewById(R.id.back_using_protocol);
        back_using_protocol.setOnClickListener(new MyClickBackAboutUsListener());
        usingProtocolWebView = (WebView)rootView.findViewById(R.id.usingProtocolWebView);
        usingProtocolWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = usingProtocolWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        App app = (App)getActivity().getApplication();
        String url = app.getInterfaceUrl()+"guarder/intor?versionCode=1.0&typeInteger=4";
        usingProtocolWebView.loadUrl(url);

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