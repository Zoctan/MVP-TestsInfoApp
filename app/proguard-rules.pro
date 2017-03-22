# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhangxinxin/Documents/SDK/android-sdk-macosx/tools/proguard/proguard-android.txt
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


# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

# 包名不混合大小写
-dontusemixedcaseclassnames
# 不忽略非公共的库类
-dontskipnonpubliclibraryclasses
# 输出混淆日志
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
# 不进行优化
-dontoptimize
# 不进行预检验
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

# 不混淆注解（注解不能被混淆）
-keepattributes *Annotation*
# 不混淆指定类
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# 不混淆native的方法
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# 不混淆继承View的所有类的set和get方法
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 不混淆继承Activity的所有类的中的参数类型为View的方法
# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 不混淆枚举类型的values和valueOf方法
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆继承Parcelable的所有类的CREATOR
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# 不混淆R类中所有static字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
# 忽略兼容库的所有警告
-dontwarn android.support.**

# Understand the @Keep support annotation.
# 不混淆指定的类及其类成员
-keep class android.support.annotation.Keep

# 不混淆使用注解的类及其类成员
-keep @android.support.annotation.Keep class * {*;}

# 不混淆所有类及其类成员中的使用注解的方法
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

# 不混淆所有类及其类成员中的使用注解的字段
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

# 不混淆所有类及其类成员中的使用注解的初始化方法
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
