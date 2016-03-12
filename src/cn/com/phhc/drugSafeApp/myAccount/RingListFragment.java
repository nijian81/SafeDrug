package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 铃声列表
 */

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;
import cn.com.phhc.drugSafeApp.utils.CreateSQLiteDatabase;

import java.util.HashMap;

public class RingListFragment extends Fragment {

    CreateSQLiteDatabase databaseHelper;
    SQLiteDatabase db;
    String databaseName;
    SettingFragment settingFragment;
    ImageButton back_ring_list;
    String ring;
    SoundPool soundPool1,soundPool2,soundPool3,soundPool4,soundPool5,soundPool6,soundPool7,soundPool8,soundPool9;
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    RelativeLayout cuckoo, electric, happy, guitar, churchRing, birdVoice, dogVoice, mysteryNote, note;
    ImageView mark_cuckoo, mark_electric, mark_happy, mark_guitar, mark_churchRing, mark_birdVoice, mark_dogVoice, mark_mysteryNote, mark_note;
    TextView save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        databaseName = "LocalDrugMessage";
        databaseHelper = new CreateSQLiteDatabase(getActivity(), databaseName, null, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ring_list_fragment, container, false);
        //保存按钮
        save = (TextView)rootView.findViewById(R.id.save);
        save.setOnClickListener(new MyClickSaveListener());
        //返回按钮
        back_ring_list = (ImageButton) rootView.findViewById(R.id.back_ring_list);
        back_ring_list.setOnClickListener(new MyClickBackSettingListener());
        //布谷鸟 1
        cuckoo = (RelativeLayout) rootView.findViewById(R.id.cuckoo);
        mark_cuckoo = (ImageView) rootView.findViewById(R.id.mark_cuckoo);
        cuckoo.setOnClickListener(new MyClickMarkCuckooListener());
        //电子 2
        electric = (RelativeLayout) rootView.findViewById(R.id.electric);
        mark_electric = (ImageView) rootView.findViewById(R.id.mark_electric);
        electric.setOnClickListener(new MyClickMarkElectricListener());
        //欢乐 3
        happy = (RelativeLayout) rootView.findViewById(R.id.happy);
        mark_happy = (ImageView) rootView.findViewById(R.id.mark_happy);
        happy.setOnClickListener(new MyClickMarkHappyListener());
        //吉他 4
        guitar = (RelativeLayout) rootView.findViewById(R.id.guitar);
        mark_guitar = (ImageView) rootView.findViewById(R.id.mark_guitar);
        guitar.setOnClickListener(new MyClickMarkGuitarListener());
        //教堂钟声 5
        churchRing = (RelativeLayout) rootView.findViewById(R.id.churchRing);
        mark_churchRing = (ImageView) rootView.findViewById(R.id.mark_churchRing);
        churchRing.setOnClickListener(new MyClickMarkChurchRingListener());
        //鸟语 6
        birdVoice = (RelativeLayout) rootView.findViewById(R.id.birdVoice);
        mark_birdVoice = (ImageView) rootView.findViewById(R.id.mark_birdVoice);
        birdVoice.setOnClickListener(new MyClickMarkBirdVoiceListener());
        //犬吠 7
        dogVoice = (RelativeLayout) rootView.findViewById(R.id.dogVoice);
        mark_dogVoice = (ImageView) rootView.findViewById(R.id.mark_dogVoice);
        dogVoice.setOnClickListener(new MyClickMarkDogVoiceListener());
        //神秘音符 8
        mysteryNote = (RelativeLayout) rootView.findViewById(R.id.mysteryNote);
        mark_mysteryNote = (ImageView) rootView.findViewById(R.id.mark_mysteryNote);
        mysteryNote.setOnClickListener(new MyClickMarkMysteryNoteListener());
        //音符 9
        note = (RelativeLayout) rootView.findViewById(R.id.note);
        mark_note = (ImageView) rootView.findViewById(R.id.mark_note);
        note.setOnClickListener(new MyClickMarkNoteListener());
        //铃音音频文件加载
        soundPool1 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool2 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool3 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool4 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool5 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool6 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool7 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool8 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundPool9 = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool1.load(getActivity(), R.raw.cuckoo, 1));
        soundMap.put(1, soundPool2.load(getActivity(), R.raw.electric, 1));
        soundMap.put(1, soundPool3.load(getActivity(), R.raw.happy, 1));
        soundMap.put(1, soundPool4.load(getActivity(), R.raw.guigar, 1));
        soundMap.put(1, soundPool5.load(getActivity(), R.raw.church_ring, 1));
        soundMap.put(1, soundPool6.load(getActivity(), R.raw.bird_voice, 1));
        soundMap.put(1, soundPool7.load(getActivity(), R.raw.dog_voice, 1));
        soundMap.put(1, soundPool8.load(getActivity(), R.raw.mystery_note, 1));
        soundMap.put(1, soundPool9.load(getActivity(), R.raw.note, 1));

        try {
            db = databaseHelper.getWritableDatabase();
            String sql = "select * from anonymityRegister";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                ring = cursor.getString(9);
            }
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        switch (ring) {
            case "1":
                mark_cuckoo.setVisibility(View.VISIBLE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "2":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.VISIBLE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "3":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.VISIBLE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "4":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.VISIBLE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "5":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.VISIBLE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "6":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.VISIBLE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "7":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.VISIBLE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
            case "8":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.VISIBLE);
                mark_note.setVisibility(View.GONE);
                break;
            case "9":
                mark_cuckoo.setVisibility(View.GONE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.VISIBLE);
                break;
            default:
                mark_cuckoo.setVisibility(View.VISIBLE);
                mark_electric.setVisibility(View.GONE);
                mark_happy.setVisibility(View.GONE);
                mark_guitar.setVisibility(View.GONE);
                mark_churchRing.setVisibility(View.GONE);
                mark_birdVoice.setVisibility(View.GONE);
                mark_dogVoice.setVisibility(View.GONE);
                mark_mysteryNote.setVisibility(View.GONE);
                mark_note.setVisibility(View.GONE);
                break;
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

    //铃音列表中点击开始返回按钮的监听事件 尼见 2015-02-26
    class MyClickBackSettingListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            settingFragment = new SettingFragment();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.login_fragment, settingFragment, "settingFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    //点击铃音列表布谷鸟监视器 尼见 2015-03-02
    class MyClickMarkCuckooListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool1.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.VISIBLE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，1表示布谷鸟
                String sql = "update anonymityRegister set ring = '1'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表电子监视器 尼见 2015-03-02
    class MyClickMarkElectricListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool2.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.VISIBLE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，2表示电子
                String sql = "update anonymityRegister set ring = '2'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表欢乐监视器 尼见 2015-03-02
    class MyClickMarkHappyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool3.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.VISIBLE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，3表示欢乐
                String sql = "update anonymityRegister set ring = '3'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表吉他监视器 尼见 2015-03-02
    class MyClickMarkGuitarListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool4.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.VISIBLE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，4表示吉他
                String sql = "update anonymityRegister set ring = '4'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表教堂钟声监视器 尼见 2015-03-02
    class MyClickMarkChurchRingListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool5.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.VISIBLE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，5表示教堂钟声
                String sql = "update anonymityRegister set ring = '5'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表鸟语监视器 尼见 2015-03-02
    class MyClickMarkBirdVoiceListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool6.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.VISIBLE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，6表示鸟语
                String sql = "update anonymityRegister set ring = '6'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表犬吠监视器 尼见 2015-03-02
    class MyClickMarkDogVoiceListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool7.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool8.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.VISIBLE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，7表示犬吠
                String sql = "update anonymityRegister set ring = '7'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表神秘音符监视器 尼见 2015-03-02
    class MyClickMarkMysteryNoteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool8.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool9.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.VISIBLE);
            mark_note.setVisibility(View.GONE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，8表示神秘音符
                String sql = "update anonymityRegister set ring = '8'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击铃音列表音符监视器 尼见 2015-03-02
    class MyClickMarkNoteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            soundPool9.play(soundMap.get(1), 1, 1, 0, 0, 1);
            soundPool1.autoPause();
            soundPool2.autoPause();
            soundPool3.autoPause();
            soundPool4.autoPause();
            soundPool5.autoPause();
            soundPool6.autoPause();
            soundPool7.autoPause();
            soundPool8.autoPause();
            mark_cuckoo.setVisibility(View.GONE);
            mark_electric.setVisibility(View.GONE);
            mark_happy.setVisibility(View.GONE);
            mark_guitar.setVisibility(View.GONE);
            mark_churchRing.setVisibility(View.GONE);
            mark_birdVoice.setVisibility(View.GONE);
            mark_dogVoice.setVisibility(View.GONE);
            mark_mysteryNote.setVisibility(View.GONE);
            mark_note.setVisibility(View.VISIBLE);
            try {
                db = databaseHelper.getWritableDatabase();
                //对数据库中铃音标志字段更新，9表示音符
                String sql = "update anonymityRegister set ring = '9'";
                db.execSQL(sql);
                db.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    //点击保存监视器 尼见 2015-03-02
    class MyClickSaveListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    AlertDialog dialog = getAlertSaveSuccess();
                    dialog.show();
                }
            });
        }
    }

    //手机号不正确警告框 尼见 2015-02-28
    AlertDialog getAlertSaveSuccess() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        builder.setTitle("提示");
        builder.setMessage("设置成功");
        builder.setPositiveButton("确定", null);
        builder.setCancelable(false);
        android.app.AlertDialog dialog = builder.create();
        return dialog;

    }

}