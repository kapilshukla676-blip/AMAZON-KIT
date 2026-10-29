package com.amazon.mShop.android.shopping

import android.content.Context
import android.provider.Settings
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object LicenseManager {
    private const val MASTER_KEY = "ARTHUR-DR-00000"
    private const val HMAC_SECRET = "ARTHURMODE_STEAMX_2024"
    private const val PREF = "amazon_lic"
    private const val KEY_LIC = "license"

    fun getDeviceId(ctx: Context): String =
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "UNKNOWN"

    fun isLicensed(ctx: Context): Boolean {
        val saved = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY_LIC, null) ?: return false
        return validate(ctx, saved)
    }

    fun validate(ctx: Context, key: String): Boolean {
        if (key == MASTER_KEY) return true
        if (!key.startsWith("ARTHUR-DR-")) return false
        val suffix = key.removePrefix("ARTHUR-DR-")
        val deviceId = getDeviceId(ctx)
        val expected = hmac("$deviceId:STEAMX").take(5).uppercase()
        return suffix == expected
    }

    fun saveLicense(ctx: Context, key: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_LIC, key).apply()
    }

    private fun hmac(data: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(HMAC_SECRET.toByteArray(), "HmacSHA256"))
        return mac.doFinal(data.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
