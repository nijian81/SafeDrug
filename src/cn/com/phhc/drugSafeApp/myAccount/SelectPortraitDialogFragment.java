package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 选择头像对话框
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

/**
 * Created by lenovo on 2015/2/27.
 */
public class SelectPortraitDialogFragment extends DialogFragment {

    ImageView tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8;
    TextView confirm,cancel;
    String portrait_id;
    ConfirmInterface listener;

    //定制fragmentDialog弹出动作
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public static SelectPortraitDialogFragment newInstance(int title) {
        SelectPortraitDialogFragment frag = new SelectPortraitDialogFragment();
        Bundle args = new Bundle();
        args.putString("patient", "2");
        frag.setArguments(args);
        return frag;
    }

    public interface ConfirmInterface {
        public void onConfirmInterface(String portrait);
    }

    public void setConfirmInterface(ConfirmInterface listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.select_portrait_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        //确认按钮监视器
        confirm = (TextView) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new MyClickConfirmTextViewListener());
        //取消按钮监视器
        cancel = (TextView) rootView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new MyClickCancelTextViewListener());
        //对每一个头像设置监听器
        tx1 = (ImageView) rootView.findViewById(R.id.tx1);
        tx1.setOnClickListener(new MyClickChangeSelectTx1Listener());
        tx2 = (ImageView) rootView.findViewById(R.id.tx2);
        tx2.setOnClickListener(new MyClickChangeSelectTx2Listener());
        tx3 = (ImageView) rootView.findViewById(R.id.tx3);
        tx3.setOnClickListener(new MyClickChangeSelectTx3Listener());
        tx4 = (ImageView) rootView.findViewById(R.id.tx4);
        tx4.setOnClickListener(new MyClickChangeSelectTx4Listener());
        tx5 = (ImageView) rootView.findViewById(R.id.tx5);
        tx5.setOnClickListener(new MyClickChangeSelectTx5Listener());
        tx6 = (ImageView) rootView.findViewById(R.id.tx6);
        tx6.setOnClickListener(new MyClickChangeSelectTx6Listener());
        tx7 = (ImageView) rootView.findViewById(R.id.tx7);
        tx7.setOnClickListener(new MyClickChangeSelectTx7Listener());
        tx8 = (ImageView) rootView.findViewById(R.id.tx8);
        tx8.setOnClickListener(new MyClickChangeSelectTx8Listener());

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

    //点击确认监视器 尼见 2015-03-03
    class MyClickConfirmTextViewListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            listener.onConfirmInterface(portrait_id);
            dismiss();
        }
    }

    //点击取消监视器 尼见 2015-03-03
    class MyClickCancelTextViewListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    //选中头像1监视器 尼见 2015-03-03
    class MyClickChangeSelectTx1Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.xz1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "1";
        }
    }

    //选中头像2监视器 尼见 2015-03-03
    class MyClickChangeSelectTx2Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.xz2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "2";
        }
    }

    //选中头像3监视器 尼见 2015-03-03
    class MyClickChangeSelectTx3Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.xz3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "3";
        }
    }

    //选中头像4监视器 尼见 2015-03-03
    class MyClickChangeSelectTx4Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.xz4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "4";
        }
    }

    //选中头像5监视器 尼见 2015-03-03
    class MyClickChangeSelectTx5Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.xz5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "5";
        }
    }

    //选中头像6监视器 尼见 2015-03-03
    class MyClickChangeSelectTx6Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.xz6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "6";
        }
    }

    //选中头像7监视器 尼见 2015-03-03
    class MyClickChangeSelectTx7Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.xz7);
            tx8.setImageResource(R.drawable.tx8);
            portrait_id = "7";
        }
    }

    //选中头像8监视器 尼见 2015-03-03
    class MyClickChangeSelectTx8Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tx1.setImageResource(R.drawable.tx1);
            tx2.setImageResource(R.drawable.tx2);
            tx3.setImageResource(R.drawable.tx3);
            tx4.setImageResource(R.drawable.tx4);
            tx5.setImageResource(R.drawable.tx5);
            tx6.setImageResource(R.drawable.tx6);
            tx7.setImageResource(R.drawable.tx7);
            tx8.setImageResource(R.drawable.xz8);
            portrait_id = "8";
        }
    }

}