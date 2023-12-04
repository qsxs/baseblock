package com.lihb.baseblock.viewbinding

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> Activity.binding() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified Vb : ViewBinding> inflateBinding(layoutInflater: LayoutInflater): Vb {
    return Vb::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as Vb
}

inline fun <reified VB : ViewBinding> Fragment.binding() = lazy {
    FragmentBindingDelegate(VB::class.java)
}

@Suppress("UNCHECKED_CAST")
class FragmentBindingDelegate<VB : ViewBinding>(private val clazz: Class<VB>) :
    ReadOnlyProperty<Fragment, VB> {
    private var isInitialized = false
    private var _binging: VB? = null
    private val binding: VB get() = _binging!!

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (!isInitialized) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    _binging = null
                }
            })
            _binging =
                clazz.getMethod("bind", View::class.java).invoke(null, thisRef.requireView()) as VB
            isInitialized = true
        }
        return binding
    }

}



