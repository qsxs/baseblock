
#Glide开始
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#Glide结束


# Gson开始
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class com.google.gson.examples.android.model.** { *; }
-keepnames class * implements java.io.Serializable
-keep class * extends com.lihb.baseblock.base.Base{ *; }
#Gson 结束