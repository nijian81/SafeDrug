package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 更改昵称对话框
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import cn.com.phhc.drugSafeApp.R;

/**
 * Created by lenovo on 2015/2/27.
 */
public class ChangeHoneyNameDialogFragment extends DialogFragment {

    ConfirmInterface listener;
    EditText honey_name_edit_text;
    String honey_name;
    Button button_cancel,button_save;

    public static ChangeHoneyNameDialogFragment newInstance(int title) {
        ChangeHoneyNameDialogFragment frag = new ChangeHoneyNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("patient", "2");
        frag.setArguments(args);
        return frag;
    }

    public interface ConfirmInterface {
        public void onConfirmInterface(String honeyName);
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

        View rootView = inflater.inflate(R.layout.change_honey_name_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        honey_name_edit_text = (EditText) rootView.findViewById(R.id.input_honey_name);
        button_cancel = (Button) rootView.findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new MyClickCancelButtonListener());
        button_save = (Button) rootView.findViewById(R.id.save);
        button_save.setOnClickListener(new MyClickSaveButtonListener());

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

    //点击取消监视器 尼见 2015-03-03
    class MyClickCancelButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(honey_name_edit_text.getWindowToken(), 0);
            dismiss();
        }
    }

    //点击保存监视器 尼见 2015-03-03
    class MyClickSaveButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(honey_name_edit_text.getWindowToken(), 0);
            honey_name = honey_name_edit_text.getText().toString();
            listener.onConfirmInterface(honey_name);
            dismiss();
        }
    }

}
