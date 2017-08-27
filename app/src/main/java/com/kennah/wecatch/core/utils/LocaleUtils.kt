package com.kennah.wecatch.core.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.*

object LocaleUtils {

    val CHINESE = "zh"
    val ENGLISH = "en"

    private val PREF_LANGUAGE = "language"
    private val SUPPORTED_LANGUAGE = Arrays.asList(ENGLISH, CHINESE)

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, getDefaultLanguage())
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String {
        return getPersistedData(context, Locale.getDefault().language)
    }

    private fun setLocale(context: Context, language: String): Context {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)

    }

    private fun getDefaultLanguage(): String {

        val currentLanguage = Locale.getDefault().language

        return if (SUPPORTED_LANGUAGE.contains(currentLanguage)) {
            currentLanguage
        } else ENGLISH

    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(PREF_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit()
                .putString(PREF_LANGUAGE, language)
                .apply()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {

        val locale: Locale = getSystemLocale(language)

        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.locales = LocaleList(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {

        val locale: Locale = getSystemLocale(language)

        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    private fun getSystemLocale(language: String): Locale {

        return when(language) {
            CHINESE -> Locale.TRADITIONAL_CHINESE
            else -> Locale.ENGLISH
        }
    }
}