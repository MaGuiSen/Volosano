package lib.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Context sContext;
	private static Toast sToast;

	public static void cancel() {
		if (sToast != null) {
			sToast.cancel();
		}
	}

	public static void setContext(Context context) {
		sContext = context;
	}

	public static void show(CharSequence text) {
		if("".equals(text.toString().trim()))
			return;
		show(text, Toast.LENGTH_LONG);
	}

	public static void show(CharSequence text, int duration) {
		if (sContext == null) {
			return;
		}
		if (sToast == null) {
			sToast = Toast.makeText(sContext, text, duration);
		} else {
			sToast.setDuration(duration);
			sToast.setText(text);
		}
		sToast.show();
	}

	public static void show(int resId) {
		show(resId, Toast.LENGTH_LONG);
	}

	public static void show(int resId, int duration) {
		if (sContext == null) {
			return;
		}
		show(sContext.getText(resId), duration);
	}

}
