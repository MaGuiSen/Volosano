@echo call adb devices
call adb devices

@echo push package to mobile
call adb push C:\gsma\AndroidStudioWorkSpace\work_git\Volosano\app\build\outputs\apk\app-release.apk /data/local/tmp/com.volosano

@echo install app
call adb shell pm install -r "/data/local/tmp/com.volosano"

@echo start app
call adb shell am start -n "com.volosano/com.volosano.SplashActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER