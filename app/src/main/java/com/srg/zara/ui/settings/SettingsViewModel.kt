package com.srg.zara.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.srg.zara.MainActivity
import com.srg.zara.util.CountryFlags
import com.srg.zara.util.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Cache
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageManager: LanguageManager,
    private val countryFlags: CountryFlags,
    private val cache: Cache,
) : ViewModel() {

    var isCheckedFilterList: Boolean = true
    var isNeedReset: Boolean = false

    fun getLanguage(context: Context): String {
        return languageManager.getLanguage(context)
    }

    fun changeLocale(context: Context, language: String) {
        languageManager.saveLanguage(context, language)
        languageManager.setLocale(context, language)
        restartApp(context)
    }

    fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    fun getCountryFlagIcon(code: String): String {
       return countryFlags.getCountryFlagByCountryCode(code)
    }

    fun clearCache(){
        cache.evictAll()
    }

}