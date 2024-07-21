package com.example.mynotes.util

import android.content.Context
import com.example.mynotes.ui.event.SortType

/**
 * @author Hazrat Ummar Shaikh
 */

object PreferenceUtil {

    private const val PREF_NAME = "note_preference"
    private const val KEY_TOGGLE_VIEW = "toggle_view"
    private const val KEY_SORT_BY = "sort_type"


    fun setToggleView(context: Context, isToggleView : Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_TOGGLE_VIEW, isToggleView).apply()
    }

    fun getToggleView(context: Context) : Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_TOGGLE_VIEW, false)
    }

    fun setSortType(context: Context, sortType : SortType) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_SORT_BY, sortType.name).apply()
    }

    fun getSortType(context: Context) : SortType {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sortType = pref.getString(KEY_SORT_BY, SortType.Date.name)
        return SortType.valueOf(sortType!!)
    }
}