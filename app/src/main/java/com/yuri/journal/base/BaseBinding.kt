package com.yuri.journal.base

import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

internal sealed interface BaseBinding<VB: ViewBinding> {
    val binding: VB

    // 绑定
    fun <T: ViewBinding> inflateBinding(inflater: LayoutInflater): T {
        var clazz: Class<*> = javaClass
        while (clazz.superclass != null) {
            clazz.findBindingMethod()?.let {
                @Suppress("UNCHECKED_CAST")
                return it.invoke(null, inflater) as T
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
        return (genericSuperclass as? ParameterizedType)?.actualTypeArguments // 获取模板列表
            ?.asSequence() // 转为kotlin元序列方便处理
            ?.filterIsInstance<Class<*>>() // 过滤出所有的类
            ?.firstOrNull { it.simpleName.endsWith("Binding") } // 找到对应的Binding类
            ?.getDeclaredMethod("inflate", LayoutInflater::class.java) // 找到绑定类inflater
            ?.also { it.isAccessible = true }
    }
}