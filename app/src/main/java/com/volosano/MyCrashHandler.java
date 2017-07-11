package com.volosano;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
 * 
 * @author Administrator
 * 
 */
public class MyCrashHandler implements UncaughtExceptionHandler {
	
	private static MyCrashHandler myCrashHandler;
	private Context context;

	public static boolean IM_TESTOR = true;
	
	private MyCrashHandler() {
		
	}

	public static synchronized MyCrashHandler getInstance() {
		if (myCrashHandler == null) {
            myCrashHandler = new MyCrashHandler();
		}
        return myCrashHandler;
	}
	
	public void init(Context context) {
		this.context = context;
        //获取系统默认的UncaughtException处理器
        //mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

	public void uncaughtException(Thread arg0, Throwable arg1) {
		// 1.获取当前程序的版本号. 版本的id
		String versioninfo = getVersionInfo();
		// 2.获取手机的硬件信息.
		String mobileInfo = getMobileInfo();
		// 3.把错误的堆栈信息 获取出来
		String errorinfo = getErrorInfo(arg1);

		Log.e("errorInfo", errorinfo);

		if(!BuildConfig.DEBUG && !IM_TESTOR) {
			//干掉当前的程序
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 获取错误的信息
	 * 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 * 
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try {
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				// 暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 * 
	 * @return
	 */
	private String getVersionInfo() {
		try {
			PackageManager pm = this.context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(this.context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}
}
