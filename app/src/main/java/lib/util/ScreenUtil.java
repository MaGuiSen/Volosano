package lib.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

	/**
	 * 获取屏幕长宽，当orientation=null or Configuration.ORIENTATION_PORTRAIT时获取默认的屏幕长宽，
	 * 当orientation=Configuration.ORIENTATION_LANDSCAPE时，获取的长宽等于屏幕真实方向的宽长。
	 * @param context
	 * @param orientation 屏幕方向，可以为null
	 * @return int[] 0-width, 1-height
	 */
	public static int[] getScreenSize(Context context, Integer orientation){
		int[] screen = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wManager.getDefaultDisplay().getMetrics(dm);
		if(orientation != null && orientation == Configuration.ORIENTATION_LANDSCAPE){	//横屏
			screen[1] = dm.widthPixels;
			screen[0] = dm.heightPixels;
		}else{		//竖屏 or 默认
			screen[0] = dm.widthPixels;
			screen[1] = dm.heightPixels;
		}
		return screen;
	}

	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wManager.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
}
