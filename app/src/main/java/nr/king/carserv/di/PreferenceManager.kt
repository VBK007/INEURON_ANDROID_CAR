package nr.king.carserv.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import javax.inject.Inject
import kotlin.collections.HashMap

class PreferenceManager @Inject constructor(var context: Context) {

    private val PREFS_NAME = "SharedPreferenceFamily"

    data class HmRef(val ref: HashMap<String, String>?)
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun clearPrefs() {
        preferences.edit().clear().apply()
    }
    fun setUserId(userId: String)
    {
        preferences[USER_ID] = userId
    }

    fun getUserIds(): String {
       return  preferences[USER_ID]?:"";
    }



    /*private fun generateId(): String { //16 digit
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val generatedId = (1..12)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("") + getDeviceID()?.substring(12)
        return if (getUserIds().containsKey(generatedId)) generateId() else generatedId
    }*/

    @SuppressLint("HardwareIds")
     fun getDeviceID(): String? {
        return Settings.Secure.getString(
            context.contentResolver, ANDROID_ID
        )
    }


    fun getAuthToken():String
    {
        return preferences[authi_toki, ""] ?:""
    }

    fun setAuthToken(data: String) {

        preferences[authi_toki] = data
    }

    companion object {
        const val USER_ID = "userID"
        const val authi_toki ="xauth_toki"
        const val USER_DETAILS = "user_details"
        const val USER_NEW_RECORDS = "user_user_records"
    }
}


/**
 * SharedPreferences extension function, to listen the edit() and apply() fun calls
 * on every SharedPreferences operation.
 */
private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
private operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
private inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}