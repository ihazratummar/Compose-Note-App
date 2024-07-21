package com.example.mynotes.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

class LocaleContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context, languageCode: String): ContextWrapper {
            var newContext = context
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                newContext = newContext.createConfigurationContext(config)
            } else {
                config.locale = locale
                newContext.resources.updateConfiguration(config, newContext.resources.displayMetrics)
            }
            return LocaleContextWrapper(newContext)
        }
    }
}