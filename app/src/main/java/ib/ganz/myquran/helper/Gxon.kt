package ib.ganz.myquran.helper

import com.google.gson.Gson

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by limakali on 3/17/2018.
 */

object Gxon {
    fun toJsonObject(o: Any): String {
        return Gson().toJson(o)
    }

    fun <T> fromJsonObject(json: String, clazz: Class<T>): T {
        return Gson().fromJson(json, clazz)
    }

    @JvmStatic
    fun <T> toJsonArray(l: List<T>): String {
        return Gson().toJson(l)
    }

    @JvmStatic
    fun <T> fromJsonArray(s: String, c: Class<T>): List<T> {
        return Gson().fromJson(s, ListOfJson(c))
    }

    private class ListOfJson<T> internal constructor(wrapper: Class<T>) : ParameterizedType {
        private val wrapped: Class<*>

        init {
            this.wrapped = wrapper
        }

        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(wrapped)
        }

        override fun getRawType(): Type {
            return List::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }
}
