# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Common
-keepattributes *Annotation*,EnclosingMethod,Signature,Exceptions,InnerClasses

#Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-dontwarn okio.**

-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-dontwarn com.google.errorprone.annotations.*

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep public class com.kennah.wecatch.local.model.** { *; }

-keep class microsoft.mappoint.** {*;}

-keep class org.metalev.multitouch.controller.** {*;}
-keep class org.osmdroid.api.IGeoPoint {*;}
-keep class org.osmdroid.api.IMapController {*;}
-keep class org.osmdroid.api.IMapView {*;}
-keep class org.osmdroid.api.IProjection {*;}
-keep class org.osmdroid.events.** {*;}
-keep class org.osmdroid.tileprovider.constants.** {*;}
-keep class org.osmdroid.tileprovider.modules.** {*;}
-keep class org.osmdroid.tileprovider.tilesource.FileBasedTileSource {*;}
-keep class org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase {*;}
-keep class org.osmdroid.tileprovider.tilesource.QuadTreeTileSource {*;}
-keep class org.osmdroid.tileprovider.tilesource.TileSourceFactory {*;}
-keep class org.osmdroid.tileprovider.tilesource.XYTileSource {*;}
-keep class org.osmdroid.tileprovider.util.Counters {*;}
-keep class org.osmdroid.tileprovider.util.ManifestUtil {*;}
-keep class org.osmdroid.tileprovider.util.SimpleInvalidationHandler {*;}
-keep class org.osmdroid.tileprovider.util.SimpleRegisterReceiver {*;}
-keep class org.osmdroid.tileprovider.util.StreamUtils {*;}
-keep class org.osmdroid.tileprovider.* {*;}
-keep class org.osmdroid.util.** {*;}
-keep class org.osmdroid.views.* {*;}
-keep class org.osmdroid.views.utils.** {*;}
-keep class org.osmdroid.views.overlay.gestures.** {*;}
-keep class org.osmdroid.views.overlay.* {*;}
-keep class org.osmdroid.views.overlay.infowindow* {*;}

-keep class org.osmdroid.* {*;}

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**