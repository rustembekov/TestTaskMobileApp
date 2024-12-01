package com.example.testtaskmobileapp.core.utilities

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: Int? = null
) : ReadWriteProperty<Any, Int?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return if (sharedPreferences.contains(getKey(property))) {
            sharedPreferences.getInt(getKey(property), defaultValue ?: 0)
        } else {
            defaultValue
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        with(sharedPreferences.edit()) {
            if (value == null) remove(getKey(property)) else putInt(getKey(property), value)
            apply()
        }
    }

    private fun getKey(property: KProperty<*>) = key ?: property.name
}


class FloatPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: Float = 0.0F
) : ReadWriteProperty<Any, Float> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Float =
        sharedPreferences.getFloat(getKey(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) =
        sharedPreferences.edit().putFloat(getKey(property), value).apply()

    private fun getKey(property: KProperty<*>) = key ?: property.name
}

class BooleanPreference(
    private val sharedPreferences: SharedPreferences,
    private var key: String? = null,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return sharedPreferences.getBoolean(getKey(property), defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        sharedPreferences.edit().putBoolean(getKey(property), value).apply()

    private fun getKey(property: KProperty<*>) = key ?: property.name
}

class StringPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        sharedPreferences.getString(getKey(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
        sharedPreferences.edit().putString(getKey(property), value).apply()

    private fun getKey(property: KProperty<*>) = key ?: property.name
}

class GsonPreference<T>(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val type: Type = object : TypeToken<T>() {}.type,
    private val key: String? = null,
    private val defaultValue: T? = null
) : ReadWriteProperty<Any, T?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val string = sharedPreferences.getString(getKey(property), null)
        return if (string.isNullOrEmpty()) {
            defaultValue
        } else {
            try {
                gson.fromJson(string, type)
            }catch (e: Exception) {
                defaultValue
            }
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        val string = gson.toJson(value)
        sharedPreferences.edit()
            .putString(getKey(property), string)
            .apply()
    }

    private fun getKey(property: KProperty<*>) = key ?: property.name
}

class LongPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: Long = 0L
) : ReadWriteProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long =
        sharedPreferences.getLong(getKey(property), defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
        sharedPreferences.edit().putLong(getKey(property), value).apply()

    private fun getKey(property: KProperty<*>) = key ?: property.name
}

class SetPreference<T : Any>(
    private val sharedPreferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: Set<T> = emptySet(),
    private val transformTo: (T) -> String,
    private val transformFrom: (String) -> T,
) : ReadWriteProperty<Any, Set<T>?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Set<T>? {
        return sharedPreferences.getStringSet(
            getKey(property),
            transformSetSafe(defaultValue, transformTo)
        )?.let { transformSetSafe(it, transformFrom) }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<T>?) {
        sharedPreferences.edit()
            .putStringSet(
                getKey(property),
                value?.let { transformSetSafe(it, transformTo) }
            )
            .apply()
    }

    private fun <U : Any, V : Any> transformSetSafe(
        value: Set<U>,
        transform: (U) -> V
    ): Set<V> {
        val transformedValue = mutableSetOf<V>()
        value.mapNotNullTo(transformedValue) {
            try {
                transform.invoke(it)
            } catch (e: Throwable) {
                null
            }
        }
        return transformedValue
    }

    private fun getKey(property: KProperty<*>) = key ?: property.name
}
