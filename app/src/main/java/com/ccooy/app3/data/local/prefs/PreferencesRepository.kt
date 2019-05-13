package com.ccooy.app3.data.local.prefs

import android.content.SharedPreferences
import com.ccooy.app3.data.model.response.LoginUser
import com.ccooy.app3.ext.prefs.boolean
import com.ccooy.app3.ext.prefs.gson
import com.ccooy.app3.ext.prefs.string

class PreferencesRepository(prefs: SharedPreferences) {
    companion object {
        private const val PREF_KEY_AUTO_LOGIN = "PREF_KEY_AUTO_LOGIN"
        private const val PREF_KEY_USERNAME = "PREF_KEY_USERNAME"
        private const val PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD"
        private const val PREF_KEY_LOGINUSER = "PREF_KEY_LOGINUSER"
    }

    var autoLogin by prefs.boolean(PREF_KEY_AUTO_LOGIN, true)

    var username by prefs.string(PREF_KEY_USERNAME, "")
    var password by prefs.string(PREF_KEY_PASSWORD, "")
    var user:LoginUser by prefs.gson(PREF_KEY_LOGINUSER, LoginUser())

}