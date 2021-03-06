package cn.com.phhc.drugSafeApp.drugRemind;
/**
 * 当生成用药提示之后，设置未生成服药时间点的药品，对应的设置服药时间点页面
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.myAccount.InputRelaxTimeFragment;
import cn.com.phhc.drugSafeApp.utils.GenerateTakeDrugTimeList;
import cn.com.phhc.drugSafeApp.utils.TakeTimeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SetTakeDrugTimeFragment extends Fragment {

    int isAllSetTakeDrugTime = 1, setNum = 0, location;
    List<GenerateTakeDrugTimeList> listGenerateTakeTime;
    String takeDrugFrequency, time[] = new String[10];
    ImageView back;
    RelativeLayout set_time_1, set_time_2, set_time_3, set_time_4, set_time_5, set_time_6, set_time_7, set_time_8, set_time_9, set_time_10;
    TextView time_one_text, time_two_text, time_three_text, time_four_text, time_five_text, time_six_text, time_seven_text, time_eight_text, time_nine_text, time_ten_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.set_take_drug_time_fragment,
                container, false);

        listGenerateTakeTime = new ArrayList<GenerateTakeDrugTimeList>();
        listGenerateTakeTime = (ArrayList) getArguments().getCharSequenceArrayList("takeDrugTime");
        takeDrugFrequency = getArguments().getString("takeDrugFrequency");
        //setNum代表将要设置的第几项内容
        setNum = getArguments().getInt("setNum");
        location = getArguments().getInt("location");
        //返回
        back = (ImageView) rootView.findViewById(R.id.back);
        back.setOnClickListener(new MyClickBackListener());
        //设置用药时间
        set_time_1 = (RelativeLayout) rootView.findViewById(R.id.set_time_1);
        set_time_1.setVisibility(View.GONE);
        set_time_1.setOnClickListener(new MyClickSetTimeListenerOne());
        time_one_text = (TextView) rootView.findViewById(R.id.time_one_text);
        set_time_2 = (RelativeLayout) rootView.findViewById(R.id.set_time_2);
        set_time_2.setVisibility(View.GONE);
        set_time_2.setOnClickListener(new MyClickSetTimeListenerTwo());
        time_two_text = (TextView) rootView.findViewById(R.id.time_two_text);
        set_time_3 = (RelativeLayout) rootView.findViewById(R.id.set_time_3);
        set_time_3.setVisibility(View.GONE);
        set_time_3.setOnClickListener(new MyClickSetTimeListenerThree());
        time_three_text = (TextView) rootView.findViewById(R.id.time_three_text);
        set_time_4 = (RelativeLayout) rootView.findViewById(R.id.set_time_4);
        set_time_4.setVisibility(View.GONE);
        set_time_4.setOnClickListener(new MyClickSetTimeListenerFour());
        time_four_text = (TextView) rootView.findViewById(R.id.time_four_text);
        set_time_5 = (RelativeLayout) rootView.findViewById(R.id.set_time_5);
        set_time_5.setVisibility(View.GONE);
        set_time_5.setOnClickListener(new MyClickSetTimeListenerFive());
        time_five_text = (TextView) rootView.findViewById(R.id.time_five_text);
        set_time_6 = (RelativeLayout) rootView.findViewById(R.id.set_time_6);
        set_time_6.setVisibility(View.GONE);
        set_time_6.setOnClickListener(new MyClickSetTimeListenerSix());
        time_six_text = (TextView) rootView.findViewById(R.id.time_six_text);
        set_time_7 = (RelativeLayout) rootView.findViewById(R.id.set_time_7);
        set_time_7.setVisibility(View.GONE);
        set_time_7.setOnClickListener(new MyClickSetTimeListenerSeven());
        time_seven_text = (TextView) rootView.findViewById(R.id.time_seven_text);
        set_time_8 = (RelativeLayout) rootView.findViewById(R.id.set_time_8);
        set_time_8.setVisibility(View.GONE);
        set_time_8.setOnClickListener(new MyClickSetTimeListenerEight());
        time_eight_text = (TextView) rootView.findViewById(R.id.time_eight_text);
        set_time_9 = (RelativeLayout) rootView.findViewById(R.id.set_time_9);
        set_time_9.setVisibility(View.GONE);
        set_time_9.setOnClickListener(new MyClickSetTimeListenerNine());
        time_nine_text = (TextView) rootView.findViewById(R.id.time_nine_text);
        set_time_10 = (RelativeLayout) rootView.findViewById(R.id.set_time_10);
        set_time_10.setVisibility(View.GONE);
        set_time_10.setOnClickListener(new MyClickSetTimeListenerTen());
        time_ten_text = (TextView) rootView.findViewById(R.id.time_ten_text);
        //根据不同的服药频率去显示
        if (takeDrugFrequency.equals("1")) {
            set_time_1.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("2")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("3")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("4")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("5")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("6")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
            set_time_6.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("7")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
            set_time_6.setVisibility(View.VISIBLE);
            set_time_7.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("8")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
            set_time_6.setVisibility(View.VISIBLE);
            set_time_7.setVisibility(View.VISIBLE);
            set_time_8.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("9")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
            set_time_6.setVisibility(View.VISIBLE);
            set_time_7.setVisibility(View.VISIBLE);
            set_time_8.setVisibility(View.VISIBLE);
            set_time_9.setVisibility(View.VISIBLE);
            set_time_10.setVisibility(View.VISIBLE);
        }
        if (takeDrugFrequency.equals("10")) {
            set_time_1.setVisibility(View.VISIBLE);
            set_time_2.setVisibility(View.VISIBLE);
            set_time_3.setVisibility(View.VISIBLE);
            set_time_4.setVisibility(View.VISIBLE);
            set_time_5.setVisibility(View.VISIBLE);
            set_time_6.setVisibility(View.VISIBLE);
            set_time_7.setVisibility(View.VISIBLE);
            set_time_8.setVisibility(View.VISIBLE);
            set_time_9.setVisibility(View.VISIBLE);
            set_time_10.setVisibility(View.VISIBLE);
        }

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

    //点击返回监视器 尼见 2015-04-07
    class MyClickBackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            isAllSetTakeDrugTime = 1;

            if (set_time_1.getVisibility() == View.VISIBLE) {
                if (time_one_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_2.getVisibility() == View.VISIBLE) {
                if (time_two_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_3.getVisibility() == View.VISIBLE) {
                if (time_three_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_4.getVisibility() == View.VISIBLE) {
                if (time_four_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_5.getVisibility() == View.VISIBLE) {
                if (time_five_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_6.getVisibility() == View.VISIBLE) {
                if (time_six_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_7.getVisibility() == View.VISIBLE) {
                if (time_three_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_8.getVisibility() == View.VISIBLE) {
                if (time_eight_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_9.getVisibility() == View.VISIBLE) {
                if (time_nine_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }
            if (set_time_10.getVisibility() == View.VISIBLE) {
                if (time_ten_text.getText().toString().equals("请设置用药时间")) {
                    isAllSetTakeDrugTime = 0;
                }
            }

            if (isAllSetTakeDrugTime == 0) {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        AlertDialog dialog = getAlertDialogWithTakeTimeNotSet();
                        dialog.show();
                    }
                });
            } else {
                //根据设置的值，更新内存中的对应药品的服药时间表
                ArrayList<TakeTimeList> list2 = new ArrayList<>();
                //times表示一天需要设置的服药次数
                int times = Integer.parseInt(takeDrugFrequency);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //将开始服药日期解析为毫秒形式
                String[] takeDate = listGenerateTakeTime.get(setNum).getBegin().split("-");
                int year = Integer.parseInt(takeDate[0]);
                int month = Integer.parseInt(takeDate[1]);
                int day = Integer.parseInt(takeDate[2]);
                Date date = new Date(year - 1900, month-1, day);
                long millisecond = date.getTime();
                //将结束服药日期解析为毫秒形式
                String[] takeDateEnd = listGenerateTakeTime.get(setNum).getEnd().split("-");
                int yearEnd = Integer.parseInt(takeDateEnd[0]);
                int monthEnd = Integer.parseInt(takeDateEnd[1]);
                int dayEnd = Integer.parseInt(takeDateEnd[2]);
                Date dateEnd = new Date(yearEnd - 1900, monthEnd-1, dayEnd);
                long millisecondEnd = dateEnd.getTime();
                //dayCount代表开始日期和结束日期之间的天数
                long dayCount = (millisecondEnd - millisecond) / 86400000 + 1;
                for (int j = 0; j < dayCount; j++) {
                    Date curDate = new Date(millisecond + j * 86400000);
                    String takeDrugDate = formatter.format(curDate);
                    for (int i = 0; i < times; i++) {
                        TakeTimeList takeTimeList = new TakeTimeList();
                        takeTimeList.setDate(takeDrugDate);
                        takeTimeList.setTime(time[i]);
                        list2.add(takeTimeList);
                    }
                }
                listGenerateTakeTime.get(location).setTakeTimeList(list2);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GenerateDrugTipsFragment generateDrugTipsFragment = new GenerateDrugTipsFragment();
                Bundle bundle = new Bundle();
                bundle.putCharSequenceArrayList("takeDrugTime", (ArrayList) listGenerateTakeTime);
                generateDrugTipsFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.login_fragment, generateDrugTipsFragment, "generateDrugTipsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        //服药时间没有设置提示框 尼见 2015-04-08
        AlertDialog getAlertDialogWithTakeTimeNotSet() {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

            builder.setTitle("提示");
            builder.setMessage("有药品尚未设置服药时间，请设置后继续");
            builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    GenerateDrugTipsFragment generateDrugTipsFragment = new GenerateDrugTipsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putCharSequenceArrayList("takeDrugTime", (ArrayList) listGenerateTakeTime);
                    generateDrugTipsFragment.setArguments(bundle);
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.login_fragment, generateDrugTipsFragment, "generateDrugTipsFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            builder.setCancelable(false);
            android.app.AlertDialog dialog = builder.create();
            return dialog;
        }

    }

    //点击设置时间1监视器 尼见 2015-04-08
    class MyClickSetTimeListenerOne implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_one_text.setText(hour_show + ":" + minute_show);
                    time[0] = time_one_text.getText().toString();
                }
            });
            if (time_one_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_one_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间2监视器 尼见 2015-04-08
    class MyClickSetTimeListenerTwo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_two_text.setText(hour_show + ":" + minute_show);
                    time[1] = time_two_text.getText().toString();
                }
            });
            if (time_two_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_two_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间3监视器 尼见 2015-04-08
    class MyClickSetTimeListenerThree implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_three_text.setText(hour_show + ":" + minute_show);
                    time[2] = time_three_text.getText().toString();
                }
            });
            if (time_three_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_three_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间4监视器 尼见 2015-04-08
    class MyClickSetTimeListenerFour implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_four_text.setText(hour_show + ":" + minute_show);
                    time[3] = time_four_text.getText().toString();
                }
            });
            if (time_four_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_four_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间5监视器 尼见 2015-04-08
    class MyClickSetTimeListenerFive implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_five_text.setText(hour_show + ":" + minute_show);
                    time[4] = time_five_text.getText().toString();
                }
            });
            if (time_five_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_five_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间6监视器 尼见 2015-04-08
    class MyClickSetTimeListenerSix implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_six_text.setText(hour_show + ":" + minute_show);
                    time[5] = time_six_text.getText().toString();
                }
            });
            if (time_six_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_six_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间7监视器 尼见 2015-04-08
    class MyClickSetTimeListenerSeven implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_seven_text.setText(hour_show + ":" + minute_show);
                    time[6] = time_seven_text.getText().toString();
                }
            });
            if (time_seven_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_seven_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间8监视器 尼见 2015-04-08
    class MyClickSetTimeListenerEight implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_eight_text.setText(hour_show + ":" + minute_show);
                    time[7] = time_eight_text.getText().toString();
                }
            });
            if (time_eight_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_eight_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间9监视器 尼见 2015-04-08
    class MyClickSetTimeListenerNine implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_nine_text.setText(hour_show + ":" + minute_show);
                    time[8] = time_nine_text.getText().toString();
                }
            });
            if (time_nine_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_nine_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

    //点击设置时间10监视器 尼见 2015-04-08
    class MyClickSetTimeListenerTen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputRelaxTimeFragment inputRelaxTimeFragment = new InputRelaxTimeFragment();
            inputRelaxTimeFragment.setConfirmInterface(new InputRelaxTimeFragment.ConfirmInterface() {
                @Override
                public void onConfirmInterface(int hour, int minute) {
                    String minute_show = "00";
                    String hour_show = "00";
                    if (minute <= 9) {
                        minute_show = "0" + minute;
                    } else {
                        minute_show = Integer.toString(minute);
                    }
                    if (hour <= 9) {
                        hour_show = "0" + hour;
                    } else {
                        hour_show = Integer.toString(hour);
                    }
                    time_ten_text.setText(hour_show + ":" + minute_show);
                    time[9] = time_ten_text.getText().toString();
                }
            });
            if (time_ten_text.getText().toString().equals("请设置用药时间")) {
                Bundle bundle = new Bundle();
                bundle.putInt("hour", 00);
                bundle.putInt("minute", 00);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            } else {
                String time[] = time_ten_text.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                inputRelaxTimeFragment.setArguments(bundle);
                inputRelaxTimeFragment.show(getActivity().getFragmentManager(), "inputBirthdayDialogFragment");
            }
        }
    }

}