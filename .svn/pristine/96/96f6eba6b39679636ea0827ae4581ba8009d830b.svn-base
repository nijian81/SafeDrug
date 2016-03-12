package cn.com.phhc.drugSafeApp.drugRemind;
/**
 * 该类是添加药品界面，点击输入药品名称时，弹出的fragment，
 * 暂时废弃不用，以后如果觉得dialogFragment不好的话，在重新启动。
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.CharacterParser;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;
import cn.com.phhc.drugSafeApp.utils.DrugItem;
import cn.com.phhc.drugSafeApp.utils.DrugItemAdapter;
import cn.com.phhc.drugSafeApp.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;

public class DrugNameQueryFragment extends Fragment {

    ConfirmInterface listener;
    ImageView back;
    EditText input_search;
    TextView confirm;
    ListView listView;
    PinyinComparator pinyinComparator;
    CharacterParser characterParser;
    ArrayList<DrugItem> list;
    DrugItemAdapter drugItemAdapter;
    CreateSQLiteDatabase databaseHelper;
    SQLiteDatabase db;
    String databaseName, drugName, drugNameParams, productDrugId, basicDrugId;

    public interface ConfirmInterface {
        public void onConfirmInterface(String drugName);
    }

    public void setConfirmInterface(ConfirmInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        databaseName = "drug_info";
        databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName, null, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.drug_name_query_fragment, container, false);

        drugNameParams = getArguments().getString("drugName");
        //返回按钮
        back = (ImageView) rootView.findViewById(R.id.back);
        back.setOnClickListener(new MyClickBackListener());
        input_search = (EditText) rootView.findViewById(R.id.input_search);
        input_search.setText(drugNameParams);
        //实现搜索框改变监听器
        input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String sql = "select showName, firstWord, productDrugId, basicDrugId, commonPinyin from drugSearchTable where type = '1' order by commonPinYin asc";
        db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        list = new ArrayList<DrugItem>();
        while (cursor.moveToNext()) {
            drugName = cursor.getString(0);
            String letter = cursor.getString(1);
            productDrugId = cursor.getString(2);
            basicDrugId = cursor.getString(3);
            String pinyin = cursor.getString(4);
            list.add(new DrugItem(productDrugId, drugName, basicDrugId, pinyin, letter));
        }
        db.close();
        drugItemAdapter = new DrugItemAdapter();
        drugItemAdapter.setContext(getActivity());
        drugItemAdapter.setArrayList(list);
        Collections.sort(list, pinyinComparator);
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(drugItemAdapter);
        listView.setVisibility(View.INVISIBLE);
        confirm = (TextView) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                // 显示或者隐藏输入法
                imm.hideSoftInputFromWindow(confirm.getWindowToken(), 0);
                AddDrugFragment addDrugFragment = new AddDrugFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down);
                //接口回调
                Bundle bundle = new Bundle();
                bundle.putString("drugName", input_search.getText().toString());
                addDrugFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.login_fragment, addDrugFragment, "addDrugFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                // 显示或者隐藏输入法
                DrugItem drugItem = (DrugItem) listView.getItemAtPosition(position);
                imm.hideSoftInputFromWindow(confirm.getWindowToken(), 0);
                AddDrugFragment addDrugFragment = new AddDrugFragment();
                Bundle bundle = new Bundle();
                bundle.putString("drugName", drugItem.getDrugName());
                addDrugFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down);
                fragmentTransaction.replace(R.id.login_fragment, addDrugFragment, "addDrugFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

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

    //点击返回药品添加页面监视器 尼见 2015-03-19
    class MyClickBackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
            // 显示或者隐藏输入法
            imm.hideSoftInputFromWindow(confirm.getWindowToken(), 0);
            AddDrugFragment addDrugFragment = new AddDrugFragment();
            Bundle bundle = new Bundle();
            bundle.putString("drugName", drugNameParams);
            addDrugFragment.setArguments(bundle);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down);
            fragmentTransaction.replace(R.id.login_fragment, addDrugFragment, "addDrugFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    //输入字符过滤药品监视器 尼见 2015-03-19
    private void filterData(String filterStr) {
        ArrayList<DrugItem> filterDateList = new ArrayList<DrugItem>();
        String upper = filterStr.toUpperCase();
        if (TextUtils.isEmpty(upper)) {
            filterDateList = list;
            drugItemAdapter.updateListView(filterDateList);
            listView.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (DrugItem drugItem : list) {
                if (drugItem.getPinyin().contains(upper)) {
                    filterDateList.add(drugItem);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        drugItemAdapter.updateListView(filterDateList);
        listView.setVisibility(View.VISIBLE);
    }

}