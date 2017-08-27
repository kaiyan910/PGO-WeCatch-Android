package com.kennah.wecatch.core.utils

import android.content.Context

object ResourceUtils {

    private val RESOURCE_TYPE_DRAWABLE = "drawable"
    private val RESOURCE_TYPE_STRING = "string"

    fun getDrawableResource(context: Context, resource: String): Int {
        return context.resources.getIdentifier(resource, RESOURCE_TYPE_DRAWABLE, context.packageName)
    }

    fun getStringResource(context: Context, resource: String): String {
        return context.getString(context.resources.getIdentifier(resource, RESOURCE_TYPE_STRING, context.packageName))
    }
}