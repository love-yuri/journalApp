package com.yuri.journal.utils

object ClassUtils {

    fun Class<*>.findClass(name: String): Class<*>? {
        for (declaredClass in javaClass.declaredClasses) {
            if (declaredClass.simpleName == name) {
                return declaredClass
            }
        }
        return null
    }
}