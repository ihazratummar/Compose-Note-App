package com.example.mynotes.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

object LanguageHelper {
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}