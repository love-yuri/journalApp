package com.yuri.journal.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

internal sealed interface BaseBinding<VB: ViewBinding> {
    val binding: VB

    // 绑定
    fun <T: ViewBinding> inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup? = null
    ): T {
        var clazz: Class<*> = javaClass
        while (clazz.superclass != null) {
            clazz.findBindingMethod()?.let {

                if (Fragment::class.java.isAssignableFrom(clazz)) {
                    @Suppress("UNCHECKED_CAST")
                    return it.invoke(null, inflater, container, false) as T
                } else {
                    @Suppress("UNCHECKED_CAST")
                    return it.invoke(null, inflater) as T
                }
            } ?.run {
                clazz = clazz.superclass
            }
        }
        error("没有找到对应的Binding类!")
    }

    /**
        Class<T>：表示具体的类类型，比如 String.class、Integer.class。
        ParameterizedType：表示带有参数的类型，比如 List<String>、Map<String, Integer>。
        GenericArrayType：表示泛型数组类型，比如 List<String>[]。
        TypeVariable<D>：表示类型变量，比如 T。
        WildcardType：表示通配符类型，比如 ? extends Number。
        binding = ActivityMainBinding.inflate(layoutInflater)
    */
    fun Class<*>.findBindingMethod(): Method? {
        val types: Array<Class<*>> = if (Fragment::class.java.isAssignableFrom(this)) {
            arrayOf(LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        } else {
            arrayOf(LayoutInflater::class.java,)
        }
        return (genericSuperclass as? ParameterizedType)?.actualTypeArguments
            ?.asSequence()
            ?.filterIsInstance<Class<*>>()
            ?.firstOrNull { it.simpleName.endsWith("Binding") }
            ?.getDeclaredMethod("inflate", *types)
            ?.also { it.isAccessible = true }
    }
}