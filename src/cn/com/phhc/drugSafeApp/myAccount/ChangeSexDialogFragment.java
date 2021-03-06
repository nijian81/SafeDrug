package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 改变性别对话框
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

/**
 * Created by lenovo on 2015/2/27.
 */
public class ChangeSexDialogFragment extends DialogFragment {

    ConfirmInterface listener;
    RelativeLayout man_relative, woman_relative;
    String sex, sex_flag;
    TextView man, woman, sex_text_view;
    ImageView mark_man, mark_woman;

    //定制fragmentDialog弹出动作
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public static ChangeSexDialogFragment newInstance(String sex) {
        ChangeSexDialogFragment frag = new ChangeSexDialogFragment();
        Bundle args = new Bundle();
        args.putString("sex", sex);
        frag.setArguments(args);
        return frag;
    }

    public interface ConfirmInterface {
        public void onConfirmInterface(String sex);
    }

    public void setConfirmInterface(ConfirmInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.change_sex_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        man_relative = (RelativeLayout) rootView.findViewById(R.id.man_relative);
        man_relative.setOnClickListener(new MyClickManListener());
        woman_relative = (RelativeLayout) rootView.findViewById(R.id.woman_relative);
        woman_relative.setOnClickListener(new MyClickWomanListener());
        man = (TextView) rootView.findViewById(R.id.man);
        woman = (TextView) rootView.findViewById(R.id.woman);
        sex_text_view = (TextView) rootView.findViewById(R.id.sex_text_view);
        mark_man = (ImageView) rootView.findViewById(R.id.mark_man);
        mark_woman = (ImageView) rootView.findViewById(R.id.mark_woman);
        sex_flag = getArguments().getString("sex", "");
        if(sex_flag.equals("男")){
            mark_man.setVisibility(View.VISIBLE);
            mark_woman.setVisibility(View.GONE);
        }else{
            mark_woman.setVisibility(View.VISIBLE);
            mark_man.setVisibility(View.GONE);
        }

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

    //点击男监视器 尼见 2015-03-03
    class MyClickManListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mark_man.setVisibility(View.VISIBLE);
            mark_woman.setVisibility(View.GONE);
            sex = "man";
            listener.onConfirmInterface(sex);
            dismiss();
        }
    }

    //点击女监视器 尼见 2015-03-03
    class MyClickWomanListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mark_man.setVisibility(View.GONE);
            mark_woman.setVisibility(View.VISIBLE);
            sex = "woman";
            listener.onConfirmInterface(sex);
            dismiss();
        }
    }

}
