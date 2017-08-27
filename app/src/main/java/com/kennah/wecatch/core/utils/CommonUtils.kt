package com.kennah.wecatch.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.net.ConnectivityManager
import android.view.View
import android.view.WindowManager
import com.kennah.wecatch.core.exception.NetworkException

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
}