# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\tools\sdk_all/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames


# 4.2以上版本调用js接口,需要在方法使用声明@JavascriptInterface,然后混淆时可能会弄丢该声明导致，程序无法调用js，需要继续再配置文件中添加条件，
-keepclassmembers class com.ly.jsnativetest.MyJSInterfaceImp {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keepclassmembers class * extends android.webkit.WebChromeClient{
   		public void openFileChooser(...);
}