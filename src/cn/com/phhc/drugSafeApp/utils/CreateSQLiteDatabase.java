package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author lenovo 创建数据库
 *
 */

public class CreateSQLiteDatabase extends SQLiteOpenHelper {

    public CreateSQLiteDatabase(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        // TODO Auto-generated constructor stub
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS anonymityRegister"
                + "(id INTEGER primary key,name,password,sys_userID,code,dtype,uuid,isLogin,flag,ring,timeStamp,current_member_id,current_member_portrait,current_member_name)";
        db.execSQL(sql);
        //isLogin字段代表是否登录，‘0’表示未登录，‘1’表示已登录
        //sys_userID是注册时候服务器返回的id，具有唯一标识的作用，修改密码的时候会用到这个字段，如果新用户登录，可以调用下行接口去同步用户的sys_userID
        //flag字段用来标志初始化进入我的账户页面的时候，0表示隐藏修改密码、显示已有账户登录和注册，1表示显示修改密码、退出，隐藏已有账户登录和注册。
        String sql1 = "insert into anonymityRegister (id,name,password,sys_userID,code,dtype,uuid,isLogin,flag,ring,timeStamp,current_member_id,current_member_portrait,current_member_name) values(1,'','','','','','',0,0,1,'2015-03-13 18:20:00','','','')";
        db.execSQL(sql1);
        //将个人信息保存在本地，用表personalInformation去保存
        String sql2 = "CREATE TABLE IF NOT EXISTS MemberInfoTable"
                + "(memberID char(36) NOT NULL, name NOT NULL, isFamily NOT NULL, photo, tel, sex, birth, getup, breakfast, lunch, dinner, sleep, updateTime, updateState)";
        db.execSQL(sql2);
        //药品信息表
        String sql3 = "Create  TABLE DrugInfoTable(\n" +
                "guideDrugID char(36) PRIMARY KEY UNIQUE NOT NULL\n" +
                ", userID char(36) NOT NULL\n" +
                ", memberID char(36) NOT NULL\n" +
                ", drugID varchar(20)\n" +
                ", drugName nvarchar(100) NOT NULL\n" +
                ", beginDate varchar(10) NOT NULL\n" +
                ", endDate varchar(10) NOT NULL\n" +
                ", note text\n" +
                ", updateTime text\n" +
                ", updateState char(5) NOT NULL\n" +
                "\n" +
                ")";
        db.execSQL(sql3);
        //服药时间表
        String sql4 = "Create  TABLE TakeTimeTable(\n" +
                "drugID char(36)\n" +
                ", guideDrugID char(36) NOT NULL\n" +
                ", memberID char(36) NOT NULL\n" +
                ", drugName text NOT NULL\n" +
                ", date char(10) NOT NULL\n" +
                ", time char(5) NOT NULL\n" +
                ", ConfirmStates text NOT NULL\n" +
                ", ConfirmTime datetime\n" +
                ", cycle float\n" +
                ", freq varchar(36) NOT NULL\n" +
                ", dose float NOT NULL\n" +
                ", doseUnit varchar(20) NOT NULL\n" +
                ", condition nvarchar(100)\n" +
                ", updateTime text\n" +
                ", updateState char(5) NOT NULL\n" +
                "\n" +
                ")";
        db.execSQL(sql4);
        //这个表暂时没用,先别删除，怕出错。
        String sql5 = "CREATE TABLE IF NOT EXISTS tempAddDrugInfo" +
                "(id INTEGER primary key,drugName,drugFreq,drugDose,beginDate,endDate,interval,remark)";
        db.execSQL(sql5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}
