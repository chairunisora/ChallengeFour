package com.chairunissa.challengefour.database.local

import android.content.Context
import android.content.SharedPreferences
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.datauser.Login
import com.chairunissa.challengefour.datauser.Register

object AppLocalData {
    private const val KEY_USERNAME = "USERNAME"
    private const val KEY_EMAIL = "EMAIL"
    private const val KEY_PASSWORD = "PASSWORD"
    private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"

    private fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }

    fun setUserLoggedIn(context: Context, login: Login) {
        getSharedPreference(context).edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, login.isLogin)
            putString(KEY_USERNAME, login.username)
            apply()
        }
    }

    fun getUserLoggedIn(context: Context): Login? {
        val isLogin = getSharedPreference(context).getBoolean(KEY_IS_LOGGED_IN, false)
        val username = getSharedPreference(context).getString(KEY_USERNAME, "")

        if (isLogin) {
            return username?.let {
                Login(isLogin, it)
            }
        }

        return null
    }

    fun dropUserLoggedIn(context: Context) {
        getSharedPreference(context).edit().apply {
            remove(KEY_IS_LOGGED_IN)
            apply()
        }
    }

    fun setRegisteredUser(context: Context, registeredUser: Register) {
        getSharedPreference(context).edit().apply {
            putString(KEY_USERNAME, registeredUser.username)
            putString(KEY_EMAIL, registeredUser.email)
            putString(KEY_PASSWORD, registeredUser.password)
            apply()
        }
    }

    fun getRegisteredUser(context: Context): Register? {
        val username = getSharedPreference(context).getString(KEY_USERNAME, "")
        val email = getSharedPreference(context).getString(KEY_EMAIL, "")
        val password = getSharedPreference(context).getString(KEY_PASSWORD, "")

        if (!username.isNullOrEmpty() &&
            !email.isNullOrEmpty() &&
            !password.isNullOrEmpty()) {
            return Register(username,email,password)
        }

        return null
    }
}