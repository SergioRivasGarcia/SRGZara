package com.srg.zara.util

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

object LanguageManager {
    private const val LANGUAGE_KEY = "language_key"
    private const val DEFAULT_LANGUAGE = "gb"

    fun saveLanguage(context: Context, language: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit { putString(LANGUAGE_KEY, language) }
    }

    fun getLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}