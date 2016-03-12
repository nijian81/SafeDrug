package cn.com.phhc.drugSafeApp.util;

import android.util.Log;

public class LogUtil {

	public static boolean IS_LOG = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "PHHC";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (IS_LOG && msg != null)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (IS_LOG && msg != null)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (IS_LOG && msg != null)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (IS_LOG && msg != null)
			Log.v(TAG, msg);
	}
	//下面是传入类名打印log
	public static void i(Class<?> _class,String msg){
		if (IS_LOG && msg != null)
			Log.i(_class.getName(), msg);
	}
	public static void d(Class<?> _class,String msg){
		if (IS_LOG && msg != null)
			Log.i(_class.getName(), msg);
	}
	public static void e(Class<?> _class,String msg){
		if (IS_LOG && msg != null)
			Log.i(_class.getName(), msg);
	}
	public static void v(Class<?> _class,String msg){
		if (IS_LOG && msg != null)
			Log.i(_class.getName(), msg);
	}
	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (IS_LOG && msg != null)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (IS_LOG && msg != null)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (IS_LOG && msg != null)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (IS_LOG && msg != null)
			Log.i(tag, msg);
	}
}
