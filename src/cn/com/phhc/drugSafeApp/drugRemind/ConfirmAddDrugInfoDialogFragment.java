package cn.com.phhc.drugSafeApp.drugRemind;
/**
 * 确认添加的服药信息
 */

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.AddDrugInfo;
import cn.com.phhc.drugSafeApp.utils.ConfirmAddDrugInfoItemAdapter;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.DrugItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/2/27.
 */
public class ConfirmAddDrugInfoDialogFragment extends DialogFragment {

    CreateSQLiteDatabase databaseHelper;
    TextView confirm, cancel;
    List<AddDrugInfo> listAddDrugInfo;
    ConfirmInterface listener;
    ListView drugInfo;
    ArrayList<DrugItem> list;
    ConfirmAddDrugInfoItemAdapter confirmAddDrugInfoItemAdapter;
    String databaseName;

    public static ConfirmAddDrugInfoDialogFragment newInstance(String sex) {
        ConfirmAddDrugInfoDialogFragment frag = new ConfirmAddDrugInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("sex", sex);
        frag.setArguments(args);
        return frag;
    }

    public interface ConfirmInterface {
        public void onConfirmInterface(String left);
    }

    public void setConfirmInterface(ConfirmInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        databaseName = "LocalDrugMessage";
        databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName, null, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.confirm_add_drug_info_dialog, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        //取消
        cancel = (TextView) rootView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new MyClickCancelListener());
        //确定
        confirm = (TextView) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new MyClickConfirmListener());
        //设置listView内容
        list = new ArrayList<>();
        listAddDrugInfo = new ArrayList<AddDrugInfo>();
        listAddDrugInfo = (ArrayList) getArguments().getCharSequenceArrayList("list");
        for (int i = 0; i < listAddDrugInfo.size(); i++) {
            list.add(new DrugItem(listAddDrugInfo.get(i).getDrugName()));
        }
        //去掉顶部的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        drugInfo = (ListView) rootView.findViewById(R.id.drugInfo);
        //配置适配器
        confirmAddDrugInfoItemAdapter = new ConfirmAddDrugInfoItemAdapter();
        confirmAddDrugInfoItemAdapter.setContext(getActivity());
        confirmAddDrugInfoItemAdapter.setArrayList(list);
        drugInfo.setAdapter(confirmAddDrugInfoItemAdapter);

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
    class MyClickConfirmListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            listener.onConfirmInterface("");
            dismiss();
        }
    }

    //点击取消监视器 尼见 2015-03-03
    class MyClickCancelListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            dismiss();
        }
    }

}
