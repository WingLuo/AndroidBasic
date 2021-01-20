package tech.bootloader.androidbasic.utils

import android.content.Context

object  SharedPreferencesUtils {
    private val NAME = "application"
    private lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }

    fun saveBooleanSharedPreferences(key: String?, b: Boolean) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putBoolean(key, b)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun saveFloatSharedPreferences(key: String?, f: Float) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putFloat(key, f)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun saveIntSharedPreferences(key: String?, i: Int) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putInt(key, i)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun saveLongSharedPreferences(key: String?, l: Long) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putLong(key, l)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun saveStringSharedPreferences(key: String?, s: String?) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putString(key, s)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun saveStringSetSharedPreferences(key: String?, set: Set<String?>?) {
        /**
         * 偏好设置
         */
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        //获取偏好设置
        val editor = sp.edit()
        //获取偏好设置的编辑者
        editor.putStringSet(key, set)
        //编辑偏好设置
        editor.commit() //保存偏好设置
    }

    fun getBooleanSharedPreferences(key: String?): Boolean {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(key, false)
    }

    fun getFloatSharedPreferences(key: String?): Float {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getFloat(key, 0f)
    }

    fun getIntSharedPreferences(key: String?): Int {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getInt(key, 0)
    }

    fun getLongSharedPreferences(key: String?): Long {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getLong(key, 0L)
    }

    fun getStringSharedPreferences(key: String?): String? {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getString(key, "")
    }

    fun getMultiStringSharedPreferences(key: String?): String? {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_MULTI_PROCESS)
        return sp.getString(key, "")
    }

    fun getStringSetSharedPreferences(key: String?): Set<String?>? {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sp.getStringSet(key, null)
    }

    fun clear() {
        val sp = context!!.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.commit()
    }
}