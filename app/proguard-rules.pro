# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hefuyi/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-keep class io.sunc.mj.api.model.BaseBean
-keepclassmembers class io.sunc.mj.api.model.BaseBean { *; }
-keep class * extends io.sunc.mj.api.model.BaseBean
-keepclassmembers class * extends io.sunc.mj.api.model.BaseBean { *; }
-keep class ** {*;}

-keep class io.sunc.mj.api.model.BaseModel
-keepclassmembers class io.sunc.mj.api.model.BaseModel { *; }
-keep class * extends io.sunc.mj.api.model.BaseModel
-keepclassmembers class * extends io.sunc.mj.api.model.BaseModel { *; }

-keep class com.fasterxml.jackson.annotation.**{*;}
-keep class org.jsoup.nodes.**{*;}
-keep class io.paperdb.**{*;}

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements java.io.Serializable { *; }
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# https://github.com/square/retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# rxjava
-keep class rx.** { *; }
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keep class android.support.design.widget.TabLayout {*;}
-keep class android.support.design.widget.TabLayout$** {*;}
-keep class com.bigkoo.quicksidebar.QuickSideBarView {*;}
-keep class com.bigkoo.quicksidebar.QuickSideBarTipsView {*;}

# glide (https://github.com/bumptech/glide)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#Glide
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-dontwarn **

#umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class io.sunc.mj.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

