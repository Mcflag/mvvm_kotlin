package com.ccooy.app3.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Patterns
import com.ccooy.app3.base.AppConstants
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    val timestamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.SIMPLIFIED_CHINESE).format(Date())

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.assets
        val inputStream: InputStream = manager.open(jsonFileName)

        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return buffer.toString(Charsets.UTF_8)
    }
}