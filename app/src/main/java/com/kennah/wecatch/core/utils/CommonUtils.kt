package com.kennah.wecatch.core.utils

import android.app.ActivityManager
import android.content.Context
import android.graphics.*
import android.net.ConnectivityManager
import android.view.View
import android.view.WindowManager
import com.kennah.wecatch.core.exception.NetworkException
import android.graphics.BlurMaskFilter.Blur
import com.kennah.wecatch.module.main.service.MainService


object CommonUtils {

    fun calculateScreenSize(manager: WindowManager): IntArray {

        val display = manager.defaultDisplay
        val size = Point()

        display.getSize(size)

        return intArrayOf(size.x, size.y)
    }

    @Throws(NetworkException::class)
    fun checkNetwork(context: Context) {
        if (!hasNetwork(context)) throw NetworkException()
    }

    private fun hasNetwork(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return info != null && info.isConnectedOrConnecting
    }

    fun saveToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Copied Text", text)
        clipboard.primaryClip = clip
    }

    fun getBitmapFromView(view: View): Bitmap {

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        //Create the bitmap
        val bitmap = Bitmap.createBitmap(view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888)
        //Create a canvas with the specified bitmap to draw into
        val c = Canvas(bitmap)

        //Render this view (and all of its children) to the given Canvas
        view.draw(c)

        return bitmap
    }

    fun highlightImage(src: Bitmap, color: Int): Bitmap {

        // create new bitmap, which will be painted and becomes result image
        val bmOut = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        // setup canvas for painting
        val canvas = Canvas(bmOut)
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)
        // create a blur paint for capturing alpha
        val ptBlur = Paint()
        ptBlur.maskFilter = BlurMaskFilter(25f, Blur.SOLID)
        val offsetXY = IntArray(2)
        // capture alpha into a bitmap
        val bmAlpha = src.extractAlpha(ptBlur, offsetXY)
        // create a color paint
        val ptAlphaColor = Paint()
        ptAlphaColor.color = color
        // paint color for captured alpha region (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0].toFloat(), offsetXY[1].toFloat(), ptAlphaColor)
        // free memory
        bmAlpha.recycle()

        // paint the image source
        canvas.drawBitmap(src, 0f, 0f, null)

        // return out final image
        return bmOut
    }

    @SuppressWarnings("deprecation")
    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }
}
