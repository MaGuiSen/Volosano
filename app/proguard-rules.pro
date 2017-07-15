# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\gsma\AndroidStudioSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


##android 默认的##
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings
-keepattributes InnerClasses
#使用注解需要添加
-keepattributes *Annotation*

#指定不混淆所有的JNI方法
-keepclasseswithmembernames class * {
    native <methods>;
}
#所有View的子类及其子类的get、set方法都不进行混淆
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
#所有View的子类及其子类的get、set方法都不进行混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#不混淆Enum类型的指定方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#不混淆Parcelable和它的子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}
#不提示兼容库的错误警告
-dontwarn android.support.**
#用到了反射需要加入
-keepattributes Signature
-keepattributes EnclosingMethod
#不混淆Serializable接口的子类中指定的某些成员变量和方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#有用到WEBView的JS调用接口，需加入如下规则:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}


-dontwarn com.volosano.modal.**
-keep class com.volosano.modal.** {*;}

#eventbus
-keep class de.greenrobot.event.eventbus.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
}

###butterknife###
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

###fastjson###
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}


#systembartint系统栏
-keep class com.readystatesoftware.systembartint.** { *; }






